/**
 * 
 */
package org.csuc.marshal;

import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.CERIF;
import xmlns.org.eurocris.cerif_1.CfOrgUnitType;
import xmlns.org.eurocris.cerif_1.CfTransType;
import xmlns.org.eurocris.cerif_1.ObjectFactory;
import org.csuc.csv.CSVResearcher;
import org.csuc.global.RandomNumeric;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.List;

/**
 * @author amartinez
 *
 */
public class MarshalDepartments extends MarshalPRC {
	private CfOrgUnitType department;

	private HashMap<String, String> mapResearcher;
	
	private String NAME;
	private String ACRO;
	private String ADDR;
	private String URL;
	private String AE;
	private String DEPT;
	private String PHONE;

	private Pair<List<String>, List<String>> PAIR;
	
	public MarshalDepartments(String name, String acro, String addr, String url,
                              String ae, String dept, String phone, Pair<List<String>, List<String>> pair, CSVResearcher researcher){
		this.mapResearcher = researcher.getMapResearcher();
		this.PAIR = pair;
				
		setAttrDepartment(name, acro, addr, url, ae, dept, phone);
		createDepartment();
	}
	
	private void setAttrDepartment(String name, String acro, String addr, String url,
			String ae, String dept, String phone){
		this.NAME = name;
		this.ACRO = acro;
		this.ADDR = addr;
		this.URL = url;
		this.AE = ae;
		this.DEPT = dept;
		this.PHONE = phone;		
	}
	
	private void createDepartment(){
		department = new ObjectFactory().createCfOrgUnitType();
		department.setCfOrgUnitId(RandomNumeric.getInstance().newId());
		
		createAcro(ACRO, department);
		createUrl(URL, department);
		createTitle(NAME, "ca", CfTransType.O, department);
		createEntityClass(Semantics.getClassId(ClassId.DEPARTMENT_OR_INSTITUTE), department);
		createCode(DEPT,Semantics.getClassId(ClassId.DEPARTMENT_OR_INSTITUTE_CODE), department);
		createEmail(AE, department);
		createAddr(ADDR, department);
		createPhone(PHONE, department);
		createRelationCfPers(department, PAIR, DEPT, mapResearcher, null);
	}
	
	public CfOrgUnitType getDepartment() {
		return department;
	}	
}
