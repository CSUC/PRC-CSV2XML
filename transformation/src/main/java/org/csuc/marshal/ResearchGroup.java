package org.csuc.marshal;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.csuc.global.Time;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class ResearchGroup extends CfOrgUnitType {

    private ObjectFactory FACTORY = new ObjectFactory();
    private Row row;
    private List<CfPersType> cfPersTypeList;

    public ResearchGroup(Row row, String classId, List<CfPersType> cfPersTypeList) {
        this.row = row;
        this.cfPersTypeList = cfPersTypeList;

        setCfOrgUnitId(row.getAs("uuid") == null ? UUID.randomUUID().toString() : row.getAs("uuid"));

        createAcro();
        createUrl();
        createTitle();
        createEntityClass(classId);
        createCode(row.getAs(4), ClassId.RESEARCH_GROUP_INTERNAL_CODE);
        createCode(row.getAs(5), ClassId.REGIONAL_RESEARCH_GROUP_CODE);
        createEmail();
        createDate();

        createRelationCfPers();
    }


    private void createAcro() {
        if (Objects.nonNull(row.getAs(1))) setCfAcro(row.getAs(1));
    }

    private void createUrl() {
        if (Objects.nonNull(row.getAs(2))) setCfURI(row.getAs(2));
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

    private void createCode(String value, ClassId classId) {
        if (Objects.nonNull(value)) {
            CfFedIdEmbType fedId = new CfFedIdEmbType();

            CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
            fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
            fedId.setCfFedId(value);
            fedIdClass.setCfClassId(Semantics.getClassId(classId));
            fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
            getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfFedId(fedId));
        }
    }

    private void createEmail() {
        if (Objects.nonNull(row.getAs(3))) {
            Stream.of(((String) row.getAs(3)).split("\\|\\|"))
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

    private void createDate() {
        if (Objects.nonNull(row.getAs(6))) {
            CfOrgUnitSrv srv = new CfOrgUnitSrv();
            srv.setCfSrvId(UUID.randomUUID().toString());
            srv.setCfClassId(Semantics.getClassId(ClassId.RESEARCH_GROUP_CREATION_DATE));
            srv.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_RESEARCH_INFRASTRUCTURE_ROLES));
            try {
                srv.setCfStartDate(Time.formatDateTime(row.getAs(6)));
            } catch (Exception e) {
                //System.err.println(e);
            }
            getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfOrgUnitSrv(srv));
        }
    }

    private void createRelationCfPers() {
        if (Objects.nonNull(row.getAs(8))) {
            List<Row> relations = row.getList(8);

            relations.forEach(relation -> {
                if (Objects.nonNull(relation.getAs(3))) {
                    researcher(relation.getAs(3), relation.getAs(2));
                } else {
                    String random = UUID.randomUUID().toString();
                    researcher(random, relation.getAs(2));

                    cfPersTypeList.add(
                            new Researcher(
                                    RowFactory.create(
                                            Objects.isNull(relation.get(0)) ? null : relation.getString(0),
                                            Objects.isNull(relation.get(1)) ? null : relation.getString(1),
                                            null, null, random),
                                    Semantics.getClassId(ClassId.UNCHECKED)));
                }
            });
        }
    }

    private void researcher(String id, String interve) {
        CfOrgUnitType.CfPersOrgUnit persOrgUnit = new CfOrgUnitType.CfPersOrgUnit();
        persOrgUnit.setCfPersId(id);
        if (interve.toLowerCase().equals("si")
                || interve.toLowerCase().equals("s"))
            persOrgUnit.setCfClassId(Semantics.getClassId(ClassId.GROUP_LEADER));
        else if (interve.toLowerCase().equals("no")
                || interve.toLowerCase().equals("n")) persOrgUnit.setCfClassId(Semantics.getClassId(ClassId.MEMBER));

        persOrgUnit.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_ORGANISATION_ROLES));
        getCfNameOrCfResActOrCfKeyw().add(FACTORY.createCfOrgUnitTypeCfPersOrgUnit(persOrgUnit));
    }
}
