package org.csuc.marshal;

import org.csuc.csv.CSVDepartment;
import org.junit.Test;
import xmlns.org.eurocris.cerif_1.CfOrgUnitType;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MarshalDepartmentTest {

    @Test
    public void execute() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Organisation.csv").getFile());
        File fileRelation = new File(classLoader.getResource("RelationOrganitsation.csv").getFile());

        assertTrue(file.exists());
        assertTrue(fileRelation.exists());

        CSVDepartment data = new CSVDepartment(file.toString(), fileRelation.toString());

        data.readCSV().forEach(department -> {
            CfOrgUnitType dept =
                    FactoryCERIF.createFactory(
                            new MarshalDepartment(
                                   new NameOrTitle( (String) department.get(0), null , null),
                                    (String) department.get(1),
                                    (String) department.get(2),
                                    (String) department.get(3),
                                    (String) department.get(4),
                                    (String) department.get(5),
                                    (String) department.get(6),
                                    data.readCSVRelation(),
                                    null
                            ));
            assertNotNull(dept);
        });
    }
}