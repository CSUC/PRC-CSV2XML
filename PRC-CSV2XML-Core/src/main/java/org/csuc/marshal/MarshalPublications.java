/**
 * 
 */
package org.csuc.marshal;

import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.CfResPublType;
import xmlns.org.eurocris.cerif_1.CfTransType;
import xmlns.org.eurocris.cerif_1.ObjectFactory;
import org.csuc.csv.CSVResearcher;
import org.javatuples.Quartet;

import java.util.HashMap;
import java.util.List;

/**
 * @author amartinez
 *
 */
public class MarshalPublications extends MarshalPRC {
	private CfResPublType PUBLICATION;
	
	private HashMap<String, String> mapResearcher;
	private HashMap<String, String> mapUncheckeds;
	
	private Quartet<List<String>,List<String>,List<String>,List<String>> QUARTET;
		
	private String TITLE;
	private String ID;
	private String DOI;
	private String HANDLE;
	private String NUM;
	private String VOL;
	private String STARTPAGE;
	private String ENDPAGE;
	private String ISBN;
	private String ISSN;
	private String DATE;
	private String PUBLICATA;
	private String PUBLICATPER;
	private String DOCUMENTTYPES;
	private String GROUPAUTHORS;
	
	
	public MarshalPublications(String title, String id, String doi, String handle, String num, String vol,
			String startPage, String endPage, String isbn, String issn, String date, String publicatA,
			String publicatPer, String documentTypes, String groupAuthors, CSVResearcher researcher, 
			Quartet<List<String>,List<String>,List<String>,List<String>> quartet){
		
		this.mapResearcher = researcher.getMapResearcher();
		this.mapUncheckeds = researcher.getUNCHECKEDS();
		this.QUARTET = quartet;
		
		setAttrPubl(title, id, doi, handle, num, vol, startPage, endPage, isbn, issn,
							date, publicatA, publicatPer, documentTypes, groupAuthors);
		
		createPublication();
		researcher.setUNCHECKEDS(mapUncheckeds);
	}
	
	private void setAttrPubl(String title, String id, String doi, String handle, String num, String vol,
			String startPage, String endPage, String isbn, String issn, String date, String publicatA,
			String publicatPer, String documentTypes, String groupAuthors){
		
		this.TITLE = title;
		this.ID = id;
		this.DOI = doi;
		this.HANDLE = handle;
		this.NUM = num;
		this.VOL = vol;
		this.STARTPAGE = startPage;
		this.ENDPAGE = endPage;
		this.ISBN = isbn;
		this.ISSN = issn;
		this.DATE = date;
		this.PUBLICATA = publicatA;
		this.PUBLICATPER = publicatPer;
		this.DOCUMENTTYPES = documentTypes;
		this.GROUPAUTHORS = groupAuthors;		
	}
	
	private void createPublication(){
		PUBLICATION = new ObjectFactory().createCfResPublType();
		if(!ID.isEmpty())	PUBLICATION.setCfResPublId(ID);
		
		createNum(NUM, PUBLICATION);
		createVol(VOL, PUBLICATION);
		createStartPage(STARTPAGE, PUBLICATION);
		createEndPage(ENDPAGE, PUBLICATION);
		createISBN(ISBN, PUBLICATION);
		createISSN(ISSN, PUBLICATION);
		createDate(DATE, null, null, PUBLICATION);
		createTitle(TITLE, "ca", CfTransType.O, PUBLICATION);
		createEntityClass(DOCUMENTTYPES, PUBLICATION);
		//createPublicationType(DOCUMENTTYPES, FACTORY, PUBLICATION);
		createCode(DOI, Semantics.getClassId(ClassId.DOI), PUBLICATION);
		createCode(HANDLE, Semantics.getClassId(ClassId.HANDLE), PUBLICATION);
		createOutPutType(PUBLICATA, PUBLICATION);
		createContributor(PUBLICATPER, PUBLICATION);
		createGroupAuthors(GROUPAUTHORS, PUBLICATION);
		createRelationCfPers(PUBLICATION, QUARTET, ID, mapResearcher, mapUncheckeds);
	}
	
	
	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public CfResPublType getPUBLICATION() {
		return PUBLICATION;
	}	
}
