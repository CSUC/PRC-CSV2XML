package org.csuc.csv;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class CSVPublicationTest {

    @Test
    public void readCSV() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Publication.csv").getFile());
        File empty = null;

        assertTrue(Files.exists(file.toPath()));

        CSVPublication data = new CSVPublication(file.toString(), null);
        assertEquals(1, data.readCSV().size());
    }

    @Test
    public void readCSVRelation() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Publication.csv").getFile());
        File fileRelation = new File(classLoader.getResource("RelationPublication.csv").getFile());

        assertTrue(Files.exists(file.toPath()));
        assertTrue(Files.exists(fileRelation.toPath()));

        List data = new CSVPublication(file.toString(), fileRelation.toString()).readCSVRelation();

        assertEquals(3, data.size());
    }

    @Test
    public void readCSVNull() throws Exception {
        CSVPublication data = new CSVPublication(null, null);

        assertEquals(null, data.readCSV());
        assertEquals(null, data.readCSV());
    }
}