package org.csuc.marshal;

import org.apache.commons.text.StringEscapeUtils;
import org.csuc.global.RandomNumeric;
import org.csuc.global.Time;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author amartinez
 */
public class MarshalProject extends CfProjType implements Factory{

    private ObjectFactory FACTORY = new ObjectFactory();

    private String title;
    private String url;
    private String official;
    private String code;
    private String funding;
    private String inici;
    private String fi;

    private List<List<Object>> relation;
    private CopyOnWriteArrayList<CfPersType> cfPersTypeList;
    private CopyOnWriteArrayList<CfPersType> newCfPersType = new CopyOnWriteArrayList<>();

    public MarshalProject(String title, String uri, String officialCode,
                           String code, String programme, String dateInici, String dateFi, List relation, List<CfPersType> cfPersType){

        this.title = StringEscapeUtils.escapeXml11(title);
        this.url = StringEscapeUtils.escapeXml11(uri);
        this.official = StringEscapeUtils.escapeXml11(officialCode);
        this.code = StringEscapeUtils.escapeXml11(code);
        this.funding = StringEscapeUtils.escapeXml11(programme);
        this.inici = StringEscapeUtils.escapeXml11(dateInici);
        this.fi = StringEscapeUtils.escapeXml11(dateFi);

        this.relation = relation;
        this.cfPersTypeList = Objects.nonNull(cfPersType) ? new CopyOnWriteArrayList(cfPersType) : null;

        execute();
    }

    private void createTitle(String langCode, CfTransType transType){
        if(Objects.nonNull(title)){
            CfMLangStringType name = new CfMLangStringType();
            name.setCfLangCode(langCode);
            name.setCfTrans(transType);
            name.setValue(title);
                getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfTitle(name));
        }
    }

    private void createUrl(){
        if(Objects.nonNull(url)) setCfURI(url);
    }

    private void createDate(){
        if(Objects.nonNull(inici)) setCfStartDate(Time.formatDateTime(inici));
        if(Objects.nonNull(fi)) setCfEndDate(Time.formatDateTime(fi));
    }

    private void createCode(String value, ClassId classId){
        if(Objects.nonNull(value)){
            CfFedIdEmbType fedId = new CfFedIdEmbType();

            CfCoreClassWithFractionType fedIdClass = new CfCoreClassWithFractionType();
            fedIdClass.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.IDENTIFIER_TYPES));
            fedId.setCfFedId(value);
            fedIdClass.setCfClassId(Semantics.getClassId(classId));
            fedId.getCfFedIdClassOrCfFedIdSrv().add(fedIdClass);
            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfFedId(fedId));
        }
    }

    private void createFundingProgramme(){
        if(Objects.nonNull(funding)){
            CfProjType.CfProjFund fund = new CfProjType.CfProjFund();
            fund.setCfFundId(funding);
            fund.setCfClassId(Semantics.getClassId(ClassId.FUNDING_PROGRAMME));
            fund.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.FUNDING_SOURCE_TYPES));
            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfProjFund(fund));
        }
    }

    private void createRelationCfPers(){
        if(Objects.nonNull(relation)){
            relation.stream().forEach(consumer->{
                if(Objects.nonNull(cfPersTypeList) && code.equals(consumer.get(0).toString())){
                    if(Objects.nonNull(consumer.get(2))){
                        CfPersType id = getIdentifier(cfPersTypeList, consumer.get(2).toString());
                        researcher(id.getCfPersId(), consumer.get(3).toString());
                    }else{
                        String random = RandomNumeric.getInstance().newId();
                        researcher(random, consumer.get(3).toString());
                        newCfPersType.add(new MarshalReseracher(random, null, null,null, consumer.get(1).toString(),
                                null, null, Semantics.getClassId(ClassId.UNCHECKED)));
                    }
                }
            });
        }
    }

    private void researcher(String id, String ip){
        CfProjType.CfProjPers pers = new CfProjType.CfProjPers();
        pers.setCfPersId(id);
        if (ip.toLowerCase().equals("si")
                || ip.toLowerCase().equals("s")) {
            pers.setCfClassId(Semantics.getClassId(ClassId.PRINCIPAL_INVESTIGATOR));
            pers.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_PROJECT_ENGAGEMENTS));
        } else if (ip.toLowerCase().equals("no")
                || ip.toLowerCase().equals("n")) {
            pers.setCfClassId(Semantics.getClassId(ClassId.CO_INVESTIGATOR));
            pers.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_PROJECT_ENGAGEMENTS));
        }
        getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfProjPers(pers));
    }

    @Override
    public void execute() {
        setCfProjId(RandomNumeric.getInstance().newId());

        createTitle("ca", CfTransType.O);
        createUrl();
        createDate();
        createCode(code, ClassId.INTERNAL_PROJECT_CODE);
        createCode(official, ClassId.OFFICIAL_PROJECT_CODE);
        createFundingProgramme();
        createRelationCfPers();
    }

    private CfPersType getIdentifier(List<CfPersType> cfPersTypeList, String value) {
        return cfPersTypeList.stream().filter(cfPersType ->
                cfPersType.getCfResIntOrCfKeywOrCfPersPers()
                        .stream().filter(f -> f.getDeclaredType().equals(CfFedIdEmbType.class))
                        .map(m -> (CfFedIdEmbType) m.getValue())
                        .filter(f -> f.getCfFedId().equals(value)) != null).findFirst().orElse(null);
    }

    public ArrayList<CfPersType> getNewCfPersType() {
        return newCfPersType.stream().collect(Collectors.toCollection(ArrayList::new));
    }
}
