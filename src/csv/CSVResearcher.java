/**
 * 
 */
package csv;

import global.UIDS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import marshal.MarshalCerif;
import marshal.MarshalInvestigators;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import cerif.CfPersType;
import cerif.ObjectFactory;

import com.csvreader.CsvReader;
import com.drew.lang.annotations.NotNull;

/**
 * @author amartinez
 *
 */
public class CSVResearcher implements Entity{	
	/**
	 * Llista que conté tots els Investigadors (cfPers)
	 */
	private List<CfPersType> listPersType = new ArrayList<CfPersType>();
	
	/**
	 * HasMap amb clau orcid i valor l'identificador que se li ha donat amb el Singleton (newId)
	 * 
	 * String[0]: orcid
	 * String[1]: id
	 * 
	 */
	private HashMap<String, String> mapResearcher = new HashMap<String, String>();
	
	/**	 
	 * Llista amb els investigadors única (clau signatura i valor l'identificador únic creat amb el 
	 * Singleton-> newid). Útil per crear Persons uncheckeds.
	 * 
	 * String[0]: signatura
	 * String[1]: id
	 * 
	 */
	private HashMap<String,String> UNCHECKEDS = new HashMap<String,String>();
	
	/**
	 * Classe predeterminada de l'entitat Person si no es diu el contrari (No ORCID).
	 */
	private String typeClassChecked = UIDS.CLASS_ID.getCHECKED();
	private String typeClassUnchecked = UIDS.CLASS_ID.getUNCHECKED();	
	private ObjectFactory FACTORY;
	private CsvReader reader;
	
	public CSVResearcher(@NotNull String path, MarshalCerif marshalCERIF) {
		if(new File(path).exists()){
			this.reader = new CSVReader(path).getReader();
			this.FACTORY = marshalCERIF.getFactory();	
		}else{
			Logger.getLogger(CSVResearcher.class.getName()).info("No existeix el fitxer " + FilenameUtils.getName(path));
		}
	}
		
	/**
	 * Crea tots els Autors 'uncheckeds' que ha trobat en totes les entitats. Aquests no estan repetits si la seva
	 * signatura és idèntica.
	 */
	public void addUncheckeds(){		
		for(Entry<String, String> entry : emptyIfNull(UNCHECKEDS.entrySet()))	
			listPersType.add(new MarshalInvestigators(
					FACTORY, entry.getValue(), null, null, null, entry.getKey(), null, null, typeClassUnchecked).getPers());		
	}
	

	@Override
	public void ReadCSV(@NotNull MarshalCerif marshal, CSVResearcher researcher) {
		try {
			while(reader.readRecord()){
				String name = StringEscapeUtils.escapeXml10(reader.get(0));
				String orcid = StringEscapeUtils.escapeXml10(reader.get(1));
				String signatura = StringEscapeUtils.escapeXml10(reader.get(2));
				String ae = StringEscapeUtils.escapeXml10(reader.get(3));
												
				MarshalInvestigators mRes 
						= new MarshalInvestigators(
								FACTORY, null, name, null, orcid, signatura, null, ae, typeClassChecked);
                        
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
	
	private <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
	    return iterable == null ? Collections.<T>emptyList() : iterable;
	}
	
	public List<CfPersType> getListPersType() {
		return listPersType;
	}

	public HashMap<String, String> getMapResearcher() {
		return mapResearcher;
	}

	public HashMap<String, String> getUNCHECKEDS() {
		return UNCHECKEDS;
	}

	public void setUNCHECKEDS(HashMap<String, String> uNCHECKEDS) {
		UNCHECKEDS = uNCHECKEDS;
	}
}