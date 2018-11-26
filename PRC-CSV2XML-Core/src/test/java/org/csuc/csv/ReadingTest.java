package org.csuc.csv;

import org.junit.Test;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class ReadingTest {

    @Test
    public void readWithCsvListReader() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("empty/Researcher.csv").getFile());

        List<List<Object>> researchers = Reading.readWithCsvListReader(file.toString(), Processors.getProcessorsResearcher(), 4,
                (new CsvPreference.Builder('"', ';', "\n")).build());

        assertNull(researchers);
    }
}