/**
 *
 */
package org.csuc.global;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe d'utilitats per a la gestió de dates i temps.
 * Proporciona mètodes per convertir dates entre diferents formats
 * i generar dates en format XMLGregorianCalendar.
 *
 * @author Albert Martínez
 */
public class Time {
    private static String DATE = "yyyy-MM-dd";
    private static String DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";


    /**
     * Converteix una data en format String a XMLGregorianCalendar amb format de data i hora.
     * Si hi ha algun error en la conversió, retorna null.
     *
     * @param inputDate Data d'entrada en format String
     * @return XMLGregorianCalendar amb la data i hora convertida, o null si hi ha error
     * @throws Exception Si hi ha un error en la conversió de la data
     */
    public static XMLGregorianCalendar formatDateTime(String inputDate) throws Exception {
        if (inputDate == null) return null;

        DateFormat format = new SimpleDateFormat(DATE_TIME);

        Date d = format.parse(inputDate);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(d));
    }

    /**
     * Converteix una data en format String a XMLGregorianCalendar amb format de data.
     * Si hi ha algun error en la conversió, retorna null.
     *
     * @param inputDate Data d'entrada en format String
     * @return XMLGregorianCalendar amb la data convertida, o null si hi ha error
     * @throws Exception Si hi ha un error en la conversió de la data
     */
    public static XMLGregorianCalendar formatDate(String inputDate) throws Exception {
        if (inputDate == null) return null;
        DateFormat format = new SimpleDateFormat(DATE);

        Date d = format.parse(inputDate);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(d));
    }
}
