package org.csuc.csv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.prefs.CsvPreference;

import java.util.List;

/**
 * @author amartinez
 */
public class CSVPublication implements Read<List<List<Object>>> {

    private static Logger logger = LogManager.getLogger(CSVPublication.class);

    private List<List<Object>> data;
    private List<List<Object>> dataRelation;

    private String file;
    private String fileRelation;


    public CSVPublication(String file, String relation) throws Exception {
        this.file = file;
        this.fileRelation = relation;

        logger.debug("Publication file:           {}", this.file);
        logger.debug("Publication Relation file:  {}", this.fileRelation);

        data = Reading.readWithCsvListReader(this.file, Processors.getProcessorsPublication(), 15);
        dataRelation = Reading.readWithCsvListReader(this.fileRelation, Processors.getProcessorsPublicationRelation(), 4);
    }

    public CSVPublication(String file, String relation, CsvPreference csvPreference) throws Exception {
        this.file = file;
        this.fileRelation = relation;

        logger.debug("Publication file:           {}", this.file);
        logger.debug("Publication Relation file:  {}", this.fileRelation);

        data = Reading.readWithCsvListReader(this.file, Processors.getProcessorsPublication(), 15, csvPreference);
        dataRelation = Reading.readWithCsvListReader(this.fileRelation, Processors.getProcessorsPublicationRelation(), 4, csvPreference);
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
