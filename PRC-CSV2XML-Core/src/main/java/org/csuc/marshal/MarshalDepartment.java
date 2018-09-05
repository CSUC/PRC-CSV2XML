package org.csuc.marshal;

import org.csuc.global.RandomNumeric;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.*;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * @author amartinez
 */
public class MarshalDepartment extends CfOrgUnitType implements Factory {

    private ObjectFactory FACTORY = new ObjectFactory();

    private NameOrTitle name;
    private String acro;
    private String addr;
    private String url;
    private String ae;
    private String dept;
    private String phone;

    private List<List<Object>> relation;
    private List<CfPersType> cfPersTypeList;

    public MarshalDepartment(NameOrTitle name, String acro, String addr, String url, String ae, String dept,
                             String phone, List relation, List<CfPersType> cfPersType) {

        this.name = name;
        this.acro = acro;
        this.addr = addr;
        this.url = url;
        this.ae = ae;
        this.dept = dept;
        this.phone = phone;

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

    private void createTitle() {
        if (Objects.nonNull(name)) {
            CfMLangStringType title = new CfMLangStringType();
            title.setCfLangCode(name.getLangCode());
            title.setCfTrans(name.getTrans());
            title.setValue(name.getValue());

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
                    CfPersType id = getIdentifier(consumer.get(1).toString());
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
        createTitle();
        createEntityClass(ClassId.DEPARTMENT_OR_INSTITUTE);
        createCode();
        createEmail();
        createAddr();
        createPhone();

        createRelationCfPers();
    }

    private CfPersType getIdentifier(String value) {
        AtomicReference<CfPersType> result = new AtomicReference<>();
        cfPersTypeList.stream().forEach((CfPersType cfPersType) -> {
            cfPersType.getCfResIntOrCfKeywOrCfPersPers().forEach((JAXBElement<?> jaxbElement) -> {
                if(jaxbElement.getDeclaredType().equals(CfFedIdEmbType.class)
                        && ((CfFedIdEmbType) jaxbElement.getValue()).getCfFedId().equals(value))    result.set(cfPersType);
            });
        });
        return (Objects.isNull(result.get())) ? null : result.get();
    }

    public CfOrgUnitType get() {
        return this;
    }
}
