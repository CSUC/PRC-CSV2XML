/**
 *
 */
package org.csuc.csv;

import com.csvreader.CsvReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import java.io.*;

/**
 * @author amartinez
 */
public class CSVReader {

    private static Logger logger = LogManager.getLogger(CSVReader.class);

    private CsvReader reader = null;

    public CSVReader(String file) {
        try {
            reader = new CsvReader(new InputStreamReader(new FileInputStream(file),
                    detectCharset(new FileInputStream(new File(file)))), ';');
            reader.readHeaders();
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        } catch (FileNotFoundException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
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
        CharsetDetector charsetDetector = new CharsetDetector();
        charsetDetector.setText(is instanceof BufferedInputStream ? is : new BufferedInputStream(is));
        charsetDetector.enableInputFilter(true);
        CharsetMatch cm = charsetDetector.detect();
        return cm.getName();
    }

    /************************************************** GETTERS / SETTERS ***************************************************/

    public CsvReader getReader() {
        return reader;
    }

    public void close() {
        reader.close();
    }
}