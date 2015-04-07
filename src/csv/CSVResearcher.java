/**
 * 
 */
package csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import marshal.MarshalCerif;
import marshal.MarshalInvestigators;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import cerif.CfPersType;

import com.csvreader.CsvReader;

/**
 * @author amartinez
 *
 */
public class CSVResearcher {
	
	/**
	 * Llista que conté tots els Investigadors (cfPers)
	 */
	private List<CfPersType> listPersType = new ArrayList<CfPersType>();
	
	/**
	 * HasMap amb clau orcid i valor l'identificador que se li ha donat amb el Singleton (newId)
	 */
	private HashMap<String, String> mapResearcher = new HashMap<String, String>();
			
	public CSVResearcher(String path, MarshalCerif marshalCERIF) {
		CsvReader reader = new CSVReader(path, Charsets.UTF_8).getReader();		
		try {
			while(reader.readRecord()){
				String name = StringEscapeUtils.escapeXml10(reader.get(0));
				String orcid = StringEscapeUtils.escapeXml10(reader.get(1));
				String signatura = StringEscapeUtils.escapeXml10(reader.get(2));
				String ae = StringEscapeUtils.escapeXml10(reader.get(3));
				String codiUniversitat = StringEscapeUtils.escapeXml10(reader.get(4));
									
				MarshalInvestigators mRes = new MarshalInvestigators(
                        marshalCERIF.getFactory(), name, null, orcid, signatura, null, ae, codiUniversitat);
                        
				listPersType.add(mRes.getPers());				
				if(!mapResearcher.containsKey(mRes.getOrcid()))	mapResearcher.put(mRes.getOrcid(), mRes.get_ID());						
			}			
			Logger.getLogger(CSVResearcher.class.getName()).info("done");
		} catch (IOException e) {
			Logger.getLogger(CSVResearcher.class.getName()).info(e);
		}finally{
			reader.close();
		}
		
	}
		
	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public List<CfPersType> getListPersType() {
		return listPersType;
	}

	public HashMap<String, String> getMapResearcher() {
		return mapResearcher;
	}
}