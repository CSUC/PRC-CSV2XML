/**
 * 
 */
package marshal;

import global.RandomNumeric;
import global.UIDS;
import cerif.CfCoreClassWithFractionType;
import cerif.CfFedIdEmbType;
import cerif.CfPersType;
import cerif.CfPersType.CfPersEAddr;
import cerif.ObjectFactory;

/**
 * @author amartinez
 *
 */
public class MarshalInvestigators {
	private CfPersType pers;
	private ObjectFactory factory;
	
	private String _ID;
		
	private String familyNames;
	private String firstNames;
	private String orcid;
	private String signatureFamilyNames;
	private String signatureFirstNames;
	private String ae;
	private String universityCode;
	
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
	public MarshalInvestigators(ObjectFactory factory,
            String familyNames, String firstNames, String orcid, String signatureFamilyNames,
            String signatureFirstNames, String ae, String universityCode) {
		
		setAttrPers(factory, familyNames, firstNames, orcid, signatureFamilyNames,
	            signatureFirstNames, ae, universityCode);		
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
	private void setAttrPers(ObjectFactory factory,
            String familyNames, String firstNames, String signatureFamilyNames,
            String signatureFirstNames, String orcid, String ae, String universityCode){
		
		this.factory = factory;
		this.familyNames = familyNames;
		this.firstNames = firstNames;		
		this.signatureFamilyNames = signatureFamilyNames;
		this.signatureFirstNames = signatureFirstNames;
		this.orcid = orcid;
		this.ae = ae;
		this.universityCode = universityCode;
		
	}
	
	/**
	 * 
	 */
	private void createResearcher(){
		pers = factory.createCfPersType();
		this._ID = RandomNumeric.getInstance().newId();
        pers.setCfPersId(_ID);
		createPersClass();
		createPersNamePers();
		createPersSignaturePers();
		createPersUniversityCode();
		createPersOrcid();
		createPersEmail();
	}
	
	/**
	 * 
	 */
	private void createPersClass(){
		CfCoreClassWithFractionType cfCoreClass = new CfCoreClassWithFractionType();
    	cfCoreClass.setCfClassId(UIDS.CLASS_ID.getCHECKED());
    	cfCoreClass.setCfClassSchemeId(UIDS.SCHEME_ID.getVERIFICATION_STATUSES());
        pers.getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersClass(cfCoreClass));
	}
	
	/**
	 * 
	 */
	private void createPersNamePers(){
		
		if(familyNames != null)
			if(!familyNames.isEmpty()){
				CfPersType.CfPersNamePers persname = new CfPersType.CfPersNamePers();
	        	persname.setCfPersNameId(RandomNumeric.getInstance().newId());
	            persname.setCfFamilyNames(familyNames);
	            persname.setCfFirstNames(firstNames);
	            persname.setCfClassId(UIDS.CLASS_ID.getPRESENTED_NAME());
	            persname.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_NAMES());	
	            pers.getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersNamePers(persname));
			}
	}
	
	/**
	 * 
	 */
	private void createPersSignaturePers(){
		
		
		if(signatureFamilyNames != null
				|| signatureFirstNames != null){
			if(!signatureFamilyNames.isEmpty()
				|| !signatureFirstNames.isEmpty() ){
				CfPersType.CfPersNamePers sig = new CfPersType.CfPersNamePers();
			       sig.setCfPersNameId(RandomNumeric.getInstance().newId());
			       sig.setCfFamilyNames(signatureFamilyNames);
			       sig.setCfFirstNames(signatureFirstNames);
			       sig.setCfClassId(UIDS.CLASS_ID.getSIGNATURE());
			       sig.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_NAMES());
			       pers.getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersNamePers(sig));
			}
		}
	}
	
	/**
	 * 
	 */
	private void createPersUniversityCode(){
		if(universityCode != null)
			if(!universityCode.isEmpty()){
				CfPersType.CfPersOrgUnit orgUnit = new CfPersType.CfPersOrgUnit();
				orgUnit.setCfOrgUnitId(universityCode);
				orgUnit.setCfClassId(UIDS.CLASS_ID.getUNIVERSITY_CODE());
				orgUnit.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
				pers.getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersOrgUnit(orgUnit));
			}		
	}
	
	/**
	 * 
	 */
	private void createPersOrcid(){
		if(orcid != null)
			if(!orcid.isEmpty()){
				CfFedIdEmbType fedId = new CfFedIdEmbType();
				fedId.setCfFedId(orcid);
				
				CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
					fedIdClass.setCfClassId(UIDS.CLASS_ID.getORCID());
					fedIdClass.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
				fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
				pers.getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfFedId(fedId));
			}
	}
	
	/**
	 * 
	 */
	private void createPersEmail(){	
		if(ae != null)
			if(!ae.isEmpty()){
				CfPersEAddr email = new CfPersEAddr();
					email.setCfEAddrId(ae);
					email.setCfClassId(UIDS.CLASS_ID.getEMAIL());
					email.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_CONTACT_DETAILS());
					pers.getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersEAddr(email));
			}
	}
	
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
