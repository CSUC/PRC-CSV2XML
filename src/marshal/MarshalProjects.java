/**
 * 
 */
package marshal;

import global.RandomNumeric;
import global.Time;
import global.UIDS;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cerif.CfCoreClassWithFractionType;
import cerif.CfFedIdEmbType;
import cerif.CfMLangStringType;
import cerif.CfProjType;
import cerif.CfTransType;
import cerif.ObjectFactory;

import com.csvreader.CsvReader;

import csv.CSVDepartment;
import csv.CSVReader;

/**
 * @author amartinez
 *
 */
public class MarshalProjects {
	private ObjectFactory FACTORY;
	private CfProjType PROJECT;
	
	private HashMap<String, String> mapResearcher;
	private CsvReader reader;
	
	private String TITLE;
	private String URI;
	private String OFFICIAL_CODE;
	private String CODE;
	private String PROGRAMME;
	private String DATE_INICI;
	private String DATE_FI;
	
	public MarshalProjects(ObjectFactory factory, String title, String uri, String officialCode,
			String code, String programme, String dateInici, String dateFi, String pathRelation, HashMap<String, String> map){		
		this.reader = new CSVReader(pathRelation, Charsets.UTF_8).getReader();
		this.mapResearcher = map;	
		setAttrProj(factory, title, uri, officialCode, code, programme, dateInici, dateFi);
		createProject();
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
		createUrl();
		createTitle();
		createCode();
		createOfficialCode();
		createFundingProgramme();
		createRelationCfPers();		
	}
	
	private void createUrl(){
		if(!StringUtils.isEmpty(URI))	PROJECT.setCfURI(URI);
	}
	
	private void createTitle(){
		if(!StringUtils.isEmpty(TITLE)){
			CfMLangStringType title = new CfMLangStringType(); 
				title.setCfLangCode("ca");
				title.setCfTrans(CfTransType.O);
				title.setValue(TITLE);
			
			PROJECT.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfTitle(title));
		}
	}
	
	private void createCode(){
		if(!StringUtils.isEmpty(CODE)){
			CfFedIdEmbType fedId = new CfFedIdEmbType();
			fedId.setCfFedId(CODE);			
			CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
				fedIdClass.setCfClassId(UIDS.CLASS_ID.getINTERNAL_PROJECT_CODE());
				fedIdClass.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
			fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
			
			PROJECT.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfFedId(fedId));
		}	
	}
	
	private void createOfficialCode(){
		if(!StringUtils.isEmpty(OFFICIAL_CODE)){
			CfFedIdEmbType fedId = new CfFedIdEmbType();
			fedId.setCfFedId(OFFICIAL_CODE);			
			CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
				fedIdClass.setCfClassId(UIDS.CLASS_ID.getOFFICIAL_PROJECT_CODE());
				fedIdClass.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
			fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
			
			PROJECT.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfFedId(fedId));
		}	
	}
	
	private void createFundingProgramme(){
		if(!StringUtils.isEmpty(DATE_INICI)
				|| !StringUtils.isEmpty(DATE_FI)
				|| !StringUtils.isEmpty(PROGRAMME)){
			CfProjType.CfProjFund fund = new CfProjType.CfProjFund();
				fund.setCfFundId(PROGRAMME);
				fund.setCfClassId(UIDS.CLASS_ID.getFUNDING_PROGRAMME());
				fund.setCfClassSchemeId(UIDS.SCHEME_ID.getFUNDING_SOURCE_TYPES());
				fund.setCfStartDate(Time.formatDateTime(DATE_INICI,Time.getDATE_TIME()));
				fund.setCfEndDate(Time.formatDateTime(DATE_FI,Time.getDATE_TIME()));			
			PROJECT.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfProjFund(fund));				
		}
	}
	
	private void createRelationCfPers(){
		try {
			while(reader.readRecord()){
				if(CODE.equals(reader.get(0))){
					String orcid = reader.get(1);
					if(mapResearcher.get(orcid) != null){
						CfProjType.CfProjPers persOrgUnit = new CfProjType.CfProjPers();
						persOrgUnit.setCfPersId(mapResearcher.get(orcid));
					if(reader.get(2).toLowerCase().equals("si")
							|| reader.get(2).toLowerCase().equals("s")){								
						persOrgUnit.setCfClassId(UIDS.CLASS_ID.getPRINCIPAL_INVESTIGATOR());
						persOrgUnit.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_PROJECT_ENGAGEMENTS());								
					}else if(reader.get(2).toLowerCase().equals("no")
							|| reader.get(2).toLowerCase().equals("n")){
						persOrgUnit.setCfClassId(UIDS.CLASS_ID.getCO_INVESTIGATOR());
						persOrgUnit.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_PROJECT_ENGAGEMENTS());	
					}
					PROJECT.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfProjPers(persOrgUnit));
					}								
				}				
			}
		} catch (IOException e) {
			Logger.getLogger(CSVDepartment.class.getName()).info(e);
		}finally{
			reader.close();
		}		
	}
	
	public CfProjType getPROJECT() {
		return PROJECT;
	} 
	
}
