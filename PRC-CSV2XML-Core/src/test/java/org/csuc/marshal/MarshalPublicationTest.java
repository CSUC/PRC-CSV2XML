package org.csuc.marshal;

import org.csuc.csv.CSVPublication;
import org.junit.Test;
import xmlns.org.eurocris.cerif_1.CfResPublType;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class MarshalPublicationTest {

    @Test
    public void execute() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Publication.csv").getFile());
        File fileRelation = new File(classLoader.getResource("RelationPublication.csv").getFile());

        assertTrue(file.exists());
        assertTrue(fileRelation.exists());

        new CSVPublication(file.toString(), fileRelation.toString()).readCSV().forEach(researcher -> {
            CfResPublType publication =
                    FactoryCERIF.createFactory(
                            new MarshalPublication(
                                    new NameOrTitle((String) researcher.get(0), null, null),
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
                            ));
            assertNotNull(publication);
        });
    }
}