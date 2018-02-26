package org.csuc.cli;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.csv.*;
import org.csuc.marshal.*;
import org.csuc.serialize.JaxbMarshal;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.Semantics;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import xmlns.org.eurocris.cerif_1.*;

import javax.xml.datatype.DatatypeFactory;
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

    public void doMain(String[] args) {
        this.bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.exit(1);
        }

        try {
            CERIF cerif = new CERIF();

            GregorianCalendar gregory = new GregorianCalendar();
            gregory.setTime(new Date());
            cerif.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory));
            cerif.setSourceDatabase(bean.getRuct());

            List<CfPersType> cfPersTypeList = new ArrayList<>();

            //Researchers
            new CSVResearcher(bean.getResearcher()).readCSV().forEach(researcher -> {
                MarshalReseracher marshalReseracher =
                        new MarshalReseracher(null, (String) researcher.get(0),
                                null, (String) researcher.get(1),
                                (String) researcher.get(2), null,
                                (String) researcher.get(3), Semantics.getClassId(ClassId.CHECKED));
                cfPersTypeList.add(marshalReseracher);
            });

            //OrgUnits (Department)
            CSVDepartment csvDepartment = new CSVDepartment(bean.getDepartment(),
                    bean.getRelationDepartment());

            Optional.ofNullable(csvDepartment.readCSV()).ifPresent(present->{
                present.forEach(researcher -> {
                    MarshalDepartment marshalDepartment =
                            new MarshalDepartment(
                                    (String) researcher.get(0),
                                    (String) researcher.get(1),
                                    (String) researcher.get(2),
                                    (String) researcher.get(3),
                                    (String) researcher.get(4),
                                    (String) researcher.get(5),
                                    (String) researcher.get(6),
                                    csvDepartment.readCSVRelation(),
                                    cfPersTypeList
                            );
                    cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalDepartment);
                });
            });

            //OrgUnits (Research Group)
            CSVResearchGroup csvResearchGroup = new CSVResearchGroup(bean.getResearcherGroup(),
                    bean.getRelationResearcherGroup());

            Optional.ofNullable(csvResearchGroup.readCSV()).ifPresent(present->{
                present.forEach(researcher -> {
                    MarshalResearchGroup marshalResearchGroup = new MarshalResearchGroup(
                            (String) researcher.get(0),
                            (String) researcher.get(1),
                            (String) researcher.get(2),
                            (String) researcher.get(3),
                            (String) researcher.get(4),
                            (String) researcher.get(5),
                            (String) researcher.get(6),
                            csvResearchGroup.readCSVRelation(),
                            cfPersTypeList
                    );
                    cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalResearchGroup);
                    cfPersTypeList.addAll(marshalResearchGroup.getNewCfPersType());
                });
            });


            //Projects
            CSVProject csvProject = new CSVProject(bean.getProject(), bean.getRelationProject());
            Optional.ofNullable(csvProject.readCSV()).ifPresent(present->{
                present.forEach(researcher -> {
                    MarshalProject marshalProject = new MarshalProject(
                            (String) researcher.get(0),
                            (String) researcher.get(1),
                            (String) researcher.get(2),
                            (String) researcher.get(3),
                            (String) researcher.get(4),
                            (String) researcher.get(5),
                            (String) researcher.get(6),
                            csvProject.readCSVRelation(),
                            cfPersTypeList
                    );
                    cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalProject);
                    cfPersTypeList.addAll(marshalProject.getNewCfPersType());
                });
            });

            //Publications
            CSVPublication csvPublication = new CSVPublication(bean.getPublication(), bean.getRelationPublication());

            Optional.ofNullable(csvPublication.readCSV()).ifPresent(present->{
                present.forEach(researcher -> {
                    MarshalPublication marshalPublication = new MarshalPublication(
                            (String) researcher.get(0),
                            (String) researcher.get(1),
                            (String) researcher.get(2),
                            (String) researcher.get(3),
                            (String) researcher.get(4),
                            (String) researcher.get(5),
                            (String) researcher.get(6),
                            (String) researcher.get(7),
                            (String) researcher.get(8),
                            (String) researcher.get(9),
                            (String) researcher.get(10),
                            (String) researcher.get(11),
                            (String) researcher.get(12),
                            (String) researcher.get(13),
                            (String) researcher.get(14),
                            csvPublication.readCSVRelation(),
                            cfPersTypeList
                    );
                    cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalPublication);
                    cfPersTypeList.addAll(marshalPublication.getNewCfPersType());
                });
            });

            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(cfPersTypeList);

            JaxbMarshal jxb = new JaxbMarshal(cerif, CERIF.class);
            if (Objects.nonNull(bean.getOutput()))
                jxb.marshaller(new FileOutputStream(bean.getOutput().toFile()), bean.getCharset(), bean.getFormatted(), false);
            else
                jxb.marshaller(IoBuilder.forLogger(App.class).setLevel(Level.INFO).buildOutputStream(), bean.getCharset(), bean.getFormatted(), false);

            logger.info("Done");
        } catch (Exception e) {
            logger.error(e);
        }
    }
}