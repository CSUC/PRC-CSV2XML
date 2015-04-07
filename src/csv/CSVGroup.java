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
import marshal.MarshalGroups;
import cerif.CfOrgUnitType;

/**
 * @author amartinez
 *
 */
public class CSVGroup {
	
	/**
	 * Llista que conté tots els Grups de recerca (cfOrgUnit)
	 */
	private List<CfOrgUnitType> listGrType = new ArrayList<CfOrgUnitType>(); 
	
	/**
	 * HasMap amb clau orcid i valor l'identificador que se li ha donat amb el Singleton (newId)
	 */
	private HashMap<String, String> mapResearcher;
	
	/**
	 * 
	 * @param path Fitxer dels Grups de recerca.
	 * @param marshalCERIF
	 * @param pathRelation Fitxer amb les relacions Grup de recerca/Investigador
	 * @param map	HasMap amb clau Orcid i valor identificador únic.
	 */
	public CSVGroup(String path, MarshalCerif marshalCERIF, String pathRelation, HashMap<String, String> map){
		CsvReader reader = new CSVReader(path, Charsets.UTF_8).getReader();
		this.mapResearcher = map;		
		try {
			while(reader.readRecord()){
				String nom = StringEscapeUtils.escapeXml10(reader.get(0));
				String sigles = StringEscapeUtils.escapeXml10(reader.get(1));
				String url = StringEscapeUtils.escapeXml10(reader.get(2));
				String ae = StringEscapeUtils.escapeXml10(reader.get(3));
				String code = StringEscapeUtils.escapeXml10(reader.get(4));
				String sgr = StringEscapeUtils.escapeXml10(reader.get(5));
				String cu = StringEscapeUtils.escapeXml10(reader.get(6));
				String date = StringEscapeUtils.escapeXml10(reader.get(7));
				String domain = StringEscapeUtils.escapeXml10(reader.get(8));

				CfOrgUnitType mGr = new MarshalGroups(marshalCERIF.getFactory(),
						nom, sigles, url, ae, code, sgr, cu, date, domain, pathRelation,
						mapResearcher).getGROUP();				
				listGrType.add(mGr);
			}					
			Logger.getLogger(CSVGroup.class.getName()).info("done");
		} catch (IOException e) {
			Logger.getLogger(CSVGroup.class.getName()).info(e);
		}finally{
			reader.close();
		}
	}

	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public List<CfOrgUnitType> getListGrType() {
		return listGrType;
	}
}