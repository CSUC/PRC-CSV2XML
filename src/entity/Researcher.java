/**
 * 
 */
package entity;

/**
 * @author amartinez
 *
 */
public class Researcher {
	private String FIRST_NAMES = null;
	private String FAMILY_NAMES = null;
	private String SIGNATURE_FIRST_NAMES = null;
	private String SIGNATURE_FAMILY_NAMES = null;
	private String ORCID = null;
	private String EMAIL = null;
	private String UNIVERSITY = null;
	
	public Researcher(String firstNames,
			String familyNames, String signatureFirstNames, String signatureFamilyNames,
			String orcid, String email, String university){
		setFIRST_NAMES(firstNames);
		setFAMILY_NAMES(familyNames);
		setSIGNATURE_FIRST_NAMES(signatureFirstNames);
		setSIGNATURE_FAMILY_NAMES(signatureFamilyNames);
		setORCID(orcid);
		setEMAIL(email);
		setUNIVERSITY(university);		
	}

	
	/*****************************************************************************************************************/

	public String getFIRST_NAMES() {
		return FIRST_NAMES;
	}

	public void setFIRST_NAMES(String fIRST_NAMES) {
		FIRST_NAMES = fIRST_NAMES;
	}

	public String getFAMILY_NAMES() {
		return FAMILY_NAMES;
	}

	public void setFAMILY_NAMES(String fAMILY_NAMES) {
		FAMILY_NAMES = fAMILY_NAMES;
	}

	public String getSIGNATURE_FIRST_NAMES() {
		return SIGNATURE_FIRST_NAMES;
	}

	public void setSIGNATURE_FIRST_NAMES(String sIGNATURE_FIRST_NAMES) {
		SIGNATURE_FIRST_NAMES = sIGNATURE_FIRST_NAMES;
	}

	public String getSIGNATURE_FAMILY_NAMES() {
		return SIGNATURE_FAMILY_NAMES;
	}

	public void setSIGNATURE_FAMILY_NAMES(String sIGNATURE_FAMILY_NAMES) {
		SIGNATURE_FAMILY_NAMES = sIGNATURE_FAMILY_NAMES;
	}

	public String getORCID() {
		return ORCID;
	}

	public void setORCID(String oRCID) {
		ORCID = oRCID;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}

	public String getUNIVERSITY() {
		return UNIVERSITY;
	}

	public void setUNIVERSITY(String uNIVERSITY) {
		UNIVERSITY = uNIVERSITY;
	}
}
