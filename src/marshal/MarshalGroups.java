/**
 * 
 */
package marshal;

import global.RandomNumeric;
import global.UIDS;

import java.util.HashMap;
import java.util.List;

import org.javatuples.Quartet;

import cerif.CfOrgUnitType;
import cerif.ObjectFactory;
import csv.CSVResearcher;

/**
 * @author amartinez
 *
 */
public class MarshalGroups extends MarshalPRC {
	private ObjectFactory FACTORY;	
	private CfOrgUnitType GROUP;

	private HashMap<String, String> mapResearcher;
	private HashMap<String, String> mapUncheckeds;
	
	private Quartet<List<String>, List<String>, List<String>, List<String>> QUARTET;
	
	private String NAME;
	private String ACRO;
	private String URL;
	private String AE;
	private String CODE;
	private String SGR;
	private String DATE_CREATION;
	private String DOMAIN;
	
	private String typeClass = UIDS.CLASS_ID.getRESEARCH_GROUP();
		
	public MarshalGroups(ObjectFactory factory, String name, String acro, String url, String ae, String code,
			String sgr, String date, String domain, Quartet<List<String>, List<String>, List<String>, List<String>> quartet, CSVResearcher researcher){
		
		this.QUARTET = quartet;
		this.mapResearcher = researcher.getMapResearcher();
		this.mapUncheckeds = researcher.getUNCHECKEDS();
		
		setAttrGroup(factory, name, acro, url, ae, code, sgr, date, domain);
		createGroup();
		
		researcher.setUNCHECKEDS(mapUncheckeds);
	}
	
	private void setAttrGroup(ObjectFactory factory, String name, String acro, String url, String ae, String code,
			String sgr, String date, String domain){
		this.FACTORY = factory;
		this.NAME = name;
		this.ACRO = acro;
		this.URL = url;
		this.AE = ae;
		this.CODE = code;
		this.SGR = sgr;
		this.DATE_CREATION = date;
		this.DOMAIN = domain;		
	}

	private void createGroup(){
		GROUP = FACTORY.createCfOrgUnitType();
		GROUP.setCfOrgUnitId(RandomNumeric.getInstance().newId());
		createAcro(ACRO, GROUP);
		createUrl(URL, GROUP);
		createTitle(NAME, FACTORY, GROUP);
		createDomain(DOMAIN, FACTORY, GROUP);
		createEntityClass(typeClass, FACTORY, GROUP);
		createCode(CODE, UIDS.CLASS_ID.getRESEARCH_GROUP_INTERNAL_CODE(), FACTORY, GROUP);
		createCode(SGR, UIDS.CLASS_ID.getREGIONAL_RESEARCH_GROUP_CODE(), FACTORY, GROUP);		
		createDate(DATE_CREATION, null, null, FACTORY, GROUP);
		createEmail(AE, FACTORY, GROUP);
		createRelationCfPers(FACTORY, GROUP, QUARTET, CODE, mapResearcher, mapUncheckeds);
	}
	

	/************************************************* GETTERS / SETTERS ***********************************************************/
	
	public CfOrgUnitType getGROUP() {
		return GROUP;
	}
		
}
