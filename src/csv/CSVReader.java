/**
 * 
 */
package csv;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.csvreader.CsvReader;

/**
 * @author amartinez
 *
 */
public class CSVReader{
	 private CsvReader reader = null;
	 
	public CSVReader(String file, Charset charset) {
		try{
			reader = new CsvReader(new InputStreamReader(new FileInputStream(file), charset), '|');
	        reader.readHeaders();
		}catch (UnsupportedEncodingException e) {
	        Logger.getLogger(CSVReader.class.getName()).info(e);
	    } catch (FileNotFoundException e) {
	        Logger.getLogger(CSVReader.class.getName()).info(e);
	    } catch (IOException e) {
	        Logger.getLogger(CSVReader.class.getName()).info(e);
	    }
	}

	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public CsvReader getReader() {
        return reader;
    }
	
	public void close(){
        reader.close();
    }
}