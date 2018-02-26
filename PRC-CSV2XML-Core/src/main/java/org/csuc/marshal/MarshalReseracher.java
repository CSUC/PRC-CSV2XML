package org.csuc.marshal;

import org.apache.commons.text.StringEscapeUtils;
import org.csuc.global.RandomNumeric;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.CfCoreClassWithFractionType;
import xmlns.org.eurocris.cerif_1.CfFedIdEmbType;
import xmlns.org.eurocris.cerif_1.CfPersType;
import xmlns.org.eurocris.cerif_1.ObjectFactory;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author amartinez
 */
public class MarshalReseracher extends CfPersType implements Factory{

    private ObjectFactory FACTORY = new ObjectFactory();

    private String _id;

    private String familyNames;
    private String firstNames;
    private String orcid;
    private String signatureFamilyNames;
    private String signatureFirstNames;
    private String ae;
    private String typeClass;

    public MarshalReseracher(String id, String familyNames, String firstNames, String orcid,
                             String signatureFamilyNames, String signatureFirstNames, String ae, String type) {

        this._id = id;
        this.familyNames = StringEscapeUtils.escapeXml11(familyNames);
        this.firstNames = StringEscapeUtils.escapeXml11(firstNames);
        this.orcid = StringEscapeUtils.escapeXml11(orcid);
        this.signatureFamilyNames = StringEscapeUtils.escapeXml11(signatureFamilyNames);
        this.signatureFirstNames = StringEscapeUtils.escapeXml11(signatureFirstNames);
        this.ae = StringEscapeUtils.escapeXml11(ae);
        this.typeClass = StringEscapeUtils.escapeXml11(type);

        execute();
    }


    private void createEntityClass(){
        if(Objects.nonNull(typeClass)){
            CfCoreClassWithFractionType cfCoreClass = new CfCoreClassWithFractionType();
            cfCoreClass.setCfClassId(typeClass);
            cfCoreClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.VERIFICATION_STATUSES));
            getCfResIntOrCfKeywOrCfPersPers().add(FACTORY.createCfPersTypeCfPersClass(cfCoreClass));
        }
    }

    private void createPersNamePers(String family, String first, ClassId classId){
        if (family != null) {
            CfPersType.CfPersNamePers persname = new CfPersType.CfPersNamePers();
            persname.setCfPersNameId(RandomNumeric.getInstance().newId());
            persname.setCfFamilyNames(family);
            if (first != null) persname.setCfFirstNames(first);
            persname.setCfClassId(Semantics.getClassId(classId));
            persname.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_NAMES));
            getCfResIntOrCfKeywOrCfPersPers().add(FACTORY.createCfPersTypeCfPersNamePers(persname));
        }
    }

    private void createCode(ClassId classId){
        if(Objects.nonNull(orcid)){
            CfFedIdEmbType fedId = new CfFedIdEmbType();

            CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
            fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
            fedId.setCfFedId(orcid);
            fedIdClass.setCfClassId(Semantics.getClassId(classId));
            fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
            getCfResIntOrCfKeywOrCfPersPers().add(FACTORY.createCfPersTypeCfFedId(fedId));
        }
    }

    private void createEmail(){
        if(Objects.nonNull(ae)){
            Stream.of(ae.split("\\|\\|"))
                    .map(elem -> new String(elem))
                    .forEach(consumer -> {
                        CfPersEAddr email = new CfPersEAddr();
                        email.setCfEAddrId(consumer);
                        email.setCfClassId(Semantics.getClassId(ClassId.EMAIL));
                        email.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_CONTACT_DETAILS));
                        getCfResIntOrCfKeywOrCfPersPers().add(FACTORY.createCfPersTypeCfPersEAddr(email));
                    });
        }
    }

    @Override
    public void execute() {
        if(this._id == null)	this._id = RandomNumeric.getInstance().newId();
        setCfPersId(_id);
        createEntityClass();
        createPersNamePers(familyNames, firstNames, ClassId.PRESENTED_NAME);
        createPersNamePers(signatureFamilyNames, signatureFirstNames, ClassId.SIGNATURE);
        createCode(ClassId.ORCID);
        createEmail();
    }
}
