package org.csuc.marshal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.global.RandomNumeric;
import org.csuc.global.Time;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.SchemeId;
import org.csuc.typesafe.semantics.Semantics;
import org.csuc.utils.DocumentTypes;
import xmlns.org.eurocris.cerif_1.*;

import javax.xml.bind.JAXBElement;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author amartinez
 */
public class MarshalPublication extends CfResPublType implements Factory {

    private static Logger logger = LogManager.getLogger(MarshalPublication.class);

    private ObjectFactory FACTORY = new ObjectFactory();

    private String _id;
    private NameOrTitle title;
    private String doi;
    private String handle;
    private String num;
    private String vol;
    private String startPage;
    private String endPage;
    private String isbn;
    private String issn;
    private String date;
    private String publicatA;
    private String publicatPer;
    private String documentTypes;
    private String groupAuthors;

    private List<List<Object>> relation;
    private CopyOnWriteArrayList<CfPersType> cfPersTypeList;
    private CopyOnWriteArrayList<CfPersType> newCfPersType = new CopyOnWriteArrayList<>();

    public MarshalPublication(NameOrTitle title, String id, String doi, String handle, String num, String vol,
                              String startPage, String endPage, String isbn, String issn, String date, String publicatA,
                              String publicatPer, String documentTypes, String groupAuthors,
                              List relation, List<CfPersType> cfPersType) {

        this.title = title;
        this._id = id;
        this.doi = doi;
        this.handle = handle;
        this.num = num;
        this.vol = vol;
        this.startPage = startPage;
        this.endPage = endPage;
        this.isbn = isbn;
        this.issn = issn;
        this.date = date;
        this.publicatA = publicatA;
        this.publicatPer = publicatPer;
        this.documentTypes = documentTypes;
        this.groupAuthors = groupAuthors;

        this.relation = relation;
        this.cfPersTypeList = Objects.nonNull(cfPersType) ? new CopyOnWriteArrayList(cfPersType) : null;

        execute();
    }

    private void createNum(){
        if(Objects.nonNull(num)) setCfNum(num);
    }

    private void createVol(){
        if(Objects.nonNull(vol)) setCfVol(vol);
    }

    private void createStartPage(){
        if(Objects.nonNull(startPage)) setCfStartPage(startPage);
    }

    private void createEndPage(){
        if(Objects.nonNull(endPage))    setCfEndPage(endPage);
    }

    private void createISBN(){
        if(Objects.nonNull(isbn)) setCfISBN(isbn);
    }

    private void createISSN(){
        if(Objects.nonNull(issn)) setCfISSN(issn);
    }

    private void createDate(){
        if(Objects.nonNull(date)) {
            try {
                setCfResPublDate(Time.formatDate(date));
            } catch (Exception e) {
                logger.warn(e);
            }
        }
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

    private void createEntityClass() {
        if(Objects.nonNull(documentTypes)){
            CfCoreClassWithFractionType classGroup = new CfCoreClassWithFractionType();
            DocumentTypes type = DocumentTypes.convert(documentTypes);
            classGroup.setCfClassId((Objects.nonNull(type)) ? Semantics.getClassId(ClassId.valueOf(type.toString())) : "");
            classGroup.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.OUTPUT_TYPES));
            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfResPublClass(classGroup));
        }
    }

    private void createCode(String value, ClassId classId){
        if(Objects.nonNull(value)){
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

    private void createOutPutType(){
        if(Objects.nonNull(publicatA)){
            CfResPublType.CfResPublResPubl outPut = new CfResPublType.CfResPublResPubl();
            outPut.setCfResPublId2(publicatA);
            outPut.setCfClassId(Semantics.getClassId(ClassId.BOOK));
            outPut.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.OUTPUT_TYPES));

            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfResPublResPubl(outPut));
        }
    }

    private void createContributor(){
        if(Objects.nonNull(publicatPer)){
            CfResPublType.CfOrgUnitResPubl outPut = new CfResPublType.CfOrgUnitResPubl();
            outPut.setCfOrgUnitId(publicatPer);
            outPut.setCfClassId(Semantics.getClassId(ClassId.UNIVERSITY_PRESS));
            outPut.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.ORGANISATION_OUTPUT_CONTRIBUTIONS));
            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfOrgUnitResPubl(outPut));
        }
    }

    private void createGroupAuthors(){
        if(Objects.nonNull(groupAuthors)){
            CfResPublType.CfPersResPubl group = new CfResPublType.CfPersResPubl();
            group.setCfPersId(groupAuthors);
            group.setCfClassId(Semantics.getClassId(ClassId.GROUP_AUTHORS));
            group.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_OUTPUT_CONTRIBUTIONS));

            getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfPersResPubl(group));
        }
    }

    private void createRelationCfPers(){
        if(Objects.nonNull(relation)){
            relation.stream().forEach(consumer->{
                if(Objects.nonNull(cfPersTypeList) && _id.equals(consumer.get(0).toString())){
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

    private void researcher(String id, String direccio){
        CfResPublType.CfPersResPubl pers = new CfResPublType.CfPersResPubl();
        pers.setCfPersId(id);

        String normalized = Normalizer.normalize(direccio, Normalizer.Form.NFD);

        if (normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").equalsIgnoreCase("si")
                || normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").equalsIgnoreCase("s"))  pers.setCfClassId(Semantics.getClassId(ClassId.DISS_DIRECTOR));
        else    pers.setCfClassId(Semantics.getClassId(ClassId.AUTHOR));

        pers.setCfClassSchemeId(Semantics.getSchemaId(SchemeId.PERSON_PROFESSIONAL_RELATIONSHIPS));
        getCfTitleOrCfAbstrOrCfKeyw().add(FACTORY.createCfResPublTypeCfPersResPubl(pers));
    }

    @Override
    public void execute() {
        if(Objects.nonNull(_id)) setCfResPublId(_id);

        createNum();
        createVol();
        createStartPage();
        createEndPage();
        createISBN();
        createISSN();
        createDate();
        createTitle();
        createEntityClass();
        createCode(doi, ClassId.DOI);
        createCode(handle, ClassId.HANDLE);
        createOutPutType();
        createGroupAuthors();
        createContributor();

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
