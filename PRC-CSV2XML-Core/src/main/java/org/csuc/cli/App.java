package org.csuc.cli;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.csv.*;
import org.csuc.marshal.*;
import org.csuc.poi.XLSX2CSV;
import org.csuc.serialize.JaxbMarshal;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.Semantics;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.supercsv.prefs.CsvPreference;
import xmlns.org.eurocris.cerif_1.*;

import javax.xml.datatype.DatatypeFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @author amartinez
 */
public class App {

    private static Logger logger = LogManager.getLogger("PRC-CSV2XML");

    private static ArgsBean bean;

    public static void main(String[] args) {
        new App().doMain(args);
    }

    private void doMain(String[] args) {
        bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.exit(1);
        }

        XLSX2CSV xlsx2CSV = null;
        try {
            xlsx2CSV = new XLSX2CSV(bean.getInput().toFile(), bean.getDelimiter(), bean.getEndOfLineSymbols());
            xlsx2CSV.execute();

            xlsx2CSV.getFiles().forEach((key, value) -> {
                try {
                    switch (key) {
                        case researchers:
                            bean.setResearcher(value.toPath());
                            break;
                        case departments:
                            bean.setDepartment(value.toPath());
                            break;
                        case departments_relations:
                            bean.setRelationDepartment(value.toPath());
                            break;
                        case research_groups:
                            bean.setResearcherGroup(value.toPath());
                            break;
                        case research_groups_relations:
                            bean.setRelationResearcherGroup(value.toPath());
                            break;
                        case projects:
                            bean.setProject(value.toPath());
                            break;
                        case projects_relations:
                            bean.setRelationProject(value.toPath());
                            break;
                        case publications:
                            bean.setPublication(value.toPath());
                            break;
                        case publication_relations:
                            bean.setRelationPublication(value.toPath());
                            break;
                    }
                } catch (FileNotFoundException e) {
                    logger.error(e);
                }
            });

            CERIF cerif = new CERIF();

            GregorianCalendar gregory = new GregorianCalendar();
            gregory.setTime(new Date());
            cerif.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory));
            cerif.setSourceDatabase(bean.getRuct());

            List<CfPersType> cfPersTypeList = new ArrayList<>();

            //Researchers
            logger.info("{}", bean.getResearcher());
            CSVResearcher csvResearcher = new CSVResearcher(bean.getResearcher(),
                    (new CsvPreference.Builder('"', bean.getDelimiter(), bean.getEndOfLineSymbols())).build());
            if(Objects.isNull(csvResearcher.readCSV()))   throw new Exception("Researchers not content!");

            csvResearcher.readCSV().forEach(researcher -> {
                MarshalReseracher marshalReseracher =
                        new MarshalReseracher(null, (String) researcher.get(0),
                                null, (String) researcher.get(1),
                                (String) researcher.get(2), null,
                                (String) researcher.get(3), Semantics.getClassId(ClassId.CHECKED));
                cfPersTypeList.add(marshalReseracher);
            });

            //OrgUnits (Department)
            logger.info("{} - {}", bean.getDepartment(), bean.getRelationDepartment());
            CSVDepartment csvDepartment = new CSVDepartment(bean.getDepartment(),
                    bean.getRelationDepartment(),
                    (new CsvPreference.Builder('"', bean.getDelimiter(), bean.getEndOfLineSymbols())).build());

            Optional.ofNullable(csvDepartment.readCSV()).ifPresent(present-> present.forEach(department -> {
                MarshalDepartment marshalDepartment =
                        new MarshalDepartment(
                                new NameOrTitle((String) department.get(0), null, null),
                                (String) department.get(1),
                                (String) department.get(2),
                                (String) department.get(3),
                                (String) department.get(4),
                                (String) department.get(5),
                                (String) department.get(6),
                                csvDepartment.readCSVRelation(),
                                cfPersTypeList
                        );
                cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalDepartment);
            }));

            //OrgUnits (Research Group)
            logger.info("{} - {}", bean.getResearcherGroup(), bean.getRelationResearcherGroup());
            CSVResearchGroup csvResearchGroup = new CSVResearchGroup(bean.getResearcherGroup(),
                    bean.getRelationResearcherGroup(),
                    (new CsvPreference.Builder('"', bean.getDelimiter(), bean.getEndOfLineSymbols())).build());

            Optional.ofNullable(csvResearchGroup.readCSV()).ifPresent(present-> present.forEach(group -> {
                MarshalResearchGroup marshalResearchGroup = new MarshalResearchGroup(
                        new NameOrTitle((String) group.get(0), null, null),
                        (String) group.get(1),
                        (String) group.get(2),
                        (String) group.get(3),
                        (String) group.get(4),
                        (String) group.get(5),
                        (String) group.get(6),
                        csvResearchGroup.readCSVRelation(),
                        cfPersTypeList
                );
                cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalResearchGroup);
                cfPersTypeList.addAll(marshalResearchGroup.getNewCfPersType());
            }));


            //Projects
            logger.info("{} - {}", bean.getProject(), bean.getRelationProject());
            CSVProject csvProject = new CSVProject(bean.getProject(), bean.getRelationProject(),
                    (new CsvPreference.Builder('"', bean.getDelimiter(), bean.getEndOfLineSymbols())).build());
            Optional.ofNullable(csvProject.readCSV()).ifPresent(present-> present.forEach(project -> {
                MarshalProject marshalProject = new MarshalProject(
                        new NameOrTitle((String) project.get(0), null, null),
                        (String) project.get(1),
                        (String) project.get(2),
                        (String) project.get(3),
                        (String) project.get(4),
                        (String) project.get(5),
                        (String) project.get(6),
                        csvProject.readCSVRelation(),
                        cfPersTypeList
                );
                cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalProject);
                cfPersTypeList.addAll(marshalProject.getNewCfPersType());
            }));

            //Publications
            logger.info("{} - {}", bean.getPublication(), bean.getRelationPublication());
            CSVPublication csvPublication = new CSVPublication(bean.getPublication(), bean.getRelationPublication(),
                    (new CsvPreference.Builder('"', bean.getDelimiter(), bean.getEndOfLineSymbols())).build());

            Optional.ofNullable(csvPublication.readCSV()).ifPresent(present-> present.forEach(publication -> {
                MarshalPublication marshalPublication = new MarshalPublication(
                        new NameOrTitle((String) publication.get(0), null, null),
                        (String) publication.get(1),
                        (String) publication.get(2),
                        (String) publication.get(3),
                        (String) publication.get(4),
                        (String) publication.get(5),
                        (String) publication.get(6),
                        (String) publication.get(7),
                        (String) publication.get(8),
                        (String) publication.get(9),
                        (String) publication.get(10),
                        (String) publication.get(11),
                        (String) publication.get(12),
                        (String) publication.get(13),
                        (String) publication.get(14),
                        csvPublication.readCSVRelation(),
                        cfPersTypeList
                );
                cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalPublication);
                cfPersTypeList.addAll(marshalPublication.getNewCfPersType());
            }));

            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(cfPersTypeList);

            JaxbMarshal jxb = new JaxbMarshal(cerif, CERIF.class);
            if (Objects.nonNull(bean.getOutput()))
                jxb.marshaller(new FileOutputStream(bean.getOutput().toFile()), bean.getCharset(), bean.getFormatted(), false);
            else
                jxb.marshaller(IoBuilder.forLogger(App.class).setLevel(Level.INFO).buildOutputStream(), bean.getCharset(), bean.getFormatted(), false);

            logger.info("Done");
        } catch (Exception e) {
            logger.error(e);
        }finally {
            if(bean.isDeleteOnExit())   xlsx2CSV.deleteOnExit();
        }
    }
}