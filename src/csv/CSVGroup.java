/**
 * 
 */
package csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.javatuples.Quartet;

import com.csvreader.CsvReader;

import marshal.MarshalCerif;
import marshal.MarshalGroups;
import cerif.CfOrgUnitType;

/**
 * @author amartinez
 *
 */
public class CSVGroup implements Entity {
	
	/**
	 * Llista que conté tots els Grups de recerca (cfOrgUnit)
	 */
	private List<CfOrgUnitType> listGrType = new ArrayList<CfOrgUnitType>(); 

	private Quartet<List<String>, List<String>, List<String>, List<String>> quartet = null;
	private CsvReader reader = null;
	
	/**
	 * 
	 * @param path Fitxer dels Grups de recerca.
	 * @param marshalCERIF
	 * @param pathRelation Fitxer amb les relacions Grup de recerca/Investigador
	 * @param map	HasMap amb clau Orcid i valor identificador únic.
	 */
	public CSVGroup(String path, MarshalCerif marshalCERIF, String pathRelation){		
		if(new File(pathRelation).exists())	quartet = createQuartet(pathRelation);
		if(new File(path).exists()){
			reader = new CSVReader(path).getReader();			
		}else{
			Logger.getLogger(CSVGroup.class.getName()).info("No existeix el fitxer " + FilenameUtils.getName(path));
		}		
	}

	@Override
	public void ReadCSV(MarshalCerif marshal, CSVResearcher researcher) {
		try {
			while(reader.readRecord()){
				listGrType.add(new MarshalGroups(
						marshal.getFactory(),
									StringEscapeUtils.escapeXml10(reader.get(0)), 
									StringEscapeUtils.escapeXml10(reader.get(1)), 
									StringEscapeUtils.escapeXml10(reader.get(2)), 
									StringEscapeUtils.escapeXml10(reader.get(3)), 
									StringEscapeUtils.escapeXml10(reader.get(4)), 
									StringEscapeUtils.escapeXml10(reader.get(5)), 
									StringEscapeUtils.escapeXml10(reader.get(6)), 
									StringEscapeUtils.escapeXml10(reader.get(7)), 
									quartet, 
									researcher).getGROUP());
			}					
			Logger.getLogger(CSVGroup.class.getName()).info("done");
		} catch (IOException e) {
			Logger.getLogger(CSVGroup.class.getName()).info(e);
		}finally{
			reader.close();
		}		
	}
	
	/**
	 * 
	 * @param pathRelation
	 * @return
	 */
	private Quartet<List<String>, List<String>, List<String>, List<String>> createQuartet(String pathRelation){
		CsvReader reader = new CSVReader(pathRelation).getReader();
		List<String> listGr = new ArrayList<String>();
		List<String> listSig = new ArrayList<String>();
		List<String> listOrcid = new ArrayList<String>();
		List<String> listIP = new ArrayList<String>();
		try {
			while(reader.readRecord()){
				listGr.add(StringEscapeUtils.escapeXml10(reader.get(0)));
				listSig.add(StringEscapeUtils.escapeXml10(reader.get(1)));
				listOrcid.add(StringEscapeUtils.escapeXml10(reader.get(2)));
				listIP.add(StringEscapeUtils.escapeXml10(reader.get(3)));					
			}
			return new Quartet<List<String>, List<String>, List<String>, List<String>>(listGr, listSig, listOrcid, listIP);
		}catch (IOException e) {
			return null;
		}finally{
			reader.close();			
		}
	}
	
	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public List<CfOrgUnitType> getListGrType() {
		return listGrType;
	}	
}