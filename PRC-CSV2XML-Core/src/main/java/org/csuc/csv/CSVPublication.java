/**
 *
 */
package org.csuc.csv;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmlns.org.eurocris.cerif_1.CfResPublType;
import com.csvreader.CsvReader;
import org.csuc.marshal.MarshalPublications;
import org.apache.commons.io.FilenameUtils;

import org.javatuples.Quartet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author amartinez
 */
public class CSVPublication implements Entity {

    private Logger logger = LogManager.getLogger(CSVPublication.class);

    /**
     * Llista que conté totes les publicacions (cfResPers)
     */
    private List<CfResPublType> listPublType = new ArrayList<CfResPublType>();


    private Quartet<List<String>, List<String>, List<String>, List<String>> quartet = null;
    private CsvReader reader = null;

    /**
     * @param path         Fitxer de publicacions.
     * @param pathRelation Fitxer amb les relacions.
     */
    public CSVPublication(String path, String pathRelation) {
        if (new File(pathRelation).exists()) quartet = createQuartet(pathRelation);
        if (new File(path).exists()) {
            reader = new CSVReader(path).getReader();
        } else {
            logger.info("No existeix el fitxer  {}", FilenameUtils.getName(path));
        }
    }

    @Override
    public void ReadCSV(CSVResearcher researcher) {
        try {
            while (reader.readRecord()) {
                listPublType.add(new MarshalPublications(
                        StringEscapeUtils.escapeXml11(reader.get(0)),
                        StringEscapeUtils.escapeXml11(reader.get(1)),
                        StringEscapeUtils.escapeXml11(reader.get(2)),
                        StringEscapeUtils.escapeXml11(reader.get(3)),
                        StringEscapeUtils.escapeXml11(reader.get(4)),
                        StringEscapeUtils.escapeXml11(reader.get(5)),
                        StringEscapeUtils.escapeXml11(reader.get(6)),
                        StringEscapeUtils.escapeXml11(reader.get(7)),
                        StringEscapeUtils.escapeXml11(reader.get(8)),
                        StringEscapeUtils.escapeXml11(reader.get(9)),
                        StringEscapeUtils.escapeXml11(reader.get(10)),
                        StringEscapeUtils.escapeXml11(reader.get(11)),
                        StringEscapeUtils.escapeXml11(reader.get(12)),
                        StringEscapeUtils.escapeXml11(reader.get(13)),
                        StringEscapeUtils.escapeXml11(reader.get(14)),
                        researcher, quartet).getPUBLICATION());
            }
        } catch (IOException e) {
            logger.error(e);
        } finally {
            reader.close();
        }
    }

    /**
     * @param pathRelation
     * @return
     */
    private Quartet<List<String>, List<String>, List<String>, List<String>> createQuartet(String pathRelation) {
        CsvReader readerRelation = new CSVReader(pathRelation).getReader();
        List<String> listId = new ArrayList<>();
        List<String> listSignature = new ArrayList<>();
        List<String> listOrcid = new ArrayList<>();
        List<String> listInterve = new ArrayList<>();
        try {
            while (readerRelation.readRecord()) {
                listId.add(StringEscapeUtils.escapeXml11(readerRelation.get(0)));
                listSignature.add(StringEscapeUtils.escapeXml11(readerRelation.get(1)));
                listOrcid.add(StringEscapeUtils.escapeXml11(readerRelation.get(2)));
                listInterve.add(StringEscapeUtils.escapeXml11(readerRelation.get(3)));
            }
            return new Quartet<>(listId, listSignature, listOrcid, listInterve);
        } catch (IOException e) {
            logger.error(e);
            return null;
        } finally {
            readerRelation.close();
        }
    }

    /************************************************** GETTERS / SETTERS ***************************************************/

    public List<CfResPublType> getListPublType() {
        return listPublType;
    }
}
