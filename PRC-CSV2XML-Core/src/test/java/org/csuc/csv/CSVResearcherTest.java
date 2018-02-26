package org.csuc.csv;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class CSVResearcherTest {

    @Test
    public void readCSV() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Researcher.csv").getFile());

        CSVResearcher data = new CSVResearcher(file.toString());
        assertEquals(3, data.readCSV().size());
    }
}