package org.csuc.csv;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class CSVResearchGroupTest {

    @Test
    public void readCSV() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("ResearchGroup.csv").getFile());
        File empty = null;

        assertTrue(Files.exists(file.toPath()));

        CSVResearchGroup data = new CSVResearchGroup(file.toString(), null);
        assertEquals(1, data.readCSV().size());
    }

    @Test
    public void readCSVRelation() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("ResearchGroup.csv").getFile());
        File fileRelation = new File(classLoader.getResource("RelationResearchGroup.csv").getFile());

        assertTrue(Files.exists(file.toPath()));
        assertTrue(Files.exists(fileRelation.toPath()));

        CSVResearchGroup data = new CSVResearchGroup(file.toString(), fileRelation.toString());

        assertEquals(1, data.readCSV().size());
        assertEquals(4, data.readCSVRelation().size());
    }

    @Test
    public void readCSVNull() throws Exception {
        CSVResearchGroup data = new CSVResearchGroup(null, null);

        assertEquals(null, data.readCSV());
        assertEquals(null, data.readCSV());
    }
}