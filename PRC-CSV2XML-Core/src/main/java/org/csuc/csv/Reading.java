package org.csuc.csv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author amartinez
 */
public class Reading {

    private static Logger logger = LogManager.getLogger(Reading.class);

    /**
     *
     * @param file
     * @param cellProcessors
     * @throws IOException
     */
    public static List<List<Object>> readWithCsvListReader(String file, CellProcessor[] cellProcessors) throws IOException {
        ICsvListReader listReader = null;
        List<List<Object>> result = new ArrayList<>();

        if(Objects.nonNull(file)){
            try {
                listReader = new CsvListReader(new FileReader(file), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
                listReader.getHeader(true); // skip the header (can't be used with CsvListReader)

                List<Object> customerList;
                while( (customerList = listReader.read(cellProcessors)) != null ) {
                    result.add(customerList);
                    logger.debug("Line: {}  Row: {}  Data:  {}", listReader.getLineNumber(), listReader.getRowNumber(), customerList);
                }
            }
            finally {
                if( listReader != null )    listReader.close();
            }
        }
        return result.isEmpty() ? null : result;
    }

    /**
     *
     * @param file
     * @param cellProcessors
     * @throws IOException
     */
    public static List<List<Object>> readWithCsvListReader(File file, CellProcessor[] cellProcessors) throws IOException {
        ICsvListReader listReader = null;
        List<List<Object>> result = new ArrayList<>();

        try {
            listReader = new CsvListReader(new FileReader(file), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            listReader.getHeader(true); // skip the header (can't be used with CsvListReader)

            List<Object> customerList;
            while( (customerList = listReader.read(cellProcessors)) != null ) {
                result.add(customerList);
                logger.debug("Line: {}  Row: {}  Data:  {}", listReader.getLineNumber(), listReader.getRowNumber(), customerList);
            }
            return result;
        }
        finally {
            if( listReader != null ) {
                listReader.close();
            }
        }
    }

}
