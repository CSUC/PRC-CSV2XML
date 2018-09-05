package org.csuc.csv;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.*;

public class CSVDepartmentTest {

    @Test
    public void readCSV() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Organisation.csv").getFile());
        File empty = null;

        assertTrue(Files.exists(file.toPath()));

        CSVDepartment data = new CSVDepartment(file.toString(), null);
        assertEquals(1, data.readCSV().size());
    }

    @Test
    public void readCSVRelation() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Organisation.csv").getFile());
        File fileRelation = new File(classLoader.getResource("RelationOrganitsation.csv").getFile());

        assertTrue(Files.exists(file.toPath()));
        assertTrue(Files.exists(fileRelation.toPath()));

        List data = new CSVDepartment(file.toString(), fileRelation.toString()).readCSVRelation();

        assertEquals(3, data.size());
    }

    @Test
    public void readCSVNull() throws Exception {
        CSVDepartment data = new CSVDepartment(null, null);

        assertEquals(null, data.readCSV());
        assertEquals(null, data.readCSV());
    }
}