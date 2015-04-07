/**
 * 
 */
package csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import marshal.MarshalCerif;
import marshal.MarshalDepartments;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

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
	 * HasMap amb clau orcid i valor l'identificador que se li ha donat amb el Singleton (newId)
	 */
	private HashMap<String, String> mapResearcher;
	
	/**
	 * 
	 * @param path Fitxer dels Departaments.
	 * @param marshalCERIF
	 * @param pathRelation Fitxer amb les relacions Departament/Investigador
	 * @param map  HasMap amb clau Orcid i valor identificador únic.
	 */
	public CSVDepartment(String path, MarshalCerif marshalCERIF, String pathRelation, HashMap<String, String> map){
		CsvReader reader = new CSVReader(path, Charsets.UTF_8).getReader();
		this.mapResearcher = map;		
		try {
			while(reader.readRecord()){
				String nom = StringEscapeUtils.escapeXml10(reader.get(0));
				String sigles = StringEscapeUtils.escapeXml10(reader.get(1));
				String addr = StringEscapeUtils.escapeXml10(reader.get(2));
				String url = StringEscapeUtils.escapeXml10(reader.get(3));
				String ae = StringEscapeUtils.escapeXml10(reader.get(4));
				String cdp = StringEscapeUtils.escapeXml10(reader.get(5));
				String cu = StringEscapeUtils.escapeXml10(reader.get(6));
				String tel = StringEscapeUtils.escapeXml10(reader.get(7));
				
				CfOrgUnitType mDep = new MarshalDepartments(marshalCERIF.getFactory(),
						nom, sigles, addr, url, ae, cdp, cu, tel, pathRelation,
						mapResearcher).getDepartment();				
				listDepType.add(mDep);							
			}					
			Logger.getLogger(CSVDepartment.class.getName()).info("done");
		} catch (IOException e) {
			Logger.getLogger(CSVDepartment.class.getName()).info(e);
		}finally{
			reader.close();
		}
	}

	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public List<CfOrgUnitType> getListDepType() {
		return listDepType;
	}
}
