/**
 * 
 */
package marshal;

import global.Time;
import global.Tuples.Pair;
import global.Tuples.Triplet;
import global.UIDS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cerif.CfCoreClassWithFractionType;
import cerif.CfFedIdEmbType;
import cerif.CfMLangStringType;
import cerif.CfResPublType;
import cerif.CfTransType;
import cerif.ObjectFactory;

/**
 * @author amartinez
 *
 */
public class MarshalPublications {
	private ObjectFactory FACTORY;
	private CfResPublType PUBLICATION;
	
	private HashMap<String, String> mapResearcher;
	
	private Triplet<List<String>,List<String>,List<String>> TRIPLET;
		
	private String TITLE;
	private String ID;
	private String DOI;
	private String HANDLE;
	private String DATE;
	private String PUBLICATA;
	private String PUBLICATPER;
	private String DOCUMENTTYPES;
	private String GROUPAUTHORS;
	
	
	public MarshalPublications(ObjectFactory factory, String title, String id, String doi, String handle, String date, String publicatA,
			String publicatPer, String documentTypes, String groupAuthors, HashMap<String, String> map, Triplet<List<String>,List<String>,List<String>> triplet){
		this.mapResearcher = map;
		this.TRIPLET = triplet;
		setAttrPubl(factory, title, id, doi, handle, date, publicatA, publicatPer, documentTypes, groupAuthors);
		createPublication();
	}
	
	private void setAttrPubl(ObjectFactory factory, String title, String id, String doi, String handle, String date, String publicatA,
			String publicatPer, String documentTypes, String groupAuthors){
		this.FACTORY = factory;
		this.TITLE = title;
		this.ID = id;
		this.DOI = doi;
		this.HANDLE = handle;
		this.DATE = date;
		this.PUBLICATA = publicatA;
		this.PUBLICATPER = publicatPer;
		this.DOCUMENTTYPES = documentTypes;
		this.GROUPAUTHORS = groupAuthors;		
	}
	
	private void createPublication(){
		PUBLICATION = FACTORY.createCfResPublType();
		if(!ID.isEmpty())	PUBLICATION.setCfResPublId(ID);
		createDate();
		createTitle();
		createClass();
		createDOI();
		createHANDLE();
		createOutPutType();
		createContributor();
		createGroupAuthors();
		createRelationCfPers();
	}
	
	private void createDate(){
		if(!StringUtils.isEmpty(DATE))
			PUBLICATION.setCfResPublDate(Time.formatDateTime(DATE,Time.getDATE()));
	}
	
	private void createTitle(){
		if(!StringUtils.isEmpty(TITLE)){
			CfMLangStringType title = new CfMLangStringType(); 
				title.setCfLangCode("ca");
				title.setCfTrans(CfTransType.O);
				title.setValue(TITLE);
			
			PUBLICATION.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfTitle(title));
		}
	}
	
	private void createClass(){
		Pair<List<String>, List<String>> pair = getPair();
		List<String> type = pair.getElement0();
		List<String> classID = pair.getElement1();
		
		if(!StringUtils.isEmpty(DOCUMENTTYPES)){
			for(int i=0;i<type.size();i++){	
				if(DOCUMENTTYPES.replaceAll(" ", "").toLowerCase().equals(type.get(i))){
					CfCoreClassWithFractionType classGroup = new CfCoreClassWithFractionType();
					classGroup.setCfClassId(classID.get(i));
					classGroup.setCfClassSchemeId(UIDS.SCHEME_ID.getOUTPUT_TYPES());
					PUBLICATION.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfResPublClass(classGroup));
				}
			}		
		}
	}
	
	private void createDOI(){
		if(!StringUtils.isEmpty(DOI)){
			CfFedIdEmbType fedId = new CfFedIdEmbType();
			fedId.setCfFedId(DOI);
			
			CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
				fedIdClass.setCfClassId(UIDS.CLASS_ID.getRESEARCH_GROUP_INTERNAL_CODE());
				fedIdClass.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
			fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
			
			PUBLICATION.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfFedId(fedId));
		}	
	}
	
	private void createHANDLE(){
		if(!StringUtils.isEmpty(HANDLE)){
			CfFedIdEmbType fedId = new CfFedIdEmbType();
			fedId.setCfFedId(HANDLE);
			
			CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
				fedIdClass.setCfClassId(UIDS.CLASS_ID.getRESEARCH_GROUP_INTERNAL_CODE());
				fedIdClass.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
			fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
			
			PUBLICATION.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfFedId(fedId));
		}	
	}
	
	private void createOutPutType(){
		if(!StringUtils.isEmpty(PUBLICATA)){
			CfResPublType.CfResPublResPubl outPut = new CfResPublType.CfResPublResPubl();
				outPut.setCfResPublId2(PUBLICATA);
				outPut.setCfClassId(UIDS.CLASS_ID.getBOOK());
				outPut.setCfClassSchemeId(UIDS.SCHEME_ID.getOUTPUT_TYPES());
				
			PUBLICATION.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfResPublResPubl(outPut));
		}
	}
	
	private void createContributor(){
		if(!StringUtils.isEmpty(PUBLICATPER)){
			CfResPublType.CfOrgUnitResPubl outPut = new CfResPublType.CfOrgUnitResPubl();
				outPut.setCfOrgUnitId(PUBLICATPER);
				outPut.setCfClassId(UIDS.CLASS_ID.getUNIVERSITY_PRESS());
				outPut.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_OUTPUT_CONTRIBUTIONS());
				
			PUBLICATION.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfOrgUnitResPubl(outPut));
		}
	}
	
	private void createGroupAuthors(){
		if(!StringUtils.isEmpty(GROUPAUTHORS)){
			CfResPublType.CfPersResPubl group = new CfResPublType.CfPersResPubl();
				group.setCfPersId(GROUPAUTHORS);
				group.setCfClassId(UIDS.CLASS_ID.getGROUP_AUTHORS());				
				group.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_OUTPUT_CONTRIBUTIONS());
				
			PUBLICATION.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfPersResPubl(group));
		}
	}
	
	
	/**
	 * 
	 */
	private void createRelationCfPers(){
		for(int i = 0; i< TRIPLET.getElement0().size(); i++){
			if(ID.equals(TRIPLET.getElement0().get(i))){
				if(mapResearcher.get(TRIPLET.getElement1().get(i)) != null){
					CfResPublType.CfPersResPubl persOrgUnit = new CfResPublType.CfPersResPubl();
					persOrgUnit.setCfPersId(mapResearcher.get(TRIPLET.getElement1().get(i)));
					if(TRIPLET.getElement2().get(i).toLowerCase().equals("si")
							|| TRIPLET.getElement2().get(i).toLowerCase().equals("s")){
						persOrgUnit.setCfClassId(UIDS.CLASS_ID.getDISS_DIRECTOR());
						persOrgUnit.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_PROFESSIONAL_RELATIONSHIPS());		
					}else if(TRIPLET.getElement2().get(i).toLowerCase().equals("no")
							|| TRIPLET.getElement2().get(i).toLowerCase().equals("n")){
						persOrgUnit.setCfClassId(UIDS.CLASS_ID.getAUTHOR());
						persOrgUnit.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_PROFESSIONAL_RELATIONSHIPS());	
					}
					PUBLICATION.getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfPersResPubl(persOrgUnit));		
				}
			}
		}	
	}
	
	
	/**
	 * Tupla amb les opcions Tipus de document a l'element0 i el UUIDS corresponent a l'element1
	 * 
	 * @return UUIDS classID.
	 */
	public Pair<List<String>, List<String>> getPair() {
		List<String> cerif = new ArrayList<String>();
			cerif.add("journalarticle");
			cerif.add("journal");
			cerif.add("chapterinbook");
			cerif.add("book");
			cerif.add("doctoralthesis");
		List<String> classid = new ArrayList<String>();
			classid.add(UIDS.CLASS_ID.getJOURNAL_ARTICLE());
			classid.add(UIDS.CLASS_ID.getJOURNAL());
			classid.add(UIDS.CLASS_ID.getCHAPTER_IN_BOOK());
			classid.add(UIDS.CLASS_ID.getBOOK());
			classid.add(UIDS.CLASS_ID.getPHD_THESIS());			
		Pair<List<String>, List<String>> tuple = Pair.createPair(cerif, classid);
		return tuple;
	}
	
	/************************************************** GETTERS / SETTERS ***************************************************/
	
	public CfResPublType getPUBLICATION() {
		return PUBLICATION;
	}	
}
