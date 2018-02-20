/**
 *
 */
package org.csuc.csv;

import com.csvreader.CsvReader;
import org.apache.commons.text.StringEscapeUtils;
import org.csuc.marshal.MarshalProjects;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Quartet;
import xmlns.org.eurocris.cerif_1.CfProjType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author amartinez
 */
public class CSVProject implements Entity {

    private static Logger logger = LogManager.getLogger(CSVProject.class);

    /**
     * Llista que conté tots els Projectes (cfProj)
     */
    private List<CfProjType> listProjType = new ArrayList<CfProjType>();
    private Quartet<List<String>, List<String>, List<String>, List<String>> quartet = null;
    private CsvReader reader = null;

    /**
     * @param path         Fitxer dels Projectes.
     * @param pathRelation Fitxer amb les relacions Projectes/Investigador
     */
    public CSVProject(String path, String pathRelation) {
        if (new File(pathRelation).exists()) quartet = createQuartet(pathRelation);
        if (new File(path).exists()) {
            reader = new CSVReader(path).getReader();
        } else {
            logger.info("No existeix el fitxer {}", FilenameUtils.getName(path));
        }
    }

    @Override
    public void ReadCSV(CSVResearcher researcher) {
        try {
            while (reader.readRecord()) {
                listProjType.add(new MarshalProjects(
                        StringEscapeUtils.escapeXml11(reader.get(0)),
                        StringEscapeUtils.escapeXml11(reader.get(1)),
                        StringEscapeUtils.escapeXml11(reader.get(2)),
                        StringEscapeUtils.escapeXml11(reader.get(3)),
                        StringEscapeUtils.escapeXml11(reader.get(4)),
                        StringEscapeUtils.escapeXml11(reader.get(5)),
                        StringEscapeUtils.escapeXml11(reader.get(6)),
                        quartet,
                        researcher).getPROJECT());
            }
        } catch (IOException e) {
            logger.error(e);
        } finally {
            reader.close();
        }
    }

    private Quartet<List<String>, List<String>, List<String>, List<String>> createQuartet(String pathRelation) {
        CsvReader reader = new CSVReader(pathRelation).getReader();
        List<String> listCode = new ArrayList<String>();
        List<String> listSig = new ArrayList<String>();
        List<String> listOrcid = new ArrayList<String>();
        List<String> listIP = new ArrayList<String>();
        try {
            while (reader.readRecord()) {
                listCode.add(StringEscapeUtils.escapeXml11(reader.get(0)));
                listSig.add(StringEscapeUtils.escapeXml11(reader.get(1)));
                listOrcid.add(StringEscapeUtils.escapeXml11(reader.get(2)));
                listIP.add(StringEscapeUtils.escapeXml11(reader.get(3)));
            }
            return new Quartet<>(listCode, listSig, listOrcid, listIP);
        } catch (IOException e) {
            return null;
        } finally {
            reader.close();
        }
    }

    /************************************************** GETTERS / SETTERS ***************************************************/

    public List<CfProjType> getListProjType() {
        return listProjType;
    }

}
