package org.csuc.csv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.prefs.CsvPreference;

import java.util.List;

/**
 * @author amartinez
 */
public class CSVDepartment implements Read<List<List<Object>>>{

    private static Logger logger = LogManager.getLogger(CSVDepartment.class);

    private List<List<Object>> data;
    private List<List<Object>> dataRelation;

    private String file;
    private String fileRelation;

    public CSVDepartment(String file, String relation) throws Exception {
        this.file = file;
        this.fileRelation = relation;

        logger.debug("Department file:           {}", this.file);
        logger.debug("Department Relation file:  {}", this.fileRelation);

        data = Reading.readWithCsvListReader(this.file, Processors.getProcessorsDepartment(), 7);
        dataRelation = Reading.readWithCsvListReader(this.fileRelation, Processors.getProcessorsDepartmentRelation(), 2);
    }

    //char quoteChar, int delimiterChar, String endOfLineSymbols
    public CSVDepartment(String file, String relation, CsvPreference csvPreference) throws Exception {
        this.file = file;
        this.fileRelation = relation;

        logger.debug("Department file:           {}", this.file);
        logger.debug("Department Relation file:  {}", this.fileRelation);

        data = Reading.readWithCsvListReader(this.file, Processors.getProcessorsDepartment(), 7, csvPreference);
        dataRelation = Reading.readWithCsvListReader(this.fileRelation, Processors.getProcessorsDepartmentRelation(), 2, csvPreference);
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
