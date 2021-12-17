package org.csuc.marshal;

import org.apache.spark.sql.Row;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.CfCoreClassWithFractionType;
import xmlns.org.eurocris.cerif_1.CfFedIdEmbType;
import xmlns.org.eurocris.cerif_1.CfPersType;
import xmlns.org.eurocris.cerif_1.ObjectFactory;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author amartinez
 */
public class Researcher extends CfPersType {

    private ObjectFactory FACTORY = new ObjectFactory();
    private Row row;

    public Researcher(Row row, String classId) {
        this.row = row;

        setCfPersId(row.getAs(4) == null ? UUID.randomUUID().toString() : row.getAs(4));

        createEntityClass(classId);
        createPersNamePers(row.getAs(0), ClassId.PRESENTED_NAME);
        createPersNamePers(row.getAs(2), ClassId.SIGNATURE);
        createCode(ClassId.ORCID);
        createEmail();
    }


    private void createEntityClass(String classId) {
        // if(Objects.nonNull(classId)){
        CfCoreClassWithFractionType cfCoreClass = new CfCoreClassWithFractionType();
        cfCoreClass.setCfClassId(classId);
        cfCoreClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.VERIFICATION_STATUSES));
        getCfResIntOrCfKeywOrCfPersPers().add(FACTORY.createCfPersTypeCfPersClass(cfCoreClass));
        // }
    }

    private void createPersNamePers(String first, ClassId classId) {
        CfPersNamePers persname = new CfPersNamePers();
        persname.setCfPersNameId(UUID.randomUUID().toString());
        persname.setCfFirstNames(first);
        persname.setCfClassId(Semantics.getClassId(classId));
        persname.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_NAMES));
        getCfResIntOrCfKeywOrCfPersPers().add(FACTORY.createCfPersTypeCfPersNamePers(persname));
    }

    private void createCode(ClassId classId) {
        if (Objects.nonNull(row.getAs(1))) {
            CfFedIdEmbType fedId = new CfFedIdEmbType();

            CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
            fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
            fedId.setCfFedId(row.getAs(1));
            fedIdClass.setCfClassId(Semantics.getClassId(classId));
            fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
            getCfResIntOrCfKeywOrCfPersPers().add(FACTORY.createCfPersTypeCfFedId(fedId));
        }
    }

    private void createEmail() {
        if (Objects.nonNull(row.getAs(3))) {
            Stream.of(((String) row.getAs(3)).split("\\|\\|"))
                    .map(String::new)
                    .forEach(consumer -> {
                        CfPersEAddr email = new CfPersEAddr();
                        email.setCfEAddrId(consumer);
                        email.setCfClassId(Semantics.getClassId(ClassId.EMAIL));
                        email.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_CONTACT_DETAILS));
                        getCfResIntOrCfKeywOrCfPersPers().add(FACTORY.createCfPersTypeCfPersEAddr(email));
                    });
        }
    }
}
