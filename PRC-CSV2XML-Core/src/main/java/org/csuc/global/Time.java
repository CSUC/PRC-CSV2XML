/**
 * 
 */
package org.csuc.global;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author amartinez
 *
 */
public class Time {

    private static Logger logger = LogManager.getLogger(Time.class);

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
	public static XMLGregorianCalendar formatDateTime(String inputDate){
		if(inputDate == null) return null;
        try {
            DateFormat format = new SimpleDateFormat(DATE_TIME);

            Date d = format.parse(inputDate);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(d));
        } catch (ParseException e) {
            logger.error(e);
        } catch (DatatypeConfigurationException e) {
            logger.error(e);
        }
        return null;
    }

    /**
     *
     * Canvia el format d'una data de tipus Date a Date Time (ISO-8601). Qualsevol error retorna un String null
     * per a poder invalidad la data.
     *
     * @param inputDate
     * @return
     */
    public static XMLGregorianCalendar formatDate(String inputDate){
        if(inputDate == null) return null;
        try {
            DateFormat format = new SimpleDateFormat(DATE);

            Date d = format.parse(inputDate);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(d));
        } catch (ParseException e) {
            logger.error(e);
        } catch (DatatypeConfigurationException e) {
            logger.error(e);
        }
        return null;
    }
}
