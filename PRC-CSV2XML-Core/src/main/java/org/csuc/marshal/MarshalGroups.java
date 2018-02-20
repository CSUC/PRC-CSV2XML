/**
 * 
 */
package org.csuc.marshal;

import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.CfOrgUnitType;
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
public class MarshalGroups extends MarshalPRC {
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

	public MarshalGroups(String name, String acro, String url, String ae, String code,
			String sgr, String date, String domain, Quartet<List<String>, List<String>, List<String>, List<String>> quartet, CSVResearcher researcher){
		
		this.QUARTET = quartet;
		this.mapResearcher = researcher.getMapResearcher();
		this.mapUncheckeds = researcher.getUNCHECKEDS();
		
		setAttrGroup(name, acro, url, ae, code, sgr, date, domain);
		createGroup();
		
		researcher.setUNCHECKEDS(mapUncheckeds);
	}
	
	private void setAttrGroup(String name, String acro, String url, String ae, String code,
			String sgr, String date, String domain){
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
		GROUP = new ObjectFactory().createCfOrgUnitType();
		GROUP.setCfOrgUnitId(RandomNumeric.getInstance().newId());
		createAcro(ACRO, GROUP);
		createUrl(URL, GROUP);
		createTitle(NAME, "ca", CfTransType.O, GROUP);
		createDomain(DOMAIN, GROUP);
		createEntityClass(Semantics.getClassId(ClassId.RESEARCH_GROUP), GROUP);
		createCode(CODE, Semantics.getClassId(ClassId.RESEARCH_GROUP_INTERNAL_CODE), GROUP);
		createCode(SGR, Semantics.getClassId(ClassId.REGIONAL_RESEARCH_GROUP_CODE), GROUP);
		createDate(DATE_CREATION, null, null, GROUP);
		createEmail(AE, GROUP);
		createRelationCfPers(GROUP, QUARTET, CODE, mapResearcher, mapUncheckeds);
	}

	/************************************************* GETTERS / SETTERS ***********************************************************/
	
	public CfOrgUnitType getGROUP() {
		return GROUP;
	}	
}
