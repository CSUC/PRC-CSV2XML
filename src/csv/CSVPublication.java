/**
 * 
 */
package csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import marshal.MarshalCerif;
import marshal.MarshalPublications;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import cerif.CfResPublType;

import com.csvreader.CsvReader;

import global.Tuples.Triplet;

/**
 * @author amartinez
 *
 */
public class CSVPublication {
	/**
	 * Llista que conté totes les publicacions (cfResPers)
	 */
	private List<CfResPublType> listPublType = new ArrayList<CfResPublType>(); 
	
	/**
	 * HasMap amb clau orcid i valor l'identificador que se li ha donat amb el Singleton (newId)
	 */
	private HashMap<String, String> mapResearcher;
	
	/**
	 * Tripleta de 3 elements on element0 és una llista d'IDS de les publicacions (poden estar repetides),
	 * element1 és una llista d'Orcids que intervenen a la publicació i l'element2 és una llista que conté
	 * si és director de la publicació.
	 */
	private Triplet<List<String>,List<String>,List<String>> triplet;
	private List<String> listId = new ArrayList<String>();
	private List<String> listOrcid = new ArrayList<String>();
	private List<String> listInterve = new ArrayList<String>();
	
	private CsvReader readerRelation;

	/**
	 * 
	 * 
	 * @param path Fitxer de publicacions.
	 * @param marshalCERIF
	 * @param pathRelation Fitxer amb les relacions.
	 * @param map HasMap amb clau Orcid i valor identificador únic.
	 */
	public CSVPublication(String path, MarshalCerif marshalCERIF, String pathRelation, HashMap<String, String> map){
		CsvReader reader = new CSVReader(path, Charsets.UTF_8).getReader();
		this.mapResearcher = map;
		createRelationPublicationResearcher(pathRelation);		
		try {
			while(reader.readRecord()){
				String titol = StringEscapeUtils.escapeXml10(reader.get(0));
				String id = StringEscapeUtils.escapeXml10(reader.get(1));
				String doi = StringEscapeUtils.escapeXml10(reader.get(2));
				String handle = StringEscapeUtils.escapeXml10(reader.get(3));
				String date = StringEscapeUtils.escapeXml10(reader.get(4));
				String publicatA = StringEscapeUtils.escapeXml10(reader.get(5));
				String publicatPer = StringEscapeUtils.escapeXml10(reader.get(6));
				String tipus = StringEscapeUtils.escapeXml10(reader.get(7));
				String grupAutors = StringEscapeUtils.escapeXml10(reader.get(8));
				
				CfResPublType mPubl = new MarshalPublications(marshalCERIF.getFactory(),
						titol , id, doi, handle, date, publicatA, publicatPer, tipus, grupAutors,
						mapResearcher, triplet).getPUBLICATION();	
				
				listPublType.add(mPubl);
			}					
			Logger.getLogger(CSVPublication.class.getName()).info("done");
		} catch (IOException e) {
			Logger.getLogger(CSVPublication.class.getName()).info(e);
		}finally{
			reader.close();
		}
	}
	
	
	/**
	 * Recorrem el fitxer de la relació entre les publicacions-Investigadors i cada registre del CSV
	 * el guardem a la llista corresponent per després posar-ho a la Tripleta.
	 * 
	 * @param pathRelation
	 */
	private void createRelationPublicationResearcher(String pathRelation){
		readerRelation= new CSVReader(pathRelation, Charsets.UTF_8).getReader();
		try {
			while(readerRelation.readRecord()){
				String id = StringEscapeUtils.escapeXml10(readerRelation.get(0));
				String orcid = StringEscapeUtils.escapeXml10(readerRelation.get(2));
				String interve = StringEscapeUtils.escapeXml10(readerRelation.get(3));
				listId.add(id);
				listOrcid.add(orcid);
				listInterve.add(interve);				
			}
			triplet = Triplet.createTriplet(listId, listOrcid, listInterve);
			
			Logger.getLogger(CSVPublication.class.getName()).info("done Relationship");			
		} catch (IOException e) {
			Logger.getLogger(CSVDepartment.class.getName()).info(e);
		}finally{
			readerRelation.close();
		}		
	}
	
	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public List<CfResPublType> getListPublType() {
		return listPublType;
	}
}
