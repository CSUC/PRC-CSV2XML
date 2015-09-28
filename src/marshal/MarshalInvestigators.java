/**
 * 
 */
package marshal;

import global.RandomNumeric;
import global.UIDS;
import cerif.CfPersType;
import cerif.ObjectFactory;

/**
 * @author amartinez
 *
 */
public class MarshalInvestigators extends MarshalPRC{
	private CfPersType pers;
	private ObjectFactory factory;
	
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
	 * @param factory
	 * @param familyNames
	 * @param firstNames
	 * @param orcid
	 * @param signatureFamilyNames
	 * @param signatureFirstNames
	 * @param ae
	 * @param universityCode
	 */
	public MarshalInvestigators(ObjectFactory factory, String id,
            String familyNames, String firstNames, String orcid, String signatureFamilyNames,
            String signatureFirstNames, String ae, String type) {
		
		setAttrPers(factory, id, familyNames, firstNames, orcid, signatureFamilyNames,
	            signatureFirstNames, ae, type);		
        createResearcher();      
	}
	
	/**
	 * 
	 * @param factory
	 * @param familyNames
	 * @param firstNames
	 * @param orcid
	 * @param signatureFamilyNames
	 * @param signatureFirstNames
	 * @param ae
	 * @param universityCode
	 */
	private void setAttrPers(ObjectFactory factory, String id,
            String familyNames, String firstNames, String orcid, String signatureFamilyNames,
            String signatureFirstNames, String ae, String type){
		
		this.factory = factory;
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
		pers = factory.createCfPersType();
		if(_ID == null)	this._ID = RandomNumeric.getInstance().newId();
        pers.setCfPersId(_ID);        
		createEntityClass(typeClass, factory, pers);
		createPersNamePers(familyNames, firstNames, UIDS.CLASS_ID.getPRESENTED_NAME(), factory, pers);
		createPersNamePers(signatureFamilyNames, signatureFirstNames, UIDS.CLASS_ID.getSIGNATURE(), factory, pers);		
		createCode(orcid, UIDS.CLASS_ID.getORCID(), factory, pers);
		createEmail(ae, factory, pers);				
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
