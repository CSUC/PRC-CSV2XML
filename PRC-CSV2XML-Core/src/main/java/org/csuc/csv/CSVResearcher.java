package org.csuc.csv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.prefs.CsvPreference;

import java.util.List;

/**
 * @author amartinez
 */
public class CSVResearcher implements Read<List<List<Object>>>{

    private static Logger logger = LogManager.getLogger(CSVResearcher.class);

    private List<List<Object>> data;
    private String file;

    public CSVResearcher(String file) throws Exception {
        this.file = file;

        logger.debug("Researcher file:           {}", this.file);
        data = Reading.readWithCsvListReader(this.file, Processors.getProcessorsResearcher(), 4);
    }

    public CSVResearcher(String file, CsvPreference csvPreference) throws Exception {
        this.file = file;

        logger.debug("Researcher file:           {}", this.file);
        data = Reading.readWithCsvListReader(this.file, Processors.getProcessorsResearcher(), 4, csvPreference);
    }

    @Override
    public List<List<Object>> readCSV(){
        return data;
    }

    @Override
    public <T> T readCSVRelation() {
        return null;
    }

}
