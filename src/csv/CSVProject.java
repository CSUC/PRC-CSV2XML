/**
 * 
 */
package csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.csvreader.CsvReader;

import marshal.MarshalCerif;
import marshal.MarshalProjects;
import cerif.CfProjType;

/**
 * @author amartinez
 *
 */
public class CSVProject {
	
	/**
	 * Llista que conté tots els Projectes (cfProj)
	 */
	private List<CfProjType> listProjType = new ArrayList<CfProjType>(); 
	
	/**
	 * HasMap amb clau orcid i valor l'identificador que se li ha donat amb el Singleton (newId)
	 */
	private HashMap<String, String> mapResearcher;
	
	/**
	 * 
	 * @param path Fitxer dels Projectes.
	 * @param marshalCERIF
	 * @param pathRelation Fitxer amb les relacions Projectes/Investigador
	 * @param map  HasMap amb clau Orcid i valor identificador únic.
	 */
	public CSVProject(String path, MarshalCerif marshalCERIF, String pathRelation, HashMap<String, String> map){
		CsvReader reader = new CSVReader(path, Charsets.UTF_8).getReader();
		this.mapResearcher = map;		
		try {
			while(reader.readRecord()){
				String titol = StringEscapeUtils.escapeXml10(reader.get(0));
				String url = StringEscapeUtils.escapeXml10(reader.get(1));
				String official_code = StringEscapeUtils.escapeXml10(reader.get(2));
				String code = StringEscapeUtils.escapeXml10(reader.get(3));
				String funding = StringEscapeUtils.escapeXml10(reader.get(4));
				String dateInici = StringEscapeUtils.escapeXml10(reader.get(5));
				String dateFi = StringEscapeUtils.escapeXml10(reader.get(6));
				
				CfProjType mProj = new MarshalProjects(marshalCERIF.getFactory(),
						titol, url, official_code, code, funding, dateInici, dateFi, pathRelation,
						mapResearcher).getPROJECT();
				listProjType.add(mProj);							
			}					
			Logger.getLogger(CSVProject.class.getName()).info("done");
		} catch (IOException e) {
			Logger.getLogger(CSVProject.class.getName()).info(e);
		}finally{
			reader.close();
		}
	}

	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public List<CfProjType> getListProjType() {
		return listProjType;
	}
	
	
}
