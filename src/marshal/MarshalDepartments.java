/**
 * 
 */
package marshal;

import global.RandomNumeric;
import global.UIDS;

import java.util.HashMap;
import java.util.List;

import org.javatuples.Pair;

import cerif.CfOrgUnitType;
import cerif.ObjectFactory;
import csv.CSVResearcher;

/**
 * @author amartinez
 *
 */
public class MarshalDepartments extends MarshalPRC {
	private ObjectFactory factory;	
	private CfOrgUnitType department;
	
	private HashMap<String, String> mapResearcher;
	
	private String NAME;
	private String ACRO;
	private String ADDR;
	private String URL;
	private String AE;
	private String DEPT;
	private String PHONE;
	
	private String typeClass = UIDS.CLASS_ID.getDEPARTMENT_OR_INSTITUTE();
	
	private Pair<List<String>, List<String>> PAIR;
	
	public MarshalDepartments(ObjectFactory factory, String name, String acro, String addr, String url,
			String ae, String dept, String phone, Pair<List<String>, List<String>> pair, CSVResearcher researcher){
		
		this.mapResearcher = researcher.getMapResearcher();
		this.PAIR = pair;
				
		setAttrDepartment(factory, name, acro, addr, url, ae, dept, phone);
		createDepartment();
	}
	
	private void setAttrDepartment(ObjectFactory factory, String name, String acro, String addr, String url,
			String ae, String dept, String phone){
		this.factory = factory;
		this.NAME = name;
		this.ACRO = acro;
		this.ADDR = addr;
		this.URL = url;
		this.AE = ae;
		this.DEPT = dept;
		this.PHONE = phone;		
	}
	
	private void createDepartment(){
		department = factory.createCfOrgUnitType();
		department.setCfOrgUnitId(RandomNumeric.getInstance().newId());
		
		createAcro(ACRO, department);
		createUrl(URL, department);
		createTitle(NAME, factory, department);
		createEntityClass(typeClass, factory, department);
		createCode(DEPT,UIDS.CLASS_ID.getDEPARTMENT_OR_INSTITUTE_CODE(), factory, department);
		createEmail(AE, factory, department);
		createAddr(ADDR, factory, department);
		createPhone(PHONE, factory, department);	
		createRelationCfPers(factory, department, PAIR, DEPT, mapResearcher, null);
	}
	
	public CfOrgUnitType getDepartment() {
		return department;
	}	
}
