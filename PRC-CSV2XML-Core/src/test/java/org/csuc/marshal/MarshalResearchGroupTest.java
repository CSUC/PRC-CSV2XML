package org.csuc.marshal;

import org.csuc.csv.CSVResearchGroup;
import org.junit.Test;
import xmlns.org.eurocris.cerif_1.CfOrgUnitType;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class MarshalResearchGroupTest {

    @Test
    public void execute() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("ResearchGroup.csv").getFile());
        File fileRelation = new File(classLoader.getResource("RelationResearchGroup.csv").getFile());

        assertTrue(file.exists());
        assertTrue(fileRelation.exists());

        new CSVResearchGroup(file.toString(), fileRelation.toString()).readCSV().forEach(researcher -> {
            CfOrgUnitType pers =
                    FactoryCERIF.createFactory(
                            new MarshalResearchGroup(
                                    new NameOrTitle( (String) researcher.get(0), null ,null),
                                    (String) researcher.get(1),
                                    (String) researcher.get(2),
                                    (String) researcher.get(3),
                                    (String) researcher.get(4),
                                    (String) researcher.get(5),
                                    (String) researcher.get(6),
                                    null,
                                    null
                            ));
            assertNotNull(pers);
        });
    }
}