/**
 * 
 */
package marshal;

import global.RandomNumeric;
import global.UIDS;

import java.util.HashMap;
import java.util.List;

import org.javatuples.Quartet;

import cerif.CfProjType;
import cerif.CfTransType;
import cerif.ObjectFactory;
import csv.CSVResearcher;

/**
 * @author amartinez
 *
 */
public class MarshalProjects extends MarshalPRC{
	private ObjectFactory FACTORY;
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
	
	public MarshalProjects(ObjectFactory factory, String title, String uri, String officialCode,
			String code, String programme, String dateInici, String dateFi, Quartet<List<String>, List<String>, List<String>, List<String>> quartet, CSVResearcher researcher){
		
		this.QUARTET = quartet;
		this.mapResearcher = researcher.getMapResearcher();	
		this.mapUncheckeds = researcher.getUNCHECKEDS();
		
		setAttrProj(factory, title, uri, officialCode, code, programme, dateInici, dateFi);
		createProject();
		
		researcher.setUNCHECKEDS(mapUncheckeds);
	}
	
	private void setAttrProj(ObjectFactory factory, String title, String uri, String officialCode,
			String code, String programme, String dateInici, String dateFi){
		this.FACTORY = factory;
		this.TITLE = title;
		this.URI = uri;
		this.OFFICIAL_CODE = officialCode;
		this.CODE = code;
		this.PROGRAMME = programme;
		this.DATE_INICI = dateInici;
		this.DATE_FI = dateFi;
	}
	
	private void createProject(){
		PROJECT = FACTORY.createCfProjType();
		PROJECT.setCfProjId(RandomNumeric.getInstance().newId());
		
		createDate(null, DATE_INICI, DATE_FI, FACTORY, PROJECT);
		createUrl(URI, PROJECT);
		createTitle(TITLE, "ca", CfTransType.O, FACTORY, PROJECT);
		createCode(CODE,UIDS.CLASS_ID.getINTERNAL_PROJECT_CODE(), FACTORY, PROJECT);
		createCode(OFFICIAL_CODE, UIDS.CLASS_ID.getOFFICIAL_PROJECT_CODE(), FACTORY, PROJECT);
		createFundingProgramme(PROGRAMME, FACTORY, PROJECT);
		createRelationCfPers(FACTORY, PROJECT, QUARTET, CODE, mapResearcher, mapUncheckeds);
	}

	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public CfProjType getPROJECT() {
		return PROJECT;
	} 
	
}
