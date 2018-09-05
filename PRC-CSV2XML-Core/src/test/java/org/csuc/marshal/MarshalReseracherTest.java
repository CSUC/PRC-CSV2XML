package org.csuc.marshal;

import org.csuc.csv.CSVResearcher;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.Semantics;
import org.junit.Test;
import xmlns.org.eurocris.cerif_1.CfPersType;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class MarshalReseracherTest {

    @Test
    public void execute() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Researcher.csv").getFile());

        assertTrue(file.exists());

        new CSVResearcher(file.toString()).readCSV().forEach(researcher -> {
            CfPersType pers =
                    FactoryCERIF.createFactory(
                            new MarshalReseracher(null, (String) researcher.get(0),
                                    null, (String) researcher.get(1),
                                    (String) researcher.get(2), null,
                                    (String) researcher.get(3), Semantics.getClassId(ClassId.CHECKED)));
            assertNotNull(pers);
        });
    }
}