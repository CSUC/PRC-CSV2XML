/**
 * 
 */
package marshal;

import global.RandomNumeric;
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
import cerif.CfTransType;
import cerif.ObjectFactory;

import com.csvreader.CsvReader;

import csv.CSVDepartment;
import csv.CSVReader;

/**
 * @author amartinez
 *
 */
public class MarshalDepartments  {
	private ObjectFactory factory;	
	private CfOrgUnitType department;
	
	private HashMap<String, String> mapResearcher;
	
	private String NAME;
	private String ACRO;
	private String ADDR;
	private String URL;
	private String AE;
	private String DEPT;
	private String CODE_UNIVESITY;
	private String PHONE;
	
	private CsvReader reader;
	
	
	public MarshalDepartments(ObjectFactory factory, String name, String acro, String addr, String url,
			String ae, String dept, String code, String phone, String pathRelation, HashMap<String, String> map){
		
		this.reader = new CSVReader(pathRelation, Charsets.UTF_8).getReader();
		this.mapResearcher = map;		
		setAttrDepartment(factory, name, acro, addr, url, ae, dept, code, phone);
		createDepartment();
	}
	
	private void setAttrDepartment(ObjectFactory factory, String name, String acro, String addr, String url,
			String ae, String dept, String code, String phone){
		this.factory = factory;
		this.NAME = name;
		this.ACRO = acro;
		this.ADDR = addr;
		this.URL = url;
		this.AE = ae;
		this.DEPT = dept;
		this.CODE_UNIVESITY = code;
		this.PHONE = phone;		
	}
	
	private void createDepartment(){
		department = factory.createCfOrgUnitType();
		department.setCfOrgUnitId(RandomNumeric.getInstance().newId());		
		createAcro();
		createUrl();
		createName();
		createClass();
		createDeptCode();
		createUniversityCode();
		createAE();
		createAddr();
		createPhone();	
		createRelationCfPers();		
	}
	
	/**
	 * 
	 */
	private void createAcro(){
		if(!StringUtils.isEmpty(ACRO))	department.setCfAcro(ACRO);
	}
	
	/**
	 * 
	 */
	private void createUrl(){
		if(!StringUtils.isEmpty(URL)) department.setCfURI(URL);
	}
	
	/**
	 * 
	 */
	private void createName(){
		if(!StringUtils.isEmpty(NAME)){
			CfMLangStringType name = new CfMLangStringType(); 
				name.setCfLangCode("ca");
				name.setCfTrans(CfTransType.O);
				name.setValue(NAME);
			
				department.getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfName(name));
		}	
	}
	
	/**
	 * 
	 */
	private void createClass(){
		CfCoreClassWithFractionType classDepartment = new CfCoreClassWithFractionType();
			classDepartment.setCfClassId(UIDS.CLASS_ID.getDEPARTMENT_OR_INSTITUTE());
			classDepartment.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_TYPES());
			department.getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitClass(classDepartment));
	}
	
	/**
	 * 
	 */
	private void createDeptCode(){
		if(!StringUtils.isEmpty(DEPT)){
			CfFedIdEmbType fedId = new CfFedIdEmbType();
			fedId.setCfFedId(DEPT);
			
			CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
				fedIdClass.setCfClassId(UIDS.CLASS_ID.getDEPARTMENT_OR_INSTITUTE_CODE());
				fedIdClass.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
			fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
			
			department.getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfFedId(fedId));
		}
		
	}
	
	/**
	 * 
	 */
	private void createUniversityCode(){
		if(!StringUtils.isEmpty(CODE_UNIVESITY)){
			CfOrgUnitType.CfOrgUnitOrgUnit code = new CfOrgUnitType.CfOrgUnitOrgUnit();
			code.setCfOrgUnitId2(CODE_UNIVESITY);
			code.setCfClassId(UIDS.CLASS_ID.getUNIVERSITY_CODE());
			code.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
			
			department.getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitOrgUnit(code));
		}
	}
	
	/**
	 * 
	 */
	private void createAddr(){
		if(!StringUtils.isEmpty(ADDR)){
			CfOrgUnitType.CfOrgUnitPAddr addr = new CfOrgUnitType.CfOrgUnitPAddr();
				addr.setCfPAddrId(ADDR);
				addr.setCfClassId(UIDS.CLASS_ID.getORGANISATION_LEGAL_POSTAL_ADDRESS());
				addr.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_CONTACT_DETAILS());
				
				department.getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitPAddr(addr));
		}
	}
	
	/**
	 * 
	 */
	private void createAE(){
		if(!StringUtils.isEmpty(AE)){
			CfOrgUnitType.CfOrgUnitEAddr ea = new CfOrgUnitType.CfOrgUnitEAddr();
				ea.setCfEAddrId(AE);
				ea.setCfClassId(UIDS.CLASS_ID.getEMAIL());
				ea.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_CONTACT_DETAILS());
			
			department.getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitEAddr(ea));
		}
	}
	
	/**
	 * 
	 */
	private void createPhone(){
		if(!StringUtils.isEmpty(PHONE)){
			CfOrgUnitType.CfOrgUnitEAddr phone = new CfOrgUnitType.CfOrgUnitEAddr();
				phone.setCfEAddrId(PHONE);
				phone.setCfClassId(UIDS.CLASS_ID.getPHONE());
				phone.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_CONTACT_DETAILS());
			
			department.getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitEAddr(phone));
		}
	}
	
	/**
	 * 	
	 */
	private void createRelationCfPers(){
		try {
			while(reader.readRecord()){
				if(DEPT.equals(reader.get(0))){
					String orcid = reader.get(1);					
					if(mapResearcher.get(orcid) != null){
						CfOrgUnitType.CfPersOrgUnit persOrgUnit = new CfOrgUnitType.CfPersOrgUnit();
						persOrgUnit.setCfPersId(mapResearcher.get(orcid));
						persOrgUnit.setCfClassId(UIDS.CLASS_ID.getGROUP_LEADER());
						persOrgUnit.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_ORGANISATION_ROLES());					
						department.getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfPersOrgUnit(persOrgUnit));
					}
				}
			}
		} catch (IOException e) {
			Logger.getLogger(CSVDepartment.class.getName()).info(e);
		}finally{
			reader.close();
		}
	}

	public CfOrgUnitType getDepartment() {
		return department;
	}	
}
