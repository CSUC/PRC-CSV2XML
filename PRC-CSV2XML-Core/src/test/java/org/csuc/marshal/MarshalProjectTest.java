package org.csuc.marshal;

import org.csuc.csv.CSVProject;
import org.junit.Test;
import xmlns.org.eurocris.cerif_1.CfProjType;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class MarshalProjectTest {

    @Test
    public void execute() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Project.csv").getFile());
        File fileRelation = new File(classLoader.getResource("RelationProject.csv").getFile());

        assertTrue(file.exists());
        assertTrue(fileRelation.exists());

        new CSVProject(file.toString(), fileRelation.toString()).readCSV().forEach(researcher -> {
            CfProjType project =
                    FactoryCERIF.createFactory(
                            new MarshalProject(
                                    new NameOrTitle((String) researcher.get(0), null, null),
                                    (String) researcher.get(1),
                                    (String) researcher.get(2),
                                    (String) researcher.get(3),
                                    (String) researcher.get(4),
                                    (String) researcher.get(5),
                                    (String) researcher.get(6),
                                    null,
                                    null
                            ));
            assertNotNull(project);
        });
    }
}