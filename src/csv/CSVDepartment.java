/**
 * 
 */
package csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import marshal.MarshalCerif;
import marshal.MarshalDepartments;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.javatuples.Pair;

import cerif.CfOrgUnitType;

import com.csvreader.CsvReader;

/**
 * @author amartinez
 *
 */
public class CSVDepartment {
	
	/**
	 * Llista que conté tots els departaments (cfOrgUnit)
	 */
	private List<CfOrgUnitType> listDepType = new ArrayList<CfOrgUnitType>(); 
	
	/**
	 * 
	 * @param path Fitxer dels Departaments.
	 * @param marshalCERIF
	 * @param pathRelation Fitxer amb les relacions Departament/Investigador
	 * @param map  HasMap amb clau Orcid i valor identificador únic.
	 */
	public CSVDepartment(String path, MarshalCerif marshalCERIF, String pathRelation, CSVResearcher researcher){
		Pair<List<String>, List<String>> pair = null;
		if(new File(pathRelation).exists())	pair = createTuple(pathRelation);
		if(new File(path).exists()){
			CsvReader reader = new CSVReader(path).getReader();
			try {
				while(reader.readRecord()){										
					listDepType.add(new MarshalDepartments(
										marshalCERIF.getFactory(),
										StringEscapeUtils.escapeXml10(reader.get(0)),
										StringEscapeUtils.escapeXml10(reader.get(1)), 
										StringEscapeUtils.escapeXml10(reader.get(2)), 
										StringEscapeUtils.escapeXml10(reader.get(3)), 
										StringEscapeUtils.escapeXml10(reader.get(4)), 
										StringEscapeUtils.escapeXml10(reader.get(5)), 
										StringEscapeUtils.escapeXml10(reader.get(6)), 
										pair, 
										researcher).getDepartment());							
				}					
				Logger.getLogger(CSVDepartment.class.getName()).info("done");
			} catch (IOException e) {
				Logger.getLogger(CSVDepartment.class.getName()).info(e);
			}finally{
				reader.close();
			}
		}else{
			Logger.getLogger(CSVDepartment.class.getName()).info("No existeix el fitxer " + FilenameUtils.getName(path));
		}
	}
	
	/**
	 * 
	 * @param pathRelation
	 * @return
	 */
	private Pair<List<String>, List<String>> createTuple(String pathRelation){
		CsvReader reader = new CSVReader(pathRelation).getReader();
		List<String> listDept = new ArrayList<String>();
		List<String> listOrcid = new ArrayList<String>();
		try {
			while(reader.readRecord()){				
				listDept.add(StringEscapeUtils.escapeXml10(reader.get(0)));
				listOrcid.add(StringEscapeUtils.escapeXml10(reader.get(1)));				
			}
			return new Pair<List<String>, List<String>>(listDept, listOrcid);
		}catch (IOException e) {
			return null;
		}finally{
			reader.close();			
		}
	}

	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public List<CfOrgUnitType> getListDepType() {
		return listDepType;
	}
}
