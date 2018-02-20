/**
 * 
 */
package org.csuc.marshal;

import org.csuc.global.RandomNumeric;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.CfPersType;
import xmlns.org.eurocris.cerif_1.ObjectFactory;

/**
 * @author amartinez
 *
 */
public class MarshalInvestigators extends MarshalPRC{
	private CfPersType pers;

	private String _ID;
		
	private String familyNames;
	private String firstNames;
	private String orcid;
	private String signatureFamilyNames;
	private String signatureFirstNames;
	private String ae;
	private String typeClass;
	
	/**
	 * 
	 * @param familyNames
	 * @param firstNames
	 * @param orcid
	 * @param signatureFamilyNames
	 * @param signatureFirstNames
	 * @param ae
	 */
	public MarshalInvestigators(String id,
            String familyNames, String firstNames, String orcid, String signatureFamilyNames,
            String signatureFirstNames, String ae, String type) {
		
		setAttrPers(id, familyNames, firstNames, orcid, signatureFamilyNames,
	            signatureFirstNames, ae, type);		
        createResearcher();      
	}
	
	/**
	 * 
	 * @param familyNames
	 * @param firstNames
	 * @param orcid
	 * @param signatureFamilyNames
	 * @param signatureFirstNames
	 * @param ae
	 */
	private void setAttrPers(String id,
            String familyNames, String firstNames, String orcid, String signatureFamilyNames,
            String signatureFirstNames, String ae, String type){
		
		this._ID = id;
		this.familyNames = familyNames;
		this.firstNames = firstNames;
		this.orcid = orcid;
		this.signatureFamilyNames = signatureFamilyNames;
		this.signatureFirstNames = signatureFirstNames;
		this.ae = ae;
		this.typeClass = type;
		
	}
	
	/**
	 * 
	 */
	private void createResearcher(){
		pers = new ObjectFactory().createCfPersType();
		if(_ID == null)	this._ID = RandomNumeric.getInstance().newId();
        pers.setCfPersId(_ID);        
		createEntityClass(typeClass, pers);
		createPersNamePers(familyNames, firstNames, Semantics.getClassId(ClassId.PRESENTED_NAME), pers);
		createPersNamePers(signatureFamilyNames, signatureFirstNames, Semantics.getClassId(ClassId.SIGNATURE), pers);
		createCode(orcid, Semantics.getClassId(ClassId.ORCID), pers);
		createEmail(ae, pers);
	}
	
	/**************************************************** GETTERS & SETTERS *****************************************************/
	
	public String get_ID() {
		return _ID;
	}

	public String getOrcid() {
		return orcid;
	}

	public CfPersType getPers() {
        return pers;
    }
	
}
