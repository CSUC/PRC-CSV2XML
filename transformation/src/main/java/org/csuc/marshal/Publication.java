package org.csuc.marshal;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.csuc.global.Time;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import org.csuc.utils.DocumentTypes;
import xmlns.org.eurocris.cerif_1.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class Publication extends CfResPublType {

    private ObjectFactory FACTORY = new ObjectFactory();
    private Row row;
    private List<CfPersType> cfPersTypeList;

    public Publication(Row row, List<CfPersType> cfPersTypeList) {
        this.row = row;
        this.cfPersTypeList = cfPersTypeList;

        setCfResPublId(row.getAs(1));

        createNum();
        createVol();
        createStartPage();
        createEndPage();
        createISBN();
        createISSN();
        createDate();
        createTitle();
        createEntityClass();
        createCode(row.getAs(2), ClassId.DOI);
        createCode(row.getAs(3), ClassId.HANDLE);
        createOutPutType();
        createGroupAuthors();
        createContributor();

        createRelationCfPers();
    }

    private void createNum() {
        if (Objects.nonNull(row.getAs(4))) setCfNum(row.getAs(4));
    }

    private void createVol() {
        if (Objects.nonNull(row.getAs(5))) setCfVol(row.getAs(5));
    }

    private void createStartPage() {
        if (Objects.nonNull(row.getAs(6))) setCfStartPage(row.getAs(6));
    }

    private void createEndPage() {
        if (Objects.nonNull(row.getAs(7))) setCfEndPage(row.getAs(7));
    }

    private void createISBN() {
        if (Objects.nonNull(row.getAs(8))) setCfISBN(row.getAs(8));
    }

    private void createISSN() {
        if (Objects.nonNull(row.getAs(9))) setCfISSN(row.getAs(9));
    }

    private void createDate() {
        if (Objects.nonNull(row.getAs(10))) {
            try {
                setCfResPublDate(Time.formatDate(row.getAs(10)));
            } catch (Exception e) {
                //System.err.println(e);
            }
        }
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

    private void createEntityClass() {
        if (Objects.nonNull(row.getAs(13))) {
            CfCoreClassWithFractionType classGroup = new CfCoreClassWithFractionType();
            DocumentTypes type = DocumentTypes.convert(row.getAs(13));
            classGroup.setCfClassId((Objects.nonNull(type)) ? Semantics.getClassId(ClassId.valueOf(type.toString())) : "");
            classGroup.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.OUTPUT_TYPES));
            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfResPublClass(classGroup));
        }
    }

    private void createCode(String value, ClassId classId) {
        if (Objects.nonNull(value)) {
            Stream.of(value.split("\\|\\|"))
                    .map(elem -> new String(elem))
                    .forEach(consumer -> {
                        CfFedIdEmbType fedId = new CfFedIdEmbType();

                        CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
                        fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
                        fedId.setCfFedId(consumer);
                        fedIdClass.setCfClassId(Semantics.getClassId(classId));
                        fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
                        getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfFedId(fedId));
                    });
        }
    }

    private void createOutPutType() {
        if (Objects.nonNull(row.getAs(11))) {
            CfResPublType.CfResPublResPubl outPut = new CfResPublType.CfResPublResPubl();
            outPut.setCfResPublId2(row.getAs(11));
            outPut.setCfClassId(Semantics.getClassId(ClassId.BOOK));
            outPut.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.OUTPUT_TYPES));

            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfResPublResPubl(outPut));
        }
    }

    private void createContributor() {
        if (Objects.nonNull(row.getAs(12))) {
            CfResPublType.CfOrgUnitResPubl outPut = new CfResPublType.CfOrgUnitResPubl();
            outPut.setCfOrgUnitId(row.getAs(12));
            outPut.setCfClassId(Semantics.getClassId(ClassId.UNIVERSITY_PRESS));
            outPut.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_OUTPUT_CONTRIBUTIONS));
            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfOrgUnitResPubl(outPut));
        }
    }

    private void createGroupAuthors() {
        if (Objects.nonNull(row.getAs(14))) {
            CfResPublType.CfPersResPubl group = new CfResPublType.CfPersResPubl();
            group.setCfPersId(row.getAs(14));
            group.setCfClassId(Semantics.getClassId(ClassId.GROUP_AUTHORS));
            group.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_OUTPUT_CONTRIBUTIONS));

            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfPersResPubl(group));
        }
    }

    private void createRelationCfPers() {
        if (Objects.nonNull(row.getAs(15))) {
            List<Row> relations = row.getList(15);

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

    private void researcher(String id, String direccio) {
        CfResPublType.CfPersResPubl pers = new CfResPublType.CfPersResPubl();
        pers.setCfPersId(id);
        if (direccio.toLowerCase().equals("si")
                || direccio.toLowerCase().equals("s")) pers.setCfClassId(Semantics.getClassId(ClassId.DISS_DIRECTOR));
        else pers.setCfClassId(Semantics.getClassId(ClassId.AUTHOR));

        pers.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_PROFESSIONAL_RELATIONSHIPS));
        getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfPersResPubl(pers));
    }
}
