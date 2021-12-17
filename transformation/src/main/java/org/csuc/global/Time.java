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
 * @author amartinez
 *
 */
public class Time {
    private static String DATE = "yyyy-MM-dd";
    private static String DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";


    /**
     *
     * Canvia el format d'una data de tipus Date a Date Time (ISO-8601). Qualsevol error retorna un String null
     * per a poder invalidad la data.
     *
     * @param inputDate
     * @return
     */
    public static XMLGregorianCalendar formatDateTime(String inputDate) throws Exception {
        if (inputDate == null) return null;

        DateFormat format = new SimpleDateFormat(DATE_TIME);

        Date d = format.parse(inputDate);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(d));
    }

    /**
     *
     * Canvia el format d'una data de tipus Date a Date Time (ISO-8601). Qualsevol error retorna un String null
     * per a poder invalidad la data.
     *
     * @param inputDate
     * @return
     */
    public static XMLGregorianCalendar formatDate(String inputDate) throws Exception {
        if (inputDate == null) return null;
        DateFormat format = new SimpleDateFormat(DATE);

        Date d = format.parse(inputDate);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(d));
    }
}
