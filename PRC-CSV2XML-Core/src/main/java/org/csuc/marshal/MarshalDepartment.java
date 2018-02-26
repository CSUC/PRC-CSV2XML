package org.csuc.marshal;

import org.apache.commons.text.StringEscapeUtils;
import org.csuc.global.RandomNumeric;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author amartinez
 */
public class MarshalDepartment extends CfOrgUnitType implements Factory {

    private ObjectFactory FACTORY = new ObjectFactory();

    private String name;
    private String acro;
    private String addr;
    private String url;
    private String ae;
    private String dept;
    private String phone;

    private List<List<Object>> relation;
    private List<CfPersType> cfPersTypeList;

    public MarshalDepartment(String name, String acro, String addr, String url, String ae, String dept,
                             String phone, List relation, List<CfPersType> cfPersType) {

        this.name = StringEscapeUtils.escapeXml11(name);
        this.acro = StringEscapeUtils.escapeXml11(acro);
        this.addr = StringEscapeUtils.escapeXml11(addr);
        this.url = StringEscapeUtils.escapeXml11(url);
        this.ae = StringEscapeUtils.escapeXml11(ae);
        this.dept = StringEscapeUtils.escapeXml11(dept);
        this.phone = StringEscapeUtils.escapeXml11(phone);

        this.relation = relation;
        this.cfPersTypeList = cfPersType;

        execute();
    }

    private void createAcro() {
        if (Objects.nonNull(acro)) setCfAcro(acro);
    }

    private void createUrl() {
        if (Objects.nonNull(url)) setCfURI(url);
    }

    private void createTitle(String langCode, CfTransType transType) {
        if (Objects.nonNull(name)) {
            CfMLangStringType title = new CfMLangStringType();
            title.setCfLangCode(langCode);
            title.setCfTrans(transType);
            title.setValue(name);

            getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfName(title));
        }
    }

    private void createEntityClass(ClassId classId) {
        CfCoreClassWithFractionType cfCoreClass = new CfCoreClassWithFractionType();
        cfCoreClass.setCfClassId(Semantics.getClassId(classId));

        cfCoreClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_TYPES));
        getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitClass(cfCoreClass));
    }

    private void createCode() {
        if (Objects.nonNull(dept)) {
            CfFedIdEmbType fedId = new CfFedIdEmbType();

            CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
            fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
            fedId.setCfFedId(dept);
            fedIdClass.setCfClassId(Semantics.getClassId(ClassId.DEPARTMENT_OR_INSTITUTE_CODE));
            fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
            getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfFedId(fedId));
        }
    }

    private void createEmail() {
        if (Objects.nonNull(ae)) {
            Stream.of(ae.split("\\|\\|"))
                    .map(elem -> new String(elem))
                    .forEach(consumer -> {
                        CfOrgUnitType.CfOrgUnitEAddr email = new CfOrgUnitType.CfOrgUnitEAddr();
                        email.setCfEAddrId(consumer);
                        email.setCfClassId(Semantics.getClassId(ClassId.EMAIL));
                        email.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_CONTACT_DETAILS));
                        getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitEAddr(email));
                    });
        }
    }

    private void createAddr() {
        if (Objects.nonNull(addr)) {
            CfOrgUnitType.CfOrgUnitPAddr ad = new CfOrgUnitType.CfOrgUnitPAddr();
            ad.setCfPAddrId(addr);
            ad.setCfClassId(Semantics.getClassId(ClassId.ORGANISATION_LEGAL_POSTAL_ADDRESS));
            ad.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_CONTACT_DETAILS));
            getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitPAddr(ad));
        }
    }

    private void createPhone() {
        if (Objects.nonNull(phone)) {
            CfOrgUnitType.CfOrgUnitEAddr ph = new CfOrgUnitType.CfOrgUnitEAddr();
            ph.setCfEAddrId(phone);
            ph.setCfClassId(Semantics.getClassId(ClassId.PHONE));
            ph.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_CONTACT_DETAILS));
            getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitEAddr(ph));
        }
    }


    private void createRelationCfPers() {
        if (Objects.nonNull(relation)) {
            relation.stream().forEach(consumer -> {
                if (Objects.nonNull(cfPersTypeList) && dept.equals(consumer.get(0).toString())) {
                    CfPersType id = getIdentifier(cfPersTypeList, consumer.get(1).toString());
                    if(Objects.nonNull(id)){
                        CfOrgUnitType.CfPersOrgUnit persOrgUnit = new CfOrgUnitType.CfPersOrgUnit();
                        persOrgUnit.setCfPersId(id.getCfPersId());
                        persOrgUnit.setCfClassId(Semantics.getClassId(ClassId.GROUP_LEADER));
                        persOrgUnit.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_ORGANISATION_ROLES));

                        getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfPersOrgUnit(persOrgUnit));
                    }
                }
            });
        }
    }

    @Override
    public void execute() {
        setCfOrgUnitId(RandomNumeric.getInstance().newId());

        createAcro();
        createUrl();
        createTitle("ca", CfTransType.O);
        createEntityClass(ClassId.DEPARTMENT_OR_INSTITUTE);
        createCode();
        createEmail();
        createAddr();
        createPhone();

        createRelationCfPers();
    }

    private CfPersType getIdentifier(List<CfPersType> cfPersTypeList, String value) {
        return cfPersTypeList.stream().filter(cfPersType ->
            cfPersType.getCfResIntOrCfKeywOrCfPersPers()
                    .stream().filter(f -> f.getDeclaredType().equals(CfFedIdEmbType.class))
                    .map(m -> (CfFedIdEmbType) m.getValue())
                    .filter(f -> f.getCfFedId().equals(value)) != null).findFirst().orElse(null);
    }

    public CfOrgUnitType get() {
        return this;
    }
}
