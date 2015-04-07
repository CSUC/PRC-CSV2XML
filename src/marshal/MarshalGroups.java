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
import cerif.CfOrgUnitType;
import cerif.CfOrgUnitType.CfOrgUnitSrv;
import cerif.CfTransType;
import cerif.ObjectFactory;

import com.csvreader.CsvReader;

import csv.CSVDepartment;
import csv.CSVReader;

/**
 * @author amartinez
 *
 */
public class MarshalGroups {
	private ObjectFactory FACTORY;	
	private CfOrgUnitType GROUP;

	private HashMap<String, String> mapResearcher;
	
	private String NAME;
	private String ACRO;
	private String URL;
	private String AE;
	private String CODE;
	private String SGR;
	private String CODE_UNIVESITY;
	private String DATE_CREATION;
	private String DOMAIN;
	
	private CsvReader reader;
	
	public MarshalGroups(ObjectFactory factory, String name, String acro, String url, String ae, String code,
			String sgr, String university, String date, String domain, String pathRelation, HashMap<String, String> map){
		
		this.reader = new CSVReader(pathRelation, Charsets.UTF_8).getReader();
		this.mapResearcher = map;	
		setAttrGroup(factory, name, acro, url, ae, code, sgr, university, date, domain);
		createGroup();
	}
	
	private void setAttrGroup(ObjectFactory factory, String name, String acro, String url, String ae, String code,
			String sgr, String university, String date, String domain){
		this.FACTORY = factory;
		this.NAME = name;
		this.ACRO = acro;
		this.URL = url;
		this.AE = ae;
		this.CODE = code;
		this.SGR = sgr;
		this.CODE_UNIVESITY = university;
		this.DATE_CREATION = date;
		this.DOMAIN = domain;		
	}

	private void createGroup(){
		GROUP = FACTORY.createCfOrgUnitType();
		GROUP.setCfOrgUnitId(RandomNumeric.getInstance().newId());
		createAcro();
		createUrl();
		createName();
		createDomain();
		createClass();
		createCode();
		createSGR();
		createUniversityCode();
		createDate();
		createAE();
		createRelationCfPers();
	}
	
	private void createAcro(){
		if(!StringUtils.isEmpty(ACRO))	GROUP.setCfAcro(ACRO);
	}
	
	
	private void createUrl(){
		if(!StringUtils.isEmpty(URL))	GROUP.setCfURI(URL);
	}
	
	private void createName(){
		if(!StringUtils.isEmpty(NAME)){
			CfMLangStringType name = new CfMLangStringType(); 
				name.setCfLangCode("ca");
				name.setCfTrans(CfTransType.O);
				name.setValue(NAME);
			
			GROUP.getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfName(name));
		}	
	}
	
	private void createDomain(){
		if(!StringUtils.isEmpty(DOMAIN)){
			CfMLangStringType key = new CfMLangStringType(); 
				key.setCfLangCode("ca");
				key.setCfTrans(CfTransType.O);
				key.setValue(DOMAIN);
			
			GROUP.getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfKeyw(key));
		}	
	}
	
	private void createClass(){
		CfCoreClassWithFractionType classGroup = new CfCoreClassWithFractionType();
			classGroup.setCfClassId(UIDS.CLASS_ID.getRESEARCH_GROUP());
			classGroup.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_TYPES());
			GROUP.getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitClass(classGroup));
	}
	
	private void createCode(){
		if(!StringUtils.isEmpty(CODE)){
			CfFedIdEmbType fedId = new CfFedIdEmbType();
			fedId.setCfFedId(CODE);
			
			CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
				fedIdClass.setCfClassId(UIDS.CLASS_ID.getRESEARCH_GROUP_INTERNAL_CODE());
				fedIdClass.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
			fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
			
			GROUP.getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfFedId(fedId));
		}		
	}
	
	private void createSGR(){
		if(!StringUtils.isEmpty(SGR)){
			CfFedIdEmbType fedId = new CfFedIdEmbType();
			fedId.setCfFedId(SGR);
			
			CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
				fedIdClass.setCfClassId(UIDS.CLASS_ID.getREGIONAL_RESEARCH_GROUP_CODE());
				fedIdClass.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
			fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
			
			GROUP.getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfFedId(fedId));
		}		
	}
	
	private void createUniversityCode(){
		if(!StringUtils.isEmpty(CODE_UNIVESITY)){
			CfOrgUnitType.CfOrgUnitOrgUnit code = new CfOrgUnitType.CfOrgUnitOrgUnit();
			code.setCfOrgUnitId2(CODE_UNIVESITY);
			code.setCfClassId(UIDS.CLASS_ID.getUNIVERSITY_CODE());
			code.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
			
			GROUP.getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitOrgUnit(code));
		}
	}
	
	
	private void createDate(){
		if(!StringUtils.isEmpty(DATE_CREATION)){
			CfOrgUnitSrv srv = new CfOrgUnitSrv();
				srv.setCfSrvId(RandomNumeric.getInstance().newId());
				srv.setCfClassId(UIDS.CLASS_ID.getRESEARCH_GROUP_CREATION_DATE());
				srv.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_RESEARCH_INFRASTRUCTURE_ROLES());				
				srv.setCfStartDate(Time.formatDateTime(DATE_CREATION,Time.getDATE_TIME()));
			GROUP.getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitSrv(srv));			
		}
	}
		
	private void createAE(){
		if(!StringUtils.isEmpty(AE)){
			CfOrgUnitType.CfOrgUnitEAddr ea = new CfOrgUnitType.CfOrgUnitEAddr();
				ea.setCfEAddrId(AE);
				ea.setCfClassId(UIDS.CLASS_ID.getEMAIL());
				ea.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_CONTACT_DETAILS());
			
			GROUP.getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitEAddr(ea));
		}
	}
	
	private void createRelationCfPers(){
		try {
			while(reader.readRecord()){
				if(CODE.equals(reader.get(0))){
					String orcid = reader.get(1);					
					if(mapResearcher.get(orcid) != null){
						CfOrgUnitType.CfPersOrgUnit persOrgUnit = new CfOrgUnitType.CfPersOrgUnit();
						persOrgUnit.setCfPersId(mapResearcher.get(orcid));
						if(reader.get(2).toLowerCase().equals("si")
								|| reader.get(2).toLowerCase().equals("s")){								
							persOrgUnit.setCfClassId(UIDS.CLASS_ID.getGROUP_LEADER());
							persOrgUnit.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_ORGANISATION_ROLES());								
						}else if(reader.get(2).toLowerCase().equals("no")
								|| reader.get(2).toLowerCase().equals("n")){
							persOrgUnit.setCfClassId(UIDS.CLASS_ID.getMEMBER());
							persOrgUnit.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_ORGANISATION_ROLES());	
						}							
						GROUP.getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfPersOrgUnit(persOrgUnit));
					}					
				}				
			}
		} catch (IOException e) {
			Logger.getLogger(CSVDepartment.class.getName()).info(e);
		}finally{
			reader.close();
		}
		
	}

	/************************************************* GETTERS / SETTERS ***********************************************************/
	
	public CfOrgUnitType getGROUP() {
		return GROUP;
	}
		
}
