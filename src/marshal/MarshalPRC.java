/**
 * 
 */
package marshal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.javatuples.Quartet;

import global.RandomNumeric;
import global.Time;
import global.UIDS;
import cerif.CfCoreClassWithFractionType;
import cerif.CfFedIdEmbType;
import cerif.CfMLangStringType;
import cerif.CfOrgUnitType;
import cerif.CfPersType;
import cerif.CfProjType;
import cerif.CfResPublType;
import cerif.CfTransType;
import cerif.ObjectFactory;
import cerif.CfOrgUnitType.CfOrgUnitSrv;
import cerif.CfPersType.CfPersEAddr;

/**
 * @author amartinez
 *
 */
public class MarshalPRC {
	
	/**
	 * Si la instancia pertany a CfPers es crea un Person Checked/Unckeded -> typeClass ho determina.
	 * Si la instancia pertany a CfOrgUnit es crea un OrgUNit depatment/group -> typeClass ho determina.
	 * 
	 * 
	 * @param typeClass UUID.
	 * @param factory 
	 * @param instance Tipus d'entitat.
	 */
	protected void createEntityClass(String typeClass, ObjectFactory factory, Object instance){
		CfCoreClassWithFractionType cfCoreClass = new CfCoreClassWithFractionType();
		cfCoreClass.setCfClassId(typeClass);
		if(instance.getClass().equals(CfPersType.class)){ //Pers
			cfCoreClass.setCfClassSchemeId(UIDS.SCHEME_ID.getVERIFICATION_STATUSES());
			((CfPersType)instance).getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersClass(cfCoreClass));
		}
		if(instance.getClass().equals(CfOrgUnitType.class)){ //OrgUnit
			cfCoreClass.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_TYPES());
			((CfOrgUnitType)instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitClass(cfCoreClass));
		}
		if(instance.getClass().equals(CfResPublType.class)){ //Publication
			if(typeClass != null){
				if(!typeClass.isEmpty()){						
					for(int i=0;i<getPair().getValue0().size();i++){						
						if(StringUtils.deleteWhitespace(typeClass).toLowerCase().equals(getPair().getValue0().get(i))){
							CfCoreClassWithFractionType classGroup = new CfCoreClassWithFractionType();
							classGroup.setCfClassId(getPair().getValue1().get(i));
							classGroup.setCfClassSchemeId(UIDS.SCHEME_ID.getOUTPUT_TYPES());
							((CfResPublType) instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfResPublClass(classGroup));
						}
					}		
				}
			}	
		}
	}
		
	/**
	 * 
	 * @param family
	 * @param first
	 * @param classid
	 * @param factory
	 * @param pers
	 */
	protected void createPersNamePers(String family, String first, String classid, ObjectFactory factory, CfPersType pers){
		if(classid.equals(UIDS.CLASS_ID.getPRESENTED_NAME())){ //Name
			if(family != null){
				if(!family.isEmpty()){
		        	CfPersType.CfPersNamePers persname = new CfPersType.CfPersNamePers();
			        persname.setCfPersNameId(RandomNumeric.getInstance().newId());
			        persname.setCfFamilyNames(family);
			        if(first != null)	persname.setCfFirstNames(first);
			        persname.setCfClassId(UIDS.CLASS_ID.getPRESENTED_NAME());
			        persname.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_NAMES());	
			        pers.getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersNamePers(persname));
				}
			}
		}
		if(classid.equals(UIDS.CLASS_ID.getSIGNATURE())){//Signature
			if(family != null){
				if(!family.isEmpty()){
			    	   CfPersType.CfPersNamePers sig = new CfPersType.CfPersNamePers();
					   sig.setCfPersNameId(RandomNumeric.getInstance().newId());
					   sig.setCfFamilyNames(family);
					   if(first != null)	sig.setCfFirstNames(first);
					   sig.setCfClassId(UIDS.CLASS_ID.getSIGNATURE());
					   sig.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_NAMES());
					   pers.getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersNamePers(sig));
					}
			}
		}
	}
	
	/**
	 * 
	 * Segons l'entitat es crea el tag del correu corresponent.
	 * 
	 * Si CfPersType -> CfPersEAddr
	 * Si CfOrgUnitType -> CfOrgUnitEAddr
	 * 
	 * @param ae
	 * @param factory
	 * @param instance
	 */
	protected void createEmail(String ae, ObjectFactory factory, Object instance){
		if(ae != null){
			if(!ae.isEmpty()){
				if(instance.getClass().equals(CfPersType.class)){ //Pers
					CfPersEAddr email = new CfPersEAddr();
					email.setCfEAddrId(ae);
					email.setCfClassId(UIDS.CLASS_ID.getEMAIL());
					email.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_CONTACT_DETAILS());					
					((CfPersType)instance).getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersEAddr(email));
				}
				if(instance.getClass().equals(CfOrgUnitType.class)){ //OrgUnit
					CfOrgUnitType.CfOrgUnitEAddr email = new CfOrgUnitType.CfOrgUnitEAddr();
					email.setCfEAddrId(ae);
					email.setCfClassId(UIDS.CLASS_ID.getEMAIL());
					email.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_CONTACT_DETAILS());				
					((CfOrgUnitType)instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitEAddr(email));
				}
			}
		}
	}
		
	/**
	 * Es crea ACRO segon l'entitat.
	 * 
	 * @param acro
	 * @param instance Tipus d'entitat.
	 */
	protected void createAcro(String acro, Object instance){
		if(acro != null){
			if(!acro.isEmpty()){
				if(instance.getClass().equals(CfOrgUnitType.class))	((CfOrgUnitType)instance).setCfAcro(acro);				
			}
		}		
	}
	
	/**
	 * Es crea URL segon l'entitat.
	 * 
	 * @param url
	 * @param instance Tipus d'entitat.
	 */
	protected void createUrl(String url, Object instance){
		if(url != null){
			if(!url.isEmpty()){
				if(instance.getClass().equals(CfOrgUnitType.class))	((CfOrgUnitType)instance).setCfURI(url);
				if(instance.getClass().equals(CfProjType.class))	((CfProjType)instance).setCfURI(url);
			}
		}		
	}
	
	/**
	 * 
	 * Es crea TITLE segon l'entitat (OrgUnit, Project o Publication).
	 * 
	 * @param title
	 * @param factory
	 * @param instance 
	 */
	protected void createTitle(String title, String langCode, CfTransType transType, ObjectFactory factory, Object instance){
		if(title != null){
			if(!title.isEmpty()){
				CfMLangStringType name = new CfMLangStringType();
					name.setCfLangCode(langCode);
					name.setCfTrans(transType);
					name.setValue(title);				
				if(instance.getClass().equals(CfOrgUnitType.class))	((CfOrgUnitType)instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfName(name));				
				if(instance.getClass().equals(CfProjType.class)) ((CfProjType)instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfProjTypeCfTitle(name));
				if(instance.getClass().equals(CfResPublType.class))	((CfResPublType)instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfTitle(name));
			}
		}			
	}
	
	/**
	 * 
	 * CfFedIdEmbType de l'entitat donat el valor i el tipus.
	 * 
	 * CfPersType -> value: orcid, type: uuids
	 * CfOrgUnitType -> value: internal code/sgr, type: uuids
	 * CfProjType -> value: internal code/official code, type: uuids
	 * CfResPublType -> value: doi/handle, type: uuids
	 * 
	 * @param value
	 * @param type
	 * @param factory
	 * @param instance
	 */
	protected void createCode(String value, String type, ObjectFactory factory, Object instance){
		if(value != null){
			if(!value.isEmpty()){
				CfFedIdEmbType fedId = new CfFedIdEmbType();
				fedId.setCfFedId(value);				
				CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();					
					fedIdClass.setCfClassSchemeId(UIDS.SCHEME_ID.getIDENTIFIER_TYPES());
				
				if(instance.getClass().equals(CfPersType.class)){ //Pers
					fedIdClass.setCfClassId(type);
					fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
					((CfPersType)instance).getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfFedId(fedId));
				}					
				if(instance.getClass().equals(CfOrgUnitType.class)){ //OrgUnit
					fedIdClass.setCfClassId(type);
					fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
					((CfOrgUnitType)instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfFedId(fedId));
				}
				if(instance.getClass().equals(CfProjType.class)){ //Project
					fedIdClass.setCfClassId(type);
					fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);				
					((CfProjType)instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfProjTypeCfFedId(fedId));
				}
				if(instance.getClass().equals(CfResPublType.class)){ //Publication
					fedIdClass.setCfClassId(type);
					fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);					
					((CfResPublType)instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfFedId(fedId));
				}				
			}
		}
	}
	
	/**
	 * Es crea EADDR segons l'entitat.
	 * 
	 * @param addr
	 * @param factory
	 * @param instance Tipus d'entitat.
	 */
	protected void createAddr(String addr, ObjectFactory factory, Object instance){
		if(addr != null){
			if(!addr.isEmpty()){
				if(instance.getClass().equals(CfOrgUnitType.class)){ //OrgUnit
					CfOrgUnitType.CfOrgUnitPAddr ad = new CfOrgUnitType.CfOrgUnitPAddr();
					ad.setCfPAddrId(addr);
					ad.setCfClassId(UIDS.CLASS_ID.getORGANISATION_LEGAL_POSTAL_ADDRESS());
					ad.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_CONTACT_DETAILS());
					((CfOrgUnitType)instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitPAddr(ad));
				}				
			}
		}		
	}
	
	/**
	 * Es crea PHONE segons l'entitat.
	 * 
	 * @param phone
	 * @param factory
	 * @param instance Tipus d'entitat.
	 */
	protected void createPhone(String phone, ObjectFactory factory, Object instance){
		if(phone != null){
			if(!phone.isEmpty()){
				if(instance.getClass().equals(CfOrgUnitType.class)){ //OrgUnit
					CfOrgUnitType.CfOrgUnitEAddr ph = new CfOrgUnitType.CfOrgUnitEAddr();
					ph.setCfEAddrId(phone);
					ph.setCfClassId(UIDS.CLASS_ID.getPHONE());
					ph.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_CONTACT_DETAILS());				
					((CfOrgUnitType)instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitEAddr(ph));
				}				
			}
		}	
	}
	
	/**
	 * Es crea l'ambit de recerca (Domain) segons l'entitat.
	 * 
	 * @param domain
	 * @param factory
	 * @param instance
	 */
	protected void createDomain(String domain, ObjectFactory factory, Object instance){
		if(domain != null){
			if(domain != null){
				if(!domain.isEmpty()){
					if(instance.getClass().equals(CfOrgUnitType.class)){ //OrgUnit
						CfMLangStringType key = new CfMLangStringType(); 
						key.setCfLangCode("ca");
						key.setCfTrans(CfTransType.O);
						key.setValue(domain);					
						((CfOrgUnitType)instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfKeyw(key));
					}				
				}
			}
		}		
	}
	
	/**
	 * 
	 * 
	 * @param date
	 * @param startDate
	 * @param endDate
	 * @param factory
	 * @param instance
	 */
	protected void createDate(String date, String startDate, String endDate, ObjectFactory factory, Object instance){
		if(date != null){
			if(!date.isEmpty()){
				if(instance.getClass().equals(CfOrgUnitType.class)){ //OrgUnit
					CfOrgUnitSrv srv = new CfOrgUnitSrv();
					srv.setCfSrvId(RandomNumeric.getInstance().newId());
					srv.setCfClassId(UIDS.CLASS_ID.getRESEARCH_GROUP_CREATION_DATE());
					srv.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_RESEARCH_INFRASTRUCTURE_ROLES());				
					srv.setCfStartDate(Time.formatDateTime(date,Time.getDATE_TIME()));							
					((CfOrgUnitType)instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitSrv(srv));		
				}
				if(instance.getClass().equals(CfResPublType.class))	((CfResPublType)instance).setCfResPublDate(Time.formatDateTime(date,Time.getDATE()));				
			}
		}
		if(startDate != null
				|| endDate != null){ //Project and Publicaton
			if(!startDate.isEmpty())	((CfProjType)instance).setCfStartDate(Time.formatDateTime(startDate,Time.getDATE()));
			if(!endDate.isEmpty())	((CfProjType)instance).setCfEndDate(Time.formatDateTime(endDate,Time.getDATE()));
		}		
	}
	
	/**
	 * 
	 * 
	 * @param programme
	 * @param factory
	 * @param project
	 */
	protected void createFundingProgramme(String programme, ObjectFactory factory, CfProjType project){
		if(programme != null){
			if(!programme.isEmpty()){ //Project
				CfProjType.CfProjFund fund = new CfProjType.CfProjFund();
					fund.setCfFundId(programme);
					fund.setCfClassId(UIDS.CLASS_ID.getFUNDING_PROGRAMME());
					fund.setCfClassSchemeId(UIDS.SCHEME_ID.getFUNDING_SOURCE_TYPES());					
					project.getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfProjTypeCfProjFund(fund));				
			}
		}		
	}
	
	/**
	 * Tupla amb les opcions Tipus de document a l'element0 i el UUIDS corresponent a l'element1
	 * 
	 * @return UUIDS classID.
	 */
	private Pair<List<String>, List<String>> getPair() {
		List<String> cerif = new ArrayList<String>();
			cerif.add("journalarticle");
			cerif.add("chapterinbook");
			cerif.add("book");
			cerif.add("doctoralthesis");
		List<String> classid = new ArrayList<String>();
			classid.add(UIDS.CLASS_ID.getJOURNAL_ARTICLE());
			classid.add(UIDS.CLASS_ID.getCHAPTER_IN_BOOK());
			classid.add(UIDS.CLASS_ID.getBOOK());
			classid.add(UIDS.CLASS_ID.getPHD_THESIS());
		return new Pair<List<String>, List<String>>(cerif, classid);
	}
	
	/**
	 * 
	 * @param publicatA
	 * @param factory
	 * @param publication
	 */
	protected void createOutPutType(String publicatA, ObjectFactory factory, CfResPublType publication){
		if(publicatA != null){
			if(!publicatA.isEmpty()){
				CfResPublType.CfResPublResPubl outPut = new CfResPublType.CfResPublResPubl();
					outPut.setCfResPublId2(publicatA);
					outPut.setCfClassId(UIDS.CLASS_ID.getBOOK());
					outPut.setCfClassSchemeId(UIDS.SCHEME_ID.getOUTPUT_TYPES());
				
				publication.getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfResPublResPubl(outPut));
			}
		}
	}
	
	/**
	 * 
	 * @param publicatPer
	 * @param factory
	 * @param publication
	 */
	protected void createContributor(String publicatPer, ObjectFactory factory, CfResPublType publication){
		if(publicatPer != null){
			if(!publicatPer.isEmpty()){
				CfResPublType.CfOrgUnitResPubl outPut = new CfResPublType.CfOrgUnitResPubl();
					outPut.setCfOrgUnitId(publicatPer);
					outPut.setCfClassId(UIDS.CLASS_ID.getUNIVERSITY_PRESS());
					outPut.setCfClassSchemeId(UIDS.SCHEME_ID.getORGANISATION_OUTPUT_CONTRIBUTIONS());
					
				publication.getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfOrgUnitResPubl(outPut));
			}
		}
	}
	
	/**
	 * 
	 * @param groupAthors
	 * @param factory
	 * @param publication
	 */
	protected void createGroupAuthors(String groupAthors, ObjectFactory factory, CfResPublType publication){
		if(groupAthors != null){
			if(!groupAthors.isEmpty()){
				CfResPublType.CfPersResPubl group = new CfResPublType.CfPersResPubl();
					group.setCfPersId(groupAthors);
					group.setCfClassId(UIDS.CLASS_ID.getGROUP_AUTHORS());				
					group.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_OUTPUT_CONTRIBUTIONS());
					
				publication.getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfPersResPubl(group));
			}
		}		
	}
	
	/**
	 * 
	 * @param num
	 * @param publication
	 */
	protected void createNum(String num, CfResPublType publication){
		if(num != null)	if(!num.isEmpty())	publication.setCfNum(num);
	}

	/**
	 * 
	 * @param vol
	 * @param publication
	 */
	protected void createVol(String vol, CfResPublType publication){
		if(vol != null)	if(!vol.isEmpty())	publication.setCfVol(vol);
	}
	
	/**
	 * 
	 * @param startPage
	 * @param publication
	 */
	protected void createStartPage(String startPage, CfResPublType publication){
		if(startPage != null)	if(!startPage.isEmpty())	publication.setCfStartPage(startPage);
	}
	
	/**
	 * 
	 * @param endPage
	 * @param publication
	 */
	protected void createEndPage(String endPage, CfResPublType publication){
		if(endPage != null)	if(!endPage.isEmpty())	publication.setCfEndPage(endPage);
	}
	
	/**
	 * 
	 * @param isbn
	 * @param publication
	 */
	protected void createISBN(String isbn, CfResPublType publication){
		if(isbn != null)	if(!isbn.isEmpty())	publication.setCfISBN(isbn);
	}
	
	/**
	 * 
	 * @param issn
	 * @param publication
	 */
	protected void createISSN(String issn, CfResPublType publication){
		if(issn != null)	if(!issn.isEmpty())	publication.setCfISSN(issn);
	}
	
	/**
	 * 
	 * Segons la instància (OrgUnit, Project o Publicaction) crea la relació entitat-Pers corresponent.
	 * 
	 * @param factory
	 * @param instance Entitat: OrgUnit, Project o Publicaction.
	 * @param javaTuple Tupla amb la informació que conté les relacions entitat-Pers (csv)
	 * @param field	Atribut clau per trobar la relació.
	 * @param researchers	Conté com a clau l'orcid i valor l'identificador únic associat.
	 * @param uncheckeds	Conté coma clau la signatura i valor l'identificador únic associat.
	 */
	@SuppressWarnings({ "unchecked" })
	protected void createRelationCfPers(ObjectFactory factory, Object instance, Object javaTuple, String field, HashMap<String, String> researchers, HashMap<String, String> uncheckeds){		
		if(javaTuple != null){
			//OrgUnit
			if(instance.getClass().equals(CfOrgUnitType.class)){			
				if(javaTuple.getClass().equals(Quartet.class)){//GRUP
					for(int i = 0; i < ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().size(); i++){
						if(field.equals(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().get(i))){
							if(researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)) != null){//Investigador
								addUserfromRole(factory, instance, researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
							}else if(!((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().get(i).isEmpty()
									&& ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i).isEmpty()){//Unchecked
								if(!uncheckeds.containsKey(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i))){//Introduir Signatura i crear Key
									String id = RandomNumeric.getInstance().newId();
									uncheckeds.put(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i), id);
									addUserfromRole(factory, instance, id, ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
								}else{//Signature ja existeix
									addUserfromRole(factory, instance, uncheckeds.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));							
								}
							}
						}
					}								
				}else if(javaTuple.getClass().equals(Pair.class)){//Departament
					for(int i = 0; i< ((Pair<List<String>, List<String>>) javaTuple).getValue0().size(); i++){
						if(field.equals(((Pair<List<String>, List<String>>) javaTuple).getValue0().get(i))){
							if(researchers.get(((Pair<List<String>, List<String>>) javaTuple).getValue1().get(i)) != null){//Es Investigador
								CfOrgUnitType.CfPersOrgUnit persOrgUnit = new CfOrgUnitType.CfPersOrgUnit();
								persOrgUnit.setCfPersId(researchers.get(((Pair<List<String>, List<String>>) javaTuple).getValue1().get(i)));
								persOrgUnit.setCfClassId(UIDS.CLASS_ID.getGROUP_LEADER());
								persOrgUnit.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_ORGANISATION_ROLES());
							
								((CfOrgUnitType)instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfPersOrgUnit(persOrgUnit));
							}
						}
					}
				}				
			}
			//Project
			if(instance.getClass().equals(CfProjType.class)){
				for(int i = 0; i < ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().size(); i++){
					if(field.equals(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().get(i))){
						if(researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)) != null){
							addUserfromRole(factory, instance, researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
						}else if(!((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i).isEmpty()
								&& ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i).isEmpty()){
							if(!uncheckeds.containsKey(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i))){//Introduir Signatura i crear Key
								String id = RandomNumeric.getInstance().newId();
								uncheckeds.put(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i), id);
								addUserfromRole(factory, instance, id, ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
							}else{//Signature ja existeix
								addUserfromRole(factory, instance, uncheckeds.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));							
							}
						}
					}
				}
			}
			//Publication
			if(instance.getClass().equals(CfResPublType.class)){
				//((CfResPublType)instance)
				if(javaTuple.getClass().equals(Quartet.class)){
					for(int i = 0; i < ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().size(); i++){
						if(field.equals(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().get(i))){
							if(researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)) != null){
								addUserfromRole(factory, instance, researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));		
							}else if(!((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i).isEmpty()
									&& ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i).isEmpty()){//UNCHECKEDS{
								if(!uncheckeds.containsKey(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i))){//Introduir Signatura i crear Key
									String id = RandomNumeric.getInstance().newId();
									uncheckeds.put(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i), id);
									addUserfromRole(factory, instance, id, ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));							
								}else{//Signature ja existeix
									addUserfromRole(factory, instance, uncheckeds.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));							
								}
							}
						}
					}
				}
			}
		}		
	}
	
	/**
	 * 
	 * S'afegeix el role segons la instància
	 * 
	 * @param factory
	 * @param instance
	 * @param id
	 * @param leaf
	 */
	protected void addUserfromRole(ObjectFactory factory, Object instance, String id, String leaf){
		//OrgUnit
		if(instance.getClass().equals(CfOrgUnitType.class)){
			CfOrgUnitType.CfPersOrgUnit persOrgUnit = new CfOrgUnitType.CfPersOrgUnit();						
			persOrgUnit.setCfPersId(id);
			if(leaf.toLowerCase().equals("si")
					|| leaf.toLowerCase().equals("s")){								
				persOrgUnit.setCfClassId(UIDS.CLASS_ID.getGROUP_LEADER());
				persOrgUnit.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_ORGANISATION_ROLES());								
			}else if(leaf.toLowerCase().equals("no")
					|| leaf.toLowerCase().equals("n")){
				persOrgUnit.setCfClassId(UIDS.CLASS_ID.getMEMBER());
				persOrgUnit.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_ORGANISATION_ROLES());	
			}
			((CfOrgUnitType)instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfPersOrgUnit(persOrgUnit));
		}
		//Project
		if(instance.getClass().equals(CfProjType.class)){
			CfProjType.CfProjPers pers = new CfProjType.CfProjPers();
			pers.setCfPersId(id);
			if(leaf.toLowerCase().equals("si")
				|| leaf.toLowerCase().equals("s")){
				pers.setCfClassId(UIDS.CLASS_ID.getPRINCIPAL_INVESTIGATOR());
				pers.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_PROJECT_ENGAGEMENTS());								
			}else if(leaf.toLowerCase().equals("no")
				|| leaf.toLowerCase().equals("n")){
				pers.setCfClassId(UIDS.CLASS_ID.getCO_INVESTIGATOR());
				pers.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_PROJECT_ENGAGEMENTS());	
			}
			((CfProjType) instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfProjTypeCfProjPers(pers));
		}		
		//Publication
		if(instance.getClass().equals(CfResPublType.class)){
			CfResPublType.CfPersResPubl pers = new CfResPublType.CfPersResPubl();
			pers.setCfPersId(id);
			if(leaf.toLowerCase().equals("si")
					|| leaf.toLowerCase().equals("s")){
				pers.setCfClassId(UIDS.CLASS_ID.getDISS_DIRECTOR());
				pers.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_PROFESSIONAL_RELATIONSHIPS());		
			}else if(leaf.toLowerCase().equals("no")
					|| leaf.toLowerCase().equals("n")){
				pers.setCfClassId(UIDS.CLASS_ID.getAUTHOR());
				pers.setCfClassSchemeId(UIDS.SCHEME_ID.getPERSON_PROFESSIONAL_RELATIONSHIPS());	
			}
			((CfResPublType) instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfPersResPubl(pers));
		}
	}
	
}
