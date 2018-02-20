/**
 *
 */
package org.csuc.marshal;

import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.*;
import xmlns.org.eurocris.cerif_1.CfOrgUnitType.CfOrgUnitSrv;
import xmlns.org.eurocris.cerif_1.CfPersType.CfPersEAddr;
import org.csuc.global.RandomNumeric;
import org.csuc.global.Time;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.javatuples.Quartet;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author amartinez
 */
public class MarshalPRC {

    /**
     * Si la instancia pertany a CfPers es crea un Person Checked/Unckeded -> typeClass ho determina.
     * Si la instancia pertany a CfOrgUnit es crea un OrgUNit depatment/group -> typeClass ho determina.
     *
     * @param typeClass UUID.
     * @param instance  Tipus d'entitat.
     */
    protected void createEntityClass(String typeClass, Object instance) {
        ObjectFactory factory = new ObjectFactory();

        CfCoreClassWithFractionType cfCoreClass = new CfCoreClassWithFractionType();
        cfCoreClass.setCfClassId(typeClass);
        if (instance.getClass().equals(CfPersType.class)) { //Pers
            cfCoreClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.VERIFICATION_STATUSES));
            ((CfPersType) instance).getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersClass(cfCoreClass));
        }
        if (instance.getClass().equals(CfOrgUnitType.class)) { //OrgUnit
            cfCoreClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_TYPES));
            ((CfOrgUnitType) instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitClass(cfCoreClass));
        }
        if (instance.getClass().equals(CfResPublType.class)) { //Publication
            if (typeClass != null) {
                if (!typeClass.isEmpty()) {
                    for (int i = 0; i < getPair().getValue0().size(); i++) {
                        if (StringUtils.deleteWhitespace(typeClass).toLowerCase().equals(getPair().getValue0().get(i))) {
                            CfCoreClassWithFractionType classGroup = new CfCoreClassWithFractionType();
                            classGroup.setCfClassId(getPair().getValue1().get(i));
                            classGroup.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.OUTPUT_TYPES));
                            ((CfResPublType) instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfResPublClass(classGroup));
                        }
                    }
                }
            }
        }
    }

    /**
     * @param family
     * @param first
     * @param classid
     * @param pers
     */
    protected void createPersNamePers(String family, String first, String classid, CfPersType pers) {
        ObjectFactory factory = new ObjectFactory();

        if (classid.equals(Semantics.getClassId(ClassId.PRESENTED_NAME))) { //Name
            if (family != null) {
                if (!family.isEmpty()) {
                    CfPersType.CfPersNamePers persname = new CfPersType.CfPersNamePers();
                    persname.setCfPersNameId(RandomNumeric.getInstance().newId());
                    persname.setCfFamilyNames(family);
                    if (first != null) persname.setCfFirstNames(first);
                    persname.setCfClassId(Semantics.getClassId(ClassId.PRESENTED_NAME));
                    persname.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_NAMES));
                    pers.getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersNamePers(persname));
                }
            }
        }
        if (classid.equals(Semantics.getClassId(ClassId.SIGNATURE))) {//Signature
            if (family != null) {
                if (!family.isEmpty()) {
                    CfPersType.CfPersNamePers sig = new CfPersType.CfPersNamePers();
                    sig.setCfPersNameId(RandomNumeric.getInstance().newId());
                    sig.setCfFamilyNames(family);
                    if (first != null) sig.setCfFirstNames(first);
                    sig.setCfClassId(Semantics.getClassId(ClassId.SIGNATURE));
                    sig.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_NAMES));
                    pers.getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersNamePers(sig));
                }
            }
        }
    }

    /**
     * Segons l'entitat es crea el tag del correu corresponent.
     * <p>
     * Si CfPersType -> CfPersEAddr
     * Si CfOrgUnitType -> CfOrgUnitEAddr
     *
     * @param ae
     * @param instance
     */
    protected void createEmail(String ae, Object instance) {
        ObjectFactory factory = new ObjectFactory();

        if (ae != null) {
            if (!ae.isEmpty()) {
                if (instance.getClass().equals(CfPersType.class)) { //Pers
                    Stream.of(ae.split("\\|\\|"))
                            .map(elem -> new String(elem))
                            .forEach(consumer -> {
                                CfPersEAddr email = new CfPersEAddr();
                                email.setCfEAddrId(consumer);
                                email.setCfClassId(Semantics.getClassId(ClassId.EMAIL));
                                email.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_CONTACT_DETAILS));
                                ((CfPersType) instance).getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfPersEAddr(email));
                            });
                }
                if (instance.getClass().equals(CfOrgUnitType.class)) { //OrgUnit
                    Stream.of(ae.split("\\|\\|"))
                            .map(elem -> new String(elem))
                            .forEach(consumer -> {
                                CfOrgUnitType.CfOrgUnitEAddr email = new CfOrgUnitType.CfOrgUnitEAddr();
                                email.setCfEAddrId(consumer);
                                email.setCfClassId(Semantics.getClassId(ClassId.EMAIL));
                                email.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_CONTACT_DETAILS));
                                ((CfOrgUnitType) instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitEAddr(email));
                            });
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
    protected void createAcro(String acro, Object instance) {
        if (acro != null) {
            if (!acro.isEmpty()) {
                if (instance.getClass().equals(CfOrgUnitType.class)) ((CfOrgUnitType) instance).setCfAcro(acro);
            }
        }
    }

    /**
     * Es crea URL segon l'entitat.
     *
     * @param url
     * @param instance Tipus d'entitat.
     */
    protected void createUrl(String url, Object instance) {
        if (url != null) {
            if (!url.isEmpty()) {
                if (instance.getClass().equals(CfOrgUnitType.class)) {
                    Stream.of(url.split("\\|\\|"))
                            .map(elem -> new String(elem))
                            .forEach(consumer -> {
                                ((CfOrgUnitType) instance).setCfURI(consumer);
                            });
                }
                if (instance.getClass().equals(CfProjType.class)) ((CfProjType) instance).setCfURI(url);
            }
        }
    }

    /**
     * Es crea TITLE segon l'entitat (OrgUnit, Project o Publication).
     *
     * @param title
     * @param instance
     */
    protected void createTitle(String title, String langCode, CfTransType transType, Object instance) {
        ObjectFactory factory = new ObjectFactory();
        if (title != null) {
            if (!title.isEmpty()) {
                CfMLangStringType name = new CfMLangStringType();
                name.setCfLangCode(langCode);
                name.setCfTrans(transType);
                name.setValue(title);
                if (instance.getClass().equals(CfOrgUnitType.class))
                    ((CfOrgUnitType) instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfName(name));
                if (instance.getClass().equals(CfProjType.class))
                    ((CfProjType) instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfProjTypeCfTitle(name));
                if (instance.getClass().equals(CfResPublType.class))
                    ((CfResPublType) instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfTitle(name));
            }
        }
    }

    /**
     * CfFedIdEmbType de l'entitat donat el valor i el tipus.
     * <p>
     * CfPersType -> value: orcid, type: uuids
     * CfOrgUnitType -> value: internal code/sgr, type: uuids
     * CfProjType -> value: internal code/official code, type: uuids
     * CfResPublType -> value: doi/handle, type: uuids
     *
     * @param value
     * @param type
     * @param instance
     */
    protected void createCode(String value, String type, Object instance) {
        ObjectFactory factory = new ObjectFactory();

        if (value != null) {
            if (!value.isEmpty()) {
                if (instance.getClass().equals(CfPersType.class)) { //Pers
                    CfFedIdEmbType fedId = new CfFedIdEmbType();

                    CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
                    fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
                    fedId.setCfFedId(value);
                    fedIdClass.setCfClassId(type);
                    fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
                    ((CfPersType) instance).getCfResIntOrCfKeywOrCfPersPers().add(factory.createCfPersTypeCfFedId(fedId));
                }
                if (instance.getClass().equals(CfOrgUnitType.class)) { //OrgUnit
                    CfFedIdEmbType fedId = new CfFedIdEmbType();

                    CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
                    fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
                    fedId.setCfFedId(value);
                    fedIdClass.setCfClassId(type);
                    fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
                    ((CfOrgUnitType) instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfFedId(fedId));
                }
                if (instance.getClass().equals(CfProjType.class)) { //Project
                    Stream.of(value.split("\\|\\|"))
                            .map(elem -> new String(elem))
                            .forEach(consumer -> {
                                CfFedIdEmbType fedId = new CfFedIdEmbType();

                                CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
                                fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
                                fedId.setCfFedId(consumer);
                                fedIdClass.setCfClassId(type);
                                fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
                                ((CfProjType) instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfProjTypeCfFedId(fedId));
                            });
                }
                if (instance.getClass().equals(CfResPublType.class)) { //Publication
                    Stream.of(value.split("\\|\\|"))
                            .map(elem -> new String(elem))
                            .forEach(consumer -> {
                                CfFedIdEmbType fedId = new CfFedIdEmbType();

                                CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
                                fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
                                fedId.setCfFedId(consumer);
                                fedIdClass.setCfClassId(type);
                                fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
                                ((CfResPublType) instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfFedId(fedId));
                            });
                }
            }
        }
    }

    /**
     * Es crea EADDR segons l'entitat.
     *
     * @param addr
     * @param instance Tipus d'entitat.
     */
    protected void createAddr(String addr, Object instance) {
        ObjectFactory factory = new ObjectFactory();

        if (addr != null) {
            if (!addr.isEmpty()) {
                if (instance.getClass().equals(CfOrgUnitType.class)) { //OrgUnit
                    CfOrgUnitType.CfOrgUnitPAddr ad = new CfOrgUnitType.CfOrgUnitPAddr();
                    ad.setCfPAddrId(addr);
                    ad.setCfClassId(Semantics.getClassId(ClassId.ORGANISATION_LEGAL_POSTAL_ADDRESS));
                    ad.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_CONTACT_DETAILS));
                    ((CfOrgUnitType) instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitPAddr(ad));
                }
            }
        }
    }

    /**
     * Es crea PHONE segons l'entitat.
     *
     * @param phone
     * @param instance Tipus d'entitat.
     */
    protected void createPhone(String phone, Object instance) {
        ObjectFactory factory = new ObjectFactory();

        if (phone != null) {
            if (!phone.isEmpty()) {
                if (instance.getClass().equals(CfOrgUnitType.class)) { //OrgUnit
                    CfOrgUnitType.CfOrgUnitEAddr ph = new CfOrgUnitType.CfOrgUnitEAddr();
                    ph.setCfEAddrId(phone);
                    ph.setCfClassId(Semantics.getClassId(ClassId.PHONE));
                    ph.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_CONTACT_DETAILS));
                    ((CfOrgUnitType) instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitEAddr(ph));
                }
            }
        }
    }

    /**
     * Es crea l'ambit de recerca (Domain) segons l'entitat.
     *
     * @param domain
     * @param instance
     */
    protected void createDomain(String domain, Object instance) {
        ObjectFactory factory = new ObjectFactory();

        if (domain != null) {
            if (domain != null) {
                if (!domain.isEmpty()) {
                    if (instance.getClass().equals(CfOrgUnitType.class)) { //OrgUnit
                        CfMLangStringType key = new CfMLangStringType();
                        key.setCfLangCode("ca");
                        key.setCfTrans(CfTransType.O);
                        key.setValue(domain);
                        ((CfOrgUnitType) instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfKeyw(key));
                    }
                }
            }
        }
    }

    /**
     * @param date
     * @param startDate
     * @param endDate
     * @param instance
     */
    protected void createDate(String date, String startDate, String endDate, Object instance) {
        ObjectFactory factory = new ObjectFactory();

        if (date != null) {
            if (!date.isEmpty()) {
                if (instance.getClass().equals(CfOrgUnitType.class)) { //OrgUnit

                    CfOrgUnitSrv srv = new CfOrgUnitSrv();
                    srv.setCfSrvId(RandomNumeric.getInstance().newId());
                    srv.setCfClassId(Semantics.getClassId(ClassId.RESEARCH_GROUP_CREATION_DATE));
                    srv.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_RESEARCH_INFRASTRUCTURE_ROLES));
                    srv.setCfStartDate(Time.formatDateTime(date));
                    ((CfOrgUnitType) instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfOrgUnitSrv(srv));
                }
                if (instance.getClass().equals(CfResPublType.class))
                    ((CfResPublType) instance).setCfResPublDate(Time.formatDate(date));
            }
        }
        if (startDate != null
                || endDate != null) { //Project and Publicaton
            if (!startDate.isEmpty())   ((CfProjType) instance).setCfStartDate(Time.formatDateTime(startDate));
            if (!endDate.isEmpty()) ((CfProjType) instance).setCfEndDate(Time.formatDateTime(endDate));
        }

    }

    /**
     * @param programme
     * @param project
     */
    protected void createFundingProgramme(String programme, CfProjType project) {
        ObjectFactory factory = new ObjectFactory();

        if (programme != null) {
            if (!programme.isEmpty()) { //Project
                CfProjType.CfProjFund fund = new CfProjType.CfProjFund();
                fund.setCfFundId(programme);
                fund.setCfClassId(Semantics.getClassId(ClassId.FUNDING_PROGRAMME));
                fund.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.FUNDING_SOURCE_TYPES));
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
        List<String> cerif = new ArrayList<>();
        cerif.add("journalarticle");
        cerif.add("chapterinbook");
        cerif.add("book");
        cerif.add("doctoralthesis");

        List<String> classid = new ArrayList<>();
        classid.add(Semantics.getClassId(ClassId.JOURNAL_ARTICLE));
        classid.add(Semantics.getClassId(ClassId.CHAPTER_IN_BOOK));
        classid.add(Semantics.getClassId(ClassId.BOOK));
        classid.add(Semantics.getClassId(ClassId.PHD_THESIS));
        return new Pair<>(cerif, classid);
    }

    /**
     * @param publicatA
     * @param publication
     */
    protected void createOutPutType(String publicatA, CfResPublType publication) {
        ObjectFactory factory = new ObjectFactory();

        if (publicatA != null) {
            if (!publicatA.isEmpty()) {
                CfResPublType.CfResPublResPubl outPut = new CfResPublType.CfResPublResPubl();
                outPut.setCfResPublId2(publicatA);
                outPut.setCfClassId(Semantics.getClassId(ClassId.BOOK));
                outPut.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.OUTPUT_TYPES));

                publication.getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfResPublResPubl(outPut));
            }
        }
    }

    /**
     * @param publicatPer
     * @param publication
     */
    protected void createContributor(String publicatPer, CfResPublType publication) {
        ObjectFactory factory = new ObjectFactory();

        if (publicatPer != null) {
            if (!publicatPer.isEmpty()) {
                CfResPublType.CfOrgUnitResPubl outPut = new CfResPublType.CfOrgUnitResPubl();
                outPut.setCfOrgUnitId(publicatPer);
                outPut.setCfClassId(Semantics.getClassId(ClassId.UNIVERSITY_PRESS));
                outPut.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_OUTPUT_CONTRIBUTIONS));
                publication.getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfOrgUnitResPubl(outPut));
            }
        }
    }

    /**
     * @param groupAthors
     * @param publication
     */
    protected void createGroupAuthors(String groupAthors, CfResPublType publication) {
        ObjectFactory factory = new ObjectFactory();

        if (groupAthors != null) {
            if (!groupAthors.isEmpty()) {
                CfResPublType.CfPersResPubl group = new CfResPublType.CfPersResPubl();
                group.setCfPersId(groupAthors);
                group.setCfClassId(Semantics.getClassId(ClassId.GROUP_AUTHORS));
                group.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_OUTPUT_CONTRIBUTIONS));

                publication.getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfPersResPubl(group));
            }
        }
    }

    /**
     * @param num
     * @param publication
     */
    protected void createNum(String num, CfResPublType publication) {
        if (num != null) if (!num.isEmpty()) publication.setCfNum(num);
    }

    /**
     * @param vol
     * @param publication
     */
    protected void createVol(String vol, CfResPublType publication) {
        if (vol != null) if (!vol.isEmpty()) publication.setCfVol(vol);
    }

    /**
     * @param startPage
     * @param publication
     */
    protected void createStartPage(String startPage, CfResPublType publication) {
        if (startPage != null) if (!startPage.isEmpty()) publication.setCfStartPage(startPage);
    }

    /**
     * @param endPage
     * @param publication
     */
    protected void createEndPage(String endPage, CfResPublType publication) {
        if (endPage != null) if (!endPage.isEmpty()) publication.setCfEndPage(endPage);
    }

    /**
     * @param isbn
     * @param publication
     */
    protected void createISBN(String isbn, CfResPublType publication) {
        if (isbn != null) if (!isbn.isEmpty()) publication.setCfISBN(isbn);
    }

    /**
     * @param issn
     * @param publication
     */
    protected void createISSN(String issn, CfResPublType publication) {
        if (issn != null) if (!issn.isEmpty()) publication.setCfISSN(issn);
    }

    /**
     * Segons la instància (OrgUnit, Project o Publicaction) crea la relació entitat-Pers corresponent.
     *
     * @param instance    Entitat: OrgUnit, Project o Publicaction.
     * @param javaTuple   Tupla amb la informació que conté les relacions entitat-Pers (csv)
     * @param field       Atribut clau per trobar la relació.
     * @param researchers Conté com a clau l'orcid i valor l'identificador únic associat.
     * @param uncheckeds  Conté coma clau la signatura i valor l'identificador únic associat.
     */
    @SuppressWarnings({"unchecked"})
    protected void createRelationCfPers(Object instance, Object javaTuple, String field, HashMap<String, String> researchers, HashMap<String, String> uncheckeds) {
        ObjectFactory factory = new ObjectFactory();

        if (javaTuple != null) {
            //OrgUnit
            if (instance.getClass().equals(CfOrgUnitType.class)) {
                if (javaTuple.getClass().equals(Quartet.class)) {//GRUP
                    for (int i = 0; i < ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().size(); i++) {
                        if (field.equals(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().get(i))) {
                            if (researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)) != null) {//Investigador
                                addUserfromRole(instance, researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
                            } else if (!((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().get(i).isEmpty()
                                    && ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i).isEmpty()) {//Unchecked
                                if (!uncheckeds.containsKey(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i))) {//Introduir Signatura i crear Key
                                    String id = RandomNumeric.getInstance().newId();
                                    uncheckeds.put(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i), id);
                                    addUserfromRole(instance, id, ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
                                } else {//Signature ja existeix
                                    addUserfromRole(instance, uncheckeds.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
                                }
                            }
                        }
                    }
                } else if (javaTuple.getClass().equals(Pair.class)) {//Departament
                    for (int i = 0; i < ((Pair<List<String>, List<String>>) javaTuple).getValue0().size(); i++) {
                        if (field.equals(((Pair<List<String>, List<String>>) javaTuple).getValue0().get(i))) {
                            if (researchers.get(((Pair<List<String>, List<String>>) javaTuple).getValue1().get(i)) != null) {//Es Investigador
                                CfOrgUnitType.CfPersOrgUnit persOrgUnit = new CfOrgUnitType.CfPersOrgUnit();
                                persOrgUnit.setCfPersId(researchers.get(((Pair<List<String>, List<String>>) javaTuple).getValue1().get(i)));
                                persOrgUnit.setCfClassId(Semantics.getClassId(ClassId.GROUP_LEADER));
                                persOrgUnit.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_ORGANISATION_ROLES));

                                ((CfOrgUnitType) instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfPersOrgUnit(persOrgUnit));
                            }
                        }
                    }
                }
            }
            //Project
            if (instance.getClass().equals(CfProjType.class)) {
                for (int i = 0; i < ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().size(); i++) {
                    if (field.equals(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().get(i))) {
                        if (researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)) != null) {
                            addUserfromRole(instance, researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
                        } else if (!((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i).isEmpty()
                                && ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i).isEmpty()) {
                            if (!uncheckeds.containsKey(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i))) {//Introduir Signatura i crear Key
                                String id = RandomNumeric.getInstance().newId();
                                uncheckeds.put(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i), id);
                                addUserfromRole(instance, id, ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
                            } else {//Signature ja existeix
                                addUserfromRole(instance, uncheckeds.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
                            }
                        }
                    }
                }
            }
            //Publication
            if (instance.getClass().equals(CfResPublType.class)) {
                //((CfResPublType)instance)
                if (javaTuple.getClass().equals(Quartet.class)) {
                    for (int i = 0; i < ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().size(); i++) {
                        if (field.equals(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue0().get(i))) {
                            if (researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)) != null) {
                                addUserfromRole(instance, researchers.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
                            } else if (!((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i).isEmpty()
                                    && ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue2().get(i).isEmpty()) {//UNCHECKEDS{
                                if (!uncheckeds.containsKey(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i))) {//Introduir Signatura i crear Key
                                    String id = RandomNumeric.getInstance().newId();
                                    uncheckeds.put(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i), id);
                                    addUserfromRole(instance, id, ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
                                } else {//Signature ja existeix
                                    addUserfromRole(instance, uncheckeds.get(((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue1().get(i)), ((Quartet<List<String>, List<String>, List<String>, List<String>>) javaTuple).getValue3().get(i));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * S'afegeix el role segons la instància
     *
     * @param instance
     * @param id
     * @param leaf
     */
    protected void addUserfromRole(Object instance, String id, String leaf) {
        ObjectFactory factory = new ObjectFactory();

        //OrgUnit
        if (instance.getClass().equals(CfOrgUnitType.class)) {
            CfOrgUnitType.CfPersOrgUnit persOrgUnit = new CfOrgUnitType.CfPersOrgUnit();
            persOrgUnit.setCfPersId(id);
            if (leaf.toLowerCase().equals("si")
                    || leaf.toLowerCase().equals("s")) {
                persOrgUnit.setCfClassId(Semantics.getClassId(ClassId.GROUP_LEADER));
                persOrgUnit.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_ORGANISATION_ROLES));
            } else if (leaf.toLowerCase().equals("no")
                    || leaf.toLowerCase().equals("n")) {
                persOrgUnit.setCfClassId(Semantics.getClassId(ClassId.MEMBER));
                persOrgUnit.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_ORGANISATION_ROLES));
            }
            ((CfOrgUnitType) instance).getCfNameOrCfResActOrCfKeyw().add(factory.createCfOrgUnitTypeCfPersOrgUnit(persOrgUnit));
        }
        //Project
        if (instance.getClass().equals(CfProjType.class)) {
            CfProjType.CfProjPers pers = new CfProjType.CfProjPers();
            pers.setCfPersId(id);
            if (leaf.toLowerCase().equals("si")
                    || leaf.toLowerCase().equals("s")) {
                pers.setCfClassId(Semantics.getClassId(ClassId.PRINCIPAL_INVESTIGATOR));
                pers.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_PROJECT_ENGAGEMENTS));
            } else if (leaf.toLowerCase().equals("no")
                    || leaf.toLowerCase().equals("n")) {
                pers.setCfClassId(Semantics.getClassId(ClassId.CO_INVESTIGATOR));
                pers.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_PROJECT_ENGAGEMENTS));
            }
            ((CfProjType) instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfProjTypeCfProjPers(pers));
        }
        //Publication
        if (instance.getClass().equals(CfResPublType.class)) {
            CfResPublType.CfPersResPubl pers = new CfResPublType.CfPersResPubl();
            pers.setCfPersId(id);
            if (leaf.toLowerCase().equals("si")
                    || leaf.toLowerCase().equals("s")) {
                pers.setCfClassId(Semantics.getClassId(ClassId.DISS_DIRECTOR));
                pers.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_PROFESSIONAL_RELATIONSHIPS));
            } else if (leaf.toLowerCase().equals("no")
                    || leaf.toLowerCase().equals("n")) {
                pers.setCfClassId(Semantics.getClassId(ClassId.AUTHOR));
                pers.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_PROFESSIONAL_RELATIONSHIPS));
            }
            ((CfResPublType) instance).getCfTitleOrCfAbstrOrCfKeyw().add(factory.createCfResPublTypeCfPersResPubl(pers));
        }
    }

}
