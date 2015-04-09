/**
 * 
 */
package csv;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import com.csvreader.CsvReader;

/**
 * @author amartinez
 *
 */
public class CSVReader{
	private CsvReader reader = null;
	 
	public CSVReader(String file) {
		try{
			reader = new CsvReader(new InputStreamReader(new FileInputStream(file),
					detectCharset(new FileInputStream(new File(file)))), '|');
	        reader.readHeaders();
		}catch (UnsupportedEncodingException e) {
	        Logger.getLogger(CSVReader.class.getName()).info(e);
	    } catch (FileNotFoundException e) {
	        Logger.getLogger(CSVReader.class.getName()).info(e);
	    } catch (IOException e) {
	        Logger.getLogger(CSVReader.class.getName()).info(e);
	    }
	}

	/**
	 * Retorna el charset del fitxet.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static String detectCharset(InputStream is) throws IOException {
		  CharsetDetector charsetDetector=new CharsetDetector();
		  charsetDetector.setText(is instanceof BufferedInputStream ? is : new BufferedInputStream(is));
		  charsetDetector.enableInputFilter(true);
		  CharsetMatch cm=charsetDetector.detect();			
		  return cm.getName();
	}
	
	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public CsvReader getReader() {
        return reader;
    }
	
	public void close(){
        reader.close();
    }
}