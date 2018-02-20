/**
 * 
 */
package org.csuc.marshal;

import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.CfProjType;
import xmlns.org.eurocris.cerif_1.CfTransType;
import xmlns.org.eurocris.cerif_1.ObjectFactory;
import org.csuc.csv.CSVResearcher;
import org.csuc.global.RandomNumeric;
import org.javatuples.Quartet;

import java.util.HashMap;
import java.util.List;

/**
 * @author amartinez
 *
 */
public class MarshalProjects extends MarshalPRC{
	private CfProjType PROJECT;
	
	private HashMap<String, String> mapResearcher;
	private HashMap<String, String> mapUncheckeds;
	
	private Quartet<List<String>, List<String>, List<String>, List<String>> QUARTET;
		
	private String TITLE;
	private String URI;
	private String OFFICIAL_CODE;
	private String CODE;
	private String PROGRAMME;
	private String DATE_INICI;
	private String DATE_FI;
	
	public MarshalProjects(String title, String uri, String officialCode,
			String code, String programme, String dateInici, String dateFi, Quartet<List<String>, List<String>, List<String>, List<String>> quartet, CSVResearcher researcher){
		
		this.QUARTET = quartet;
		this.mapResearcher = researcher.getMapResearcher();	
		this.mapUncheckeds = researcher.getUNCHECKEDS();
		
		setAttrProj(title, uri, officialCode, code, programme, dateInici, dateFi);
		createProject();
		
		researcher.setUNCHECKEDS(mapUncheckeds);
	}
	
	private void setAttrProj(String title, String uri, String officialCode,
			String code, String programme, String dateInici, String dateFi){
		this.TITLE = title;
		this.URI = uri;
		this.OFFICIAL_CODE = officialCode;
		this.CODE = code;
		this.PROGRAMME = programme;
		this.DATE_INICI = dateInici;
		this.DATE_FI = dateFi;
	}
	
	private void createProject(){
		PROJECT = new ObjectFactory().createCfProjType();
		PROJECT.setCfProjId(RandomNumeric.getInstance().newId());
		
		createDate(null, DATE_INICI, DATE_FI, PROJECT);
		createUrl(URI, PROJECT);
		createTitle(TITLE, "ca", CfTransType.O, PROJECT);
		createCode(CODE, Semantics.getClassId(ClassId.INTERNAL_PROJECT_CODE), PROJECT);
		createCode(OFFICIAL_CODE, Semantics.getClassId(ClassId.OFFICIAL_PROJECT_CODE), PROJECT);
		createFundingProgramme(PROGRAMME, PROJECT);
		createRelationCfPers(PROJECT, QUARTET, CODE, mapResearcher, mapUncheckeds);
	}

	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public CfProjType getPROJECT() {
		return PROJECT;
	} 
	
}
