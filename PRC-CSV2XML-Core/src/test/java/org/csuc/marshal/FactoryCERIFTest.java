package org.csuc.marshal;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.io.IoBuilder;
import org.csuc.csv.*;
import org.csuc.serialize.JaxbMarshal;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.Semantics;
import org.junit.Test;
import xmlns.org.eurocris.cerif_1.*;

import javax.xml.bind.JAXBException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.*;

public class FactoryCERIFTest {

    @Test
    public void createFactory() throws JAXBException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();

        CERIF cerif = new CERIF();

        List<CfPersType> cfPersTypeList = new ArrayList<>();

        //Researchers
        new CSVResearcher(new File(classLoader.getResource("Researcher.csv").getFile()).toString()).readCSV().forEach(researcher -> {
            CfPersType pers =
                    FactoryCERIF.createFactory(
                            new MarshalReseracher(null, (String) researcher.get(0),
                                    null, (String) researcher.get(1),
                                    (String) researcher.get(2), null,
                                    (String) researcher.get(3), Semantics.getClassId(ClassId.CHECKED)));
            assertNotNull(pers);
            cfPersTypeList.add(pers);
        });

        //OrgUnits (Department)
        CSVDepartment csvDepartment = new CSVDepartment(new File(classLoader.getResource("Organisation.csv").getFile()).toString(),
                new File(classLoader.getResource("RelationOrganitsation.csv").getFile()).toString());

        csvDepartment.readCSV().forEach(researcher -> {
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
            assertNotNull(marshalDepartment);
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalDepartment);
        });

        //OrgUnits (Research Group)
        CSVResearchGroup csvResearchGroup = new CSVResearchGroup(new File(classLoader.getResource("ResearchGroup.csv").getFile()).toString(),
                new File(classLoader.getResource("RelationResearchGroup.csv").getFile()).toString());
        csvResearchGroup.readCSV().forEach(researcher -> {
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
            assertNotNull(marshalResearchGroup);
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalResearchGroup);
            //cfPersTypeList.addAll(marshalResearchGroup.getNewCfPersType());
        });

        //Projects
        new CSVProject(new File(classLoader.getResource("Project.csv").getFile()).toString(),
                new File(classLoader.getResource("RelationProject.csv").getFile()).toString()).readCSV().forEach(researcher -> {
            MarshalProject marshalProject =
                    new MarshalProject(
                            (String) researcher.get(0),
                            (String) researcher.get(1),
                            (String) researcher.get(2),
                            (String) researcher.get(3),
                            (String) researcher.get(4),
                            (String) researcher.get(5),
                            (String) researcher.get(6),
                            null,
                            null
                    );
            assertNotNull(marshalProject);
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalProject);
           // cfPersTypeList.addAll(marshalProject.getNewCfPersType());
        });

        //Publications
        new CSVPublication(new File(classLoader.getResource("Publication.csv").getFile()).toString(),
                new File(classLoader.getResource("RelationPublication.csv").getFile()).toString()).readCSV().forEach(researcher -> {
            MarshalPublication marshalPublication =
                    new MarshalPublication(
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
                            null,
                            null
                    );
            assertNotNull(marshalPublication);
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(marshalPublication);
            //cfPersTypeList.addAll(marshalPublication.getNewCfPersType());
        });

        cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(cfPersTypeList);

        JaxbMarshal jxb = new JaxbMarshal(cerif, CERIF.class);
        jxb.marshaller(IoBuilder.forLogger(FactoryCERIFTest.class).setLevel(Level.INFO).buildOutputStream(), StandardCharsets.UTF_8, true, false);
    }
}