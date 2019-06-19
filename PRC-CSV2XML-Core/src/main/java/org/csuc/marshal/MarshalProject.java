package org.csuc.marshal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.global.RandomNumeric;
import org.csuc.global.Time;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import xmlns.org.eurocris.cerif_1.*;

import javax.xml.bind.JAXBElement;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author amartinez
 */
public class MarshalProject extends CfProjType implements Factory{

    private static Logger logger = LogManager.getLogger(MarshalProject.class);

    private ObjectFactory FACTORY = new ObjectFactory();

    private NameOrTitle title;
    private String url;
    private String official;
    private String code;
    private String funding;
    private String inici;
    private String fi;

    private List<List<Object>> relation;
    private CopyOnWriteArrayList<CfPersType> cfPersTypeList;
    private CopyOnWriteArrayList<CfPersType> newCfPersType = new CopyOnWriteArrayList<>();

    public MarshalProject(NameOrTitle title, String uri, String officialCode,
                           String code, String programme, String dateInici, String dateFi, List relation, List<CfPersType> cfPersType){

        this.title = title;
        this.url = uri;
        this.official = officialCode;
        this.code = code;
        this.funding = programme;
        this.inici = dateInici;
        this.fi = dateFi;

        this.relation = relation;
        this.cfPersTypeList = Objects.nonNull(cfPersType) ? new CopyOnWriteArrayList(cfPersType) : null;

        execute();
    }

    private void createTitle(){
        if(Objects.nonNull(title)){
            CfMLangStringType name = new CfMLangStringType();
            name.setCfLangCode(title.getLangCode());
            name.setCfTrans(title.getTrans());
            name.setValue(title.getValue());
                getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfTitle(name));
        }
    }

    private void createUrl(){
        if(Objects.nonNull(url)) setCfURI(url);
    }

    private void createDate(){
        if(Objects.nonNull(inici)) {
            try {
                setCfStartDate(Time.formatDateTime(inici));
            } catch (Exception e) {
                logger.warn(e);
            }
        }
        if(Objects.nonNull(fi)) {
            try {
                setCfEndDate(Time.formatDateTime(fi));
            } catch (Exception e) {
                logger.warn(e);
            }
        }
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
                        CfPersType id = getIdentifier(consumer.get(2).toString());
                        if(Objects.nonNull(id)) researcher(id.getCfPersId(), (Objects.nonNull(consumer.get(3))) ? consumer.get(3).toString() : "");
                    }else{
                        String random = RandomNumeric.getInstance().newId();
                        researcher(random, (Objects.nonNull(consumer.get(3))) ? consumer.get(3).toString() : "");
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

        String normalized = Normalizer.normalize(ip, Normalizer.Form.NFD);

        if (normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").equalsIgnoreCase("si")
                || normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").equalsIgnoreCase("s"))    pers.setCfClassId(Semantics.getClassId(ClassId.PRINCIPAL_INVESTIGATOR));
        else if (ip.equalsIgnoreCase("no")
                || ip.equalsIgnoreCase("n")) pers.setCfClassId(Semantics.getClassId(ClassId.CO_INVESTIGATOR));

        pers.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_PROJECT_ENGAGEMENTS));
        getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfProjTypeCfProjPers(pers));
    }

    @Override
    public void execute() {
        setCfProjId(RandomNumeric.getInstance().newId());

        createTitle();
        createUrl();
        createDate();
        createCode(code, ClassId.INTERNAL_PROJECT_CODE);
        createCode(official, ClassId.OFFICIAL_PROJECT_CODE);
        createFundingProgramme();
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

    public ArrayList<CfPersType> getNewCfPersType() {
        return newCfPersType.stream().collect(Collectors.toCollection(ArrayList::new));
    }
}
