package org.csuc.csv;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class CSVProjectTest {

    @Test
    public void readCSV() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Project.csv").getFile());
        File empty = null;

        assertTrue(Files.exists(file.toPath()));

        CSVProject data = new CSVProject(file.toString(), null);
        assertEquals(1, data.readCSV().size());
    }

    @Test
    public void readCSVRelation() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Project.csv").getFile());
        File fileRelation = new File(classLoader.getResource("RelationProject.csv").getFile());

        assertTrue(Files.exists(file.toPath()));
        assertTrue(Files.exists(fileRelation.toPath()));

        CSVProject data = new CSVProject(file.toString(), fileRelation.toString());

        assertEquals(1, data.readCSV().size());
        assertEquals(4, data.readCSVRelation().size());
    }

    @Test
    public void readCSVNull() throws IOException {
        CSVProject data = new CSVProject(null, null);

        assertEquals(null, data.readCSV());
        assertEquals(null, data.readCSV());
    }
}