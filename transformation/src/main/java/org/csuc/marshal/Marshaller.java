package org.csuc.marshal;

import xmlns.org.eurocris.cerif_1.CERIF;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Marshaller {

    private CERIF cerif = new CERIF();

    public Marshaller(String ruct) throws DatatypeConfigurationException {
        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(new Date());
        cerif.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory));
        cerif.setSourceDatabase(ruct);
    }

    public void build(String output, boolean formatted, List... objects) throws JAXBException, FileNotFoundException {
        Arrays.stream(objects).forEach(o -> {
            cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().addAll(o);
        });

        JAXBContext jaxbContext = JAXBContext.newInstance(CERIF.class);
        javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.toString());
        jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
        jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, false);

        jaxbMarshaller.marshal(cerif, new FileOutputStream(output));
    }
}
