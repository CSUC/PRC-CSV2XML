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
     * Long que conté la durada des de que entra la petició fins que envia la resposta
     * JSON.
     * 
     * @param inici
     * @return
     */
    public static long getTime(Date inici){
    	Date fi = new Date();
  		long diff = fi.getTime() - inici.getTime();
  		return TimeUnit.MILLISECONDS.toSeconds(diff);
    }  
    
    public static void getTime(final Date inici, final String Strategy) {
    	Date fi = new Date();
  		long diff = fi.getTime() - inici.getTime();

		long s = TimeUnit.MILLISECONDS.toSeconds(diff);
		long m = TimeUnit.MILLISECONDS.toMinutes(diff);
		long h	= TimeUnit.MILLISECONDS.toHours(diff);

		logger.info("Time "+Strategy+":\t"
				+ h + "h:"+ m + "m:"+ s + "s");
	}

    /**
     *
     * Canvia el format d'una data de tipus Date a Date Time (ISO-8601). Qualsevol error retorna un String null
     * per a poder invalidad la data.
     *
     * @param inputDate
     * @param formatOut
     * @return
     */
	public static XMLGregorianCalendar formatDateTime(String inputDate, String formatOut){
		if(inputDate == null) return null;
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(inputDate);
        } catch (DatatypeConfigurationException e) {
            logger.error(e);
            return null;
        }
    }
		
	public static Boolean isDateValid(XMLGregorianCalendar xmlGregorianCalendar) throws DatatypeConfigurationException{
		if(xmlGregorianCalendar.getYear()
					<= DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()).getYear())	return true;
		return false;
	}
	
	public static String getDATE() {
		return DATE;
	}

	public static String getDATE_TIME() {
		return DATE_TIME;
	}
	
}
