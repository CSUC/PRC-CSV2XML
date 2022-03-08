package org.csuc.marshal;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.csuc.global.Time;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * +--------------------+--------------------+--------------+--------------------+-------------------+-------------------+-------+--------------------+
 * |               Títol|                 URL|  Codi oficial|         Codi intern|           Programa|         Data inici|Data fi|                uuid|
 * +--------------------+--------------------+--------------+--------------------+-------------------+-------------------+-------+--------------------+
 */
public class Project extends CfProjType implements Serializable {
    private ObjectFactory FACTORY = new ObjectFactory();

    private Row row;
    private List<CfPersType> cfPersTypeList;

    public Project(Row row, List<CfPersType> cfPersTypeList) {
        this.row = row;
        this.cfPersTypeList = cfPersTypeList;

        setCfProjId(row.getAs("uuid") == null ? UUID.randomUUID().toString() : row.getAs("uuid"));

        createTitle();
        createUrl();
        createDate();
        createCode(row.getAs(3), ClassId.INTERNAL_PROJECT_CODE);
        createCode(row.getAs(2), ClassId.OFFICIAL_PROJECT_CODE);
        createFundingProgramme();

        createRelationCfPers();
    }


    private void createTitle() {
        if (Objects.nonNull(row.getAs(0))) {
            NameOrTitle nameOrTitle = new NameOrTitle(row.getAs(0), null, null);

            CfMLangStringType name = new CfMLangStringType();
            name.setCfLangCode(nameOrTitle.getLangCode());
            name.setCfTrans(nameOrTitle.getTrans());
            name.setValue(nameOrTitle.getValue());

            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfTitle(name));
        }
    }

    private void createUrl() {
        if (Objects.nonNull(row.getAs(1))) setCfURI(row.getAs(1));
    }

    private void createDate() {
        if (Objects.nonNull(row.getAs(5))) {
            try {
                setCfStartDate(Time.formatDateTime(row.getAs(5)));
            } catch (Exception e) {
                //logger.warn(e);
            }
        }
        if (Objects.nonNull(row.getAs(6))) {
            try {
                setCfEndDate(Time.formatDateTime(row.getAs(6)));
            } catch (Exception e) {
                //logger.warn(e);
            }
        }
    }

    private void createCode(String value, ClassId classId) {
        if (Objects.nonNull(value)) {
            CfFedIdEmbType fedId = new CfFedIdEmbType();

            CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
            fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
            fedId.setCfFedId(value);
            fedIdClass.setCfClassId(Semantics.getClassId(classId));
            fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfFedId(fedId));
        }
    }

    private void createFundingProgramme() {
        if (Objects.nonNull(row.getAs(4))) {
            CfProjType.CfProjFund fund = new CfProjType.CfProjFund();
            fund.setCfFundId(row.getAs(4));
            fund.setCfClassId(Semantics.getClassId(ClassId.FUNDING_PROGRAMME));
            fund.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.FUNDING_SOURCE_TYPES));
            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfProjFund(fund));
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

    private void researcher(String id, String ip) {
        CfProjType.CfProjPers pers = new CfProjType.CfProjPers();
        pers.setCfPersId(id);

        if(Objects.nonNull(ip) && (ip.toLowerCase().equals("si")
                || ip.toLowerCase().equals("s")))  pers.setCfClassId(Semantics.getClassId(ClassId.PRINCIPAL_INVESTIGATOR));
        else pers.setCfClassId(Semantics.getClassId(ClassId.CO_INVESTIGATOR));

        pers.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_PROJECT_ENGAGEMENTS));
        getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfProjPers(pers));
    }


}
