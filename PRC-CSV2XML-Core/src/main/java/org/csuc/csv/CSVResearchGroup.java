package org.csuc.csv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.prefs.CsvPreference;

import java.util.List;

/**
 * @author amartinez
 */
public class CSVResearchGroup implements Read<List<List<Object>>> {

    private static Logger logger = LogManager.getLogger(CSVResearchGroup.class);

    private List<List<Object>> data;
    private List<List<Object>> dataRelation;

    private String file;
    private String fileRelation;

    public CSVResearchGroup(String file, String relation) throws Exception {
        this.file = file;
        this.fileRelation = relation;

        logger.debug("Research Group file:           {}", this.file);
        logger.debug("Research Group file:  {}", this.fileRelation);

        data = Reading.readWithCsvListReader(this.file, Processors.getProcessorsResearchGroup(), 7);
        dataRelation = Reading.readWithCsvListReader(this.fileRelation, Processors.getProcessorsResearchGroupRelation(), 4);
    }

    public CSVResearchGroup(String file, String relation, CsvPreference csvPreference) throws Exception {
        this.file = file;
        this.fileRelation = relation;

        logger.debug("Research Group file:           {}", this.file);
        logger.debug("Research Group file:  {}", this.fileRelation);

        data = Reading.readWithCsvListReader(this.file, Processors.getProcessorsResearchGroup(), 7, csvPreference);
        dataRelation = Reading.readWithCsvListReader(this.fileRelation, Processors.getProcessorsResearchGroupRelation(), 4, csvPreference);
    }


    @Override
    public List<List<Object>> readCSV() {
        return data;
    }

    @Override
    public List<List<Object>> readCSVRelation() {
        return dataRelation;
    }

}
