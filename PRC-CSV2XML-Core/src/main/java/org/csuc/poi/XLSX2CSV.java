package org.csuc.poi;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.csuc.utils.SHEETS;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author amartinez
 */
public class XLSX2CSV {

    private static Logger logger = LogManager.getLogger(XLSX2CSV.class);

    private Workbook workbook;

    private char SEPARATOR;
    private String ENDOFLINESYMBOLS;
    private Pattern rxquote = Pattern.compile("\"");

    private Map<SHEETS, File> files = new HashMap<>();

    public XLSX2CSV(String file, char delimiter, String endOfLineSymbols) throws FileNotFoundException {
        InputStream is = new FileInputStream(file);
        workbook = StreamingReader.builder()
                .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);            // InputStream or File for XLSX file (required)
    }

    public XLSX2CSV(File file, char delimiter, String endOfLineSymbols) throws FileNotFoundException {
        InputStream is = new FileInputStream(file);
        SEPARATOR = delimiter;
        ENDOFLINESYMBOLS = endOfLineSymbols;
        workbook = StreamingReader.builder()
                .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);            // InputStream or File for XLSX file (required)
    }

    public XLSX2CSV(String file, int rowCacheSize, int bufferSize, char delimiter, String endOfLineSymbols) throws FileNotFoundException {
        InputStream is = new FileInputStream(file);
        SEPARATOR = delimiter;
        ENDOFLINESYMBOLS = endOfLineSymbols;
        workbook = StreamingReader.builder()
                .rowCacheSize(rowCacheSize)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(bufferSize)        // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);                     // InputStream or File for XLSX file (required)
    }

    public XLSX2CSV(File file, int rowCacheSize, int bufferSize, char delimiter, String endOfLineSymbols) throws FileNotFoundException {
        InputStream is = new FileInputStream(file);
        SEPARATOR = delimiter;
        ENDOFLINESYMBOLS = endOfLineSymbols;
        workbook = StreamingReader.builder()
                .rowCacheSize(rowCacheSize)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(bufferSize)        // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);                     // InputStream or File for XLSX file (required)
    }

    /**
     * @throws IOException
     */
    public void execute() throws IOException {
        workbook.forEach(sheet -> {
            try {
                if (SHEETS.convert(sheet.getSheetName()).equals(SHEETS.researchers))
                    save(sheet.getSheetName(), foreachCell(sheet, 4));
                if (SHEETS.convert(sheet.getSheetName()).equals(SHEETS.departments))
                    save(sheet.getSheetName(), foreachCell(sheet, 7));
                if (SHEETS.convert(sheet.getSheetName()).equals(SHEETS.departments_relations))
                    save(sheet.getSheetName(), foreachCell(sheet, 2));
                if (SHEETS.convert(sheet.getSheetName()).equals(SHEETS.research_groups))
                    save(sheet.getSheetName(), foreachCell(sheet, 7));
                if (SHEETS.convert(sheet.getSheetName()).equals(SHEETS.research_groups_relations))
                    save(sheet.getSheetName(), foreachCell(sheet, 4));
                if (SHEETS.convert(sheet.getSheetName()).equals(SHEETS.projects))
                    save(sheet.getSheetName(), foreachCell(sheet, 7));
                if (SHEETS.convert(sheet.getSheetName()).equals(SHEETS.projects_relations))
                    save(sheet.getSheetName(), foreachCell(sheet, 4));
                if (SHEETS.convert(sheet.getSheetName()).equals(SHEETS.publications))
                    save(sheet.getSheetName(), foreachCell(sheet, 15));
                if (SHEETS.convert(sheet.getSheetName()).equals(SHEETS.publication_relations))
                    save(sheet.getSheetName(), foreachCell(sheet, 4));
            } catch (Exception e) {
                logger.error(e);
            }
        });
        workbook.close();
    }

    /**
     *
     * @param sheet
     * @param max
     * @return
     */
    private String foreachCell(Sheet sheet, int max) {
        StringBuffer buffer = new StringBuffer();
        sheet.forEach(row -> {
            boolean firstCell = true;
            for (int rn = 0; rn < max; rn++) {
                if ( ! firstCell ) buffer.append(SEPARATOR);
                if (row.getCell(rn) == null || row.getCell(rn).getStringCellValue().isEmpty()) buffer.append("\"\"");
                else    buffer.append(encodeValue(row.getCell(rn).getStringCellValue(), SEPARATOR));
                firstCell = false;
            }
            buffer.append(ENDOFLINESYMBOLS);
        });
        return buffer.toString();
    }

    /**
     *
     * @param value
     * @return
     */
    private String encodeValue(String value, char delimiter){
        boolean needQuotes = false;
        if ( value.indexOf(',') != -1 || value.indexOf(';') != -1 ||
                value.indexOf('"') != -1 || value.indexOf('\n') != -1 ||
                value.indexOf('\r') != -1 || value.indexOf('\t') != -1 ||
                value.indexOf(delimiter) != -1)

            needQuotes = true;
        Matcher m = rxquote.matcher(value);
        if ( m.find() ) needQuotes = true; value = m.replaceAll("\"\"");
        if ( needQuotes ) return "\"" + value + "\"";
        else return value;
    }

    /**
     * @param filename
     * @param content
     */
    private void save(String filename, String content) {
        try {
            File temporal = File.createTempFile(filename, ".csv");
            Files.write(temporal.toPath(), content.getBytes());

            files.putIfAbsent(SHEETS.convert(filename), temporal);
        } catch (IOException e) {
            logger.error(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isLast(int i, int max) {
        return (i++ == max - 1);
    }

    public void deleteOnExit() {
        if (Objects.nonNull(files) && !files.isEmpty())
            files.entrySet().forEach(file -> file.getValue().deleteOnExit());
    }

    public Map<SHEETS, File> getFiles() {
        return files;
    }
}
