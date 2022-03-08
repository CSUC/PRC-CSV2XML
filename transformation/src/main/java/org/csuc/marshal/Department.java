package org.csuc.marshal;

import org.apache.spark.sql.Row;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class Department extends CfOrgUnitType implements Serializable {

    private ObjectFactory FACTORY = new ObjectFactory();
    private Row row;

    public Department(Row row, String classId) {
        this.row = row;

        setCfOrgUnitId(row.getAs("uuid") == null ? UUID.randomUUID().toString() : row.getAs("uuid"));

        createAcro();
        createUrl();
        createTitle();
        createEntityClass(classId);
        createCode();
        createEmail();
        createAddr();
        createPhone();

        createRelationCfPers();
    }

    private void createAcro() {
        if (Objects.nonNull(row.getAs(1))) setCfAcro(row.getAs(1));
    }

    private void createUrl() {
        if (Objects.nonNull(row.getAs(3))) setCfURI(row.getAs(3));
    }

    private void createTitle() {
        if (Objects.nonNull(row.getAs(0))) {
            NameOrTitle nameOrTitle = new NameOrTitle(row.getAs(0), null, null);

            CfMLangStringType title = new CfMLangStringType();
            title.setCfLangCode(nameOrTitle.getLangCode());
            title.setCfTrans(nameOrTitle.getTrans());
            title.setValue(nameOrTitle.getValue());

            getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfName(title));
        }
    }

    private void createEntityClass(String classId) {
        CfCoreClassWithFractionType cfCoreClass = new CfCoreClassWithFractionType();
        cfCoreClass.setCfClassId(classId);

        cfCoreClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_TYPES));
        getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitClass(cfCoreClass));
    }

    private void createCode() {
        if (Objects.nonNull(row.getAs(5))) {
            CfFedIdEmbType fedId = new CfFedIdEmbType();

            CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
            fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
            fedId.setCfFedId(row.getAs(5));
            fedIdClass.setCfClassId(Semantics.getClassId(ClassId.DEPARTMENT_OR_INSTITUTE_CODE));
            fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
            getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfFedId(fedId));
        }
    }

    private void createEmail() {
        if (Objects.nonNull(row.getAs(4))) {
            Stream.of(((String) row.getAs(4)).split("\\|\\|"))
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
        if (Objects.nonNull(row.getAs(2))) {
            CfOrgUnitType.CfOrgUnitPAddr ad = new CfOrgUnitType.CfOrgUnitPAddr();
            ad.setCfPAddrId(row.getAs(2));
            ad.setCfClassId(Semantics.getClassId(ClassId.ORGANISATION_LEGAL_POSTAL_ADDRESS));
            ad.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_CONTACT_DETAILS));
            getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitPAddr(ad));
        }
    }

    private void createPhone() {
        if (Objects.nonNull(row.getAs(6))) {
            CfOrgUnitType.CfOrgUnitEAddr ph = new CfOrgUnitType.CfOrgUnitEAddr();
            ph.setCfEAddrId(row.getAs(6));
            ph.setCfClassId(Semantics.getClassId(ClassId.PHONE));
            ph.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_CONTACT_DETAILS));
            getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitEAddr(ph));
        }
    }

    private void createRelationCfPers() {
        if (Objects.nonNull(row.getAs(8))) {
            List<Row> relations = row.getList(8);

            if(!relations.isEmpty()){
                relations.forEach(relation -> {
                    if(Objects.nonNull(relation.get(1))) researcher(relation.getString(1));
                });
            }
        }
    }

    private void researcher(String id) {
        CfOrgUnitType.CfPersOrgUnit persOrgUnit = new CfOrgUnitType.CfPersOrgUnit();
        persOrgUnit.setCfPersId(id);
        persOrgUnit.setCfClassId(Semantics.getClassId(ClassId.GROUP_LEADER));
        persOrgUnit.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_ORGANISATION_ROLES));

        getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfPersOrgUnit(persOrgUnit));
    }
}
