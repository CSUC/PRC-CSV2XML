/**
 *
 */
package org.csuc.csv;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.CfPersType;
import xmlns.org.eurocris.cerif_1.ObjectFactory;
import com.csvreader.CsvReader;
import org.csuc.marshal.MarshalInvestigators;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author amartinez
 */
public class CSVResearcher implements Entity {

    private static Logger logger = LogManager.getLogger(CSVResearcher.class);

    /**
     * Llista que conté tots els Investigadors (cfPers)
     */
    private List<CfPersType> listPersType = new ArrayList<CfPersType>();

    /**
     * HasMap amb clau orcid i valor l'identificador que se li ha donat amb el Singleton (newId)
     * <p>
     * String[0]: orcid
     * String[1]: id
     */
    private HashMap<String, String> mapResearcher = new HashMap<String, String>();

    /**
     * Llista amb els investigadors única (clau signatura i valor l'identificador únic creat amb el
     * Singleton-> newid). Útil per crear Persons uncheckeds.
     * <p>
     * String[0]: signatura
     * String[1]: id
     */
    private HashMap<String, String> UNCHECKEDS = new HashMap<>();


    private ObjectFactory FACTORY;
    private CsvReader reader;

    public CSVResearcher(String path) {
        if (new File(path).exists()) {
            this.reader = new CSVReader(path).getReader();
        } else {
            logger.info("No existeix el fitxer " + FilenameUtils.getName(path));
        }
    }

    /**
     * Crea tots els Autors 'uncheckeds' que ha trobat en totes les entitats. Aquests no estan repetits si la seva
     * signatura és idèntica.
     */
    public void addUncheckeds() {
        for (Entry<String, String> entry : emptyIfNull(UNCHECKEDS.entrySet()))
            listPersType.add(new MarshalInvestigators(
                    entry.getValue(), null, null, null, entry.getKey(), null,
                    null, Semantics.getClassId(ClassId.UNCHECKED)).getPers());
    }


    @Override
    public void ReadCSV(CSVResearcher researcher) {
        try {
            while (reader.readRecord()) {
                String name = StringEscapeUtils.escapeXml11(reader.get(0));
                String orcid = StringEscapeUtils.escapeXml11(reader.get(1));
                String signatura = StringEscapeUtils.escapeXml11(reader.get(2));
                String ae = StringEscapeUtils.escapeXml11(reader.get(3));

                MarshalInvestigators mRes
                        = new MarshalInvestigators(
                                null, name, null, orcid, signatura, null, ae, Semantics.getClassId(ClassId.CHECKED));

                listPersType.add(mRes.getPers());
                if (!mapResearcher.containsKey(mRes.getOrcid())) mapResearcher.put(mRes.getOrcid(), mRes.get_ID());
            }
        } catch (IOException e) {
            logger.error(e);
        } finally {
            reader.close();
        }
    }

    /************************************************** GETTERS / SETTERS ***************************************************/

    private <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }

    public List<CfPersType> getListPersType() {
        return listPersType;
    }

    public HashMap<String, String> getMapResearcher() {
        return mapResearcher;
    }

    public HashMap<String, String> getUNCHECKEDS() {
        return UNCHECKEDS;
    }

    public void setUNCHECKEDS(HashMap<String, String> uNCHECKEDS) {
        UNCHECKEDS = uNCHECKEDS;
    }
}