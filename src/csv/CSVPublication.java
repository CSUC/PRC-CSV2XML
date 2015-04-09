/**
 * 
 */
package csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import marshal.MarshalCerif;
import marshal.MarshalPublications;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.javatuples.Quartet;

import cerif.CfResPublType;

import com.csvreader.CsvReader;


/**
 * @author amartinez
 *
 */
public class CSVPublication {
	/**
	 * Llista que conté totes les publicacions (cfResPers)
	 */
	private List<CfResPublType> listPublType = new ArrayList<CfResPublType>(); 
	
	
	private Quartet<List<String>,List<String>,List<String>,List<String>> quartet = null;
	
	/**
	 * 
	 * 
	 * @param path Fitxer de publicacions.
	 * @param marshalCERIF
	 * @param pathRelation Fitxer amb les relacions.
	 * @param map HasMap amb clau Orcid i valor identificador únic.
	 */
	public CSVPublication(String path, MarshalCerif marshalCERIF, String pathRelation, CSVResearcher researcher){
		if(new File(pathRelation).exists())	quartet = createQuartet(pathRelation);
		if(new File(path).exists()){
			CsvReader reader = new CSVReader(path).getReader();				
			try {
				while(reader.readRecord()){					
					listPublType.add(new MarshalPublications(
										marshalCERIF.getFactory(),
										StringEscapeUtils.escapeXml10(reader.get(0)), 
										StringEscapeUtils.escapeXml10(reader.get(1)), 
										StringEscapeUtils.escapeXml10(reader.get(2)), 
										StringEscapeUtils.escapeXml10(reader.get(3)),
										StringEscapeUtils.escapeXml10(reader.get(4)), 
										StringEscapeUtils.escapeXml10(reader.get(5)), 
										StringEscapeUtils.escapeXml10(reader.get(6)), 
										StringEscapeUtils.escapeXml10(reader.get(7)), 
										StringEscapeUtils.escapeXml10(reader.get(8)), 
										StringEscapeUtils.escapeXml10(reader.get(9)),
										StringEscapeUtils.escapeXml10(reader.get(10)), 
										StringEscapeUtils.escapeXml10(reader.get(11)), 
										StringEscapeUtils.escapeXml10(reader.get(12)), 
										StringEscapeUtils.escapeXml10(reader.get(13)), 
										StringEscapeUtils.escapeXml10(reader.get(14)),
										researcher, quartet).getPUBLICATION());
				}
				Logger.getLogger(CSVPublication.class.getName()).info("done");
			} catch (IOException e) {
				Logger.getLogger(CSVPublication.class.getName()).info(e);
			}finally{
				reader.close();
			}
		}else{
			Logger.getLogger(CSVPublication.class.getName()).info("No existeix el fitxer " + FilenameUtils.getName(path));
		}
	}
	
	
	/**
	 * 
	 * @param pathRelation
	 * @return
	 */
	private Quartet<List<String>, List<String>, List<String>, List<String>> createQuartet(String pathRelation){
		CsvReader readerRelation= new CSVReader(pathRelation).getReader();
		List<String> listId = new ArrayList<String>();
		List<String> listSignature = new ArrayList<String>();
		List<String> listOrcid = new ArrayList<String>();
		List<String> listInterve = new ArrayList<String>();
		try {
			while(readerRelation.readRecord()){				
				listId.add(StringEscapeUtils.escapeXml10(readerRelation.get(0)));
				listSignature.add(StringEscapeUtils.escapeXml10(readerRelation.get(1)));
				listOrcid.add(StringEscapeUtils.escapeXml10(readerRelation.get(2)));
				listInterve.add(StringEscapeUtils.escapeXml10(readerRelation.get(3)));				
			}
			return new Quartet<List<String>, List<String>, List<String>, List<String>>(listId, listSignature, listOrcid, listInterve);
		} catch (IOException e) {
			Logger.getLogger(CSVDepartment.class.getName()).info(e);
			return null;
		}finally{
			readerRelation.close();
		}		
	}
	
	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public List<CfResPublType> getListPublType() {
		return listPublType;
	}
}
