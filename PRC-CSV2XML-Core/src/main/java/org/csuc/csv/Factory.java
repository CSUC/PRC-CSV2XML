/**
 *
 */
package org.csuc.csv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.serialize.JaxbMarshal;
import xmlns.org.eurocris.cerif_1.CERIF;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * @author amartinez
 */
public class Factory {

    private static Logger logger = LogManager.getLogger(Factory.class);

    private CSVResearcher RES;
    private CSVDepartment DEP;
    private CSVGroup GR;
    private CSVProject PROJ;
    private CSVPublication PUBL;

    /**
     * @param input
     */
    public Factory(final List<Entity> input) {
        for (Entity ent : input) {
            if (ent.getClass().equals(CSVResearcher.class)) RES = (CSVResearcher) ent;
            if (ent.getClass().equals(CSVDepartment.class)) DEP = (CSVDepartment) ent;
            if (ent.getClass().equals(CSVGroup.class)) GR = (CSVGroup) ent;
            if (ent.getClass().equals(CSVProject.class)) PROJ = (CSVProject) ent;
            if (ent.getClass().equals(CSVPublication.class)) PUBL = (CSVPublication) ent;
        }

        RES.ReadCSV(null);
        DEP.ReadCSV(RES);
        GR.ReadCSV(RES);
        PROJ.ReadCSV(RES);
        PUBL.ReadCSV(RES);

        //Crear llistat d'uncheckeds
        if (!RES.getUNCHECKEDS().isEmpty()) RES.addUncheckeds();


    }

    /**
     *
     * @param ruct
     * @param outFile
     */
    public void XML(String ruct, String outFile) {
        CERIF cerif = new CERIF();
        try {
            GregorianCalendar gregory = new GregorianCalendar();
            gregory.setTime(new Date());
            cerif.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory));
            cerif.setSourceDatabase(ruct);

            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(RES.getListPersType());
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(DEP.getListDepType());
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(GR.getListGrType());
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(PROJ.getListProjType());
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(PUBL.getListPublType());


            new JaxbMarshal(cerif, CERIF.class).marshaller(new FileOutputStream(outFile));
        } catch (DatatypeConfigurationException e) {
            logger.error(e);
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (JAXBException e) {
            logger.error(e);
        }
    }


    public void XML(String ruct, String outFile, Charset charset, boolean formatted, boolean fragment) {
        CERIF cerif = new CERIF();
        try {
            GregorianCalendar gregory = new GregorianCalendar();
            gregory.setTime(new Date());
            cerif.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory));
            cerif.setSourceDatabase(ruct);

            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(RES.getListPersType());
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(DEP.getListDepType());
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(GR.getListGrType());
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(PROJ.getListProjType());
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(PUBL.getListPublType());


            new JaxbMarshal(cerif, CERIF.class).marshaller(new FileOutputStream(outFile), charset, true, true);
        } catch (DatatypeConfigurationException e) {
            logger.error(e);
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (JAXBException e) {
            logger.error(e);
        }
    }


}
