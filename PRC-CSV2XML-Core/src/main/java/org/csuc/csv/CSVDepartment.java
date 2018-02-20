/**
 *
 */
package org.csuc.csv;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xmlns.org.eurocris.cerif_1.CERIF;
import xmlns.org.eurocris.cerif_1.CfOrgUnitType;
import com.csvreader.CsvReader;
import org.csuc.marshal.MarshalDepartments;
import org.apache.commons.io.FilenameUtils;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amartinez
 */
public class CSVDepartment implements Entity {

    private static Logger logger = LogManager.getLogger(CSVDepartment.class);

    /**
     * Llista que conté tots els departaments (cfOrgUnit)
     */
    private List<CfOrgUnitType> listDepType = new ArrayList<CfOrgUnitType>();

    private Pair<List<String>, List<String>> pair = null;
    private CsvReader reader = null;

    /**
     * @param path         Fitxer dels Departaments.
     * @param pathRelation Fitxer amb les relacions Departament/Investigador
     */
    public CSVDepartment(String path, String pathRelation) {
        if (new File(pathRelation).exists()) pair = createTuple(pathRelation);
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
                listDepType.add(new MarshalDepartments(
                        StringEscapeUtils.escapeXml11(reader.get(0)),
                        StringEscapeUtils.escapeXml11(reader.get(1)),
                        StringEscapeUtils.escapeXml11(reader.get(2)),
                        StringEscapeUtils.escapeXml11(reader.get(3)),
                        StringEscapeUtils.escapeXml11(reader.get(4)),
                        StringEscapeUtils.escapeXml11(reader.get(5)),
                        StringEscapeUtils.escapeXml11(reader.get(6)),
                        pair,
                        researcher).getDepartment());
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
    private Pair<List<String>, List<String>> createTuple(String pathRelation) {
        CsvReader reader = new CSVReader(pathRelation).getReader();
        List<String> listDept = new ArrayList<>();
        List<String> listOrcid = new ArrayList<>();
        try {
            while (reader.readRecord()) {
                listDept.add(StringEscapeUtils.escapeXml11(reader.get(0)));
                listOrcid.add(StringEscapeUtils.escapeXml11(reader.get(1)));
            }
            return new Pair<>(listDept, listOrcid);
        } catch (IOException e) {
            return null;
        } finally {
            reader.close();
        }
    }

    /************************************************** GETTERS / SETTERS ***************************************************/

    public List<CfOrgUnitType> getListDepType() {
        return listDepType;
    }
}
