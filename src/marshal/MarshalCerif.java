/**
 * 
 */
package marshal;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import csv.CSVDepartment;
import csv.CSVGroup;
import csv.CSVProject;
import csv.CSVPublication;
import csv.CSVResearcher;
import cerif.CERIF;
import cerif.ObjectFactory;

/**
 * @author amartinez
 *
 */
public class MarshalCerif {
	
	private ObjectFactory factory;
    private CERIF cerif;
    
    /**
     * 
     */
    public MarshalCerif(String version) {
    	factory = new ObjectFactory();
        cerif = factory.createCERIF();
        cerif.setDate(newDate());
        cerif.setSourceDatabase(version);
	}	
    
    /**
     * 
     * @param out Nom amb ruta absoluta del fitxer que crearà i volcarà tot el contingut XML.
     * @param formattedOutput True si es vol el format tabulat, False minimitzat.
     * @param charset Format de sortida. CERIF especifica a l'XSD que el format ha de set UTF-8
     * @throws JAXBException
     */
    public void createCerif(List<List<? extends Object>> list, OutputStream out, boolean formattedOutput, String charset) throws JAXBException {
    	if(list!=null)	for(List<? extends Object> objs : list)	for(Object obj : objs)	cerif.getCfClassOrCfClassSchemeOrCfClassSchemeDescr().add(obj);
        JAXBContext context = JAXBContext.newInstance(CERIF.class);
        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "urn:xmlns:org:eurocris:cerif-1.6-2 http://www.eurocris.org/Uploads/Web%20pages/CERIF-1.6/CERIF_1.6_2.xsd");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formattedOutput);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, charset);      
        marshaller.marshal(cerif,out);
    }
    
   
    
    /**
     * Data del moment que es crea l'XML
     * 
     * @return XMLGregorianCalendar amb la data del moment
     */
    private XMLGregorianCalendar newDate(){
        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(new Date());
        try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
		} catch (DatatypeConfigurationException e) {			
			Logger.getLogger(MarshalCerif.class.getName()).info(e);
			return null;
		}
    }
    
    /**
     * Apila a l'objecte CERIF les llistes d'entitats.
     * 
     * @param res
     * @param dep
     * @param gr
     * @param proj
     * @param publ
     * @return
     */
    public List<List<? extends Object>> extracted(CSVResearcher res,
			CSVDepartment dep, CSVGroup gr, CSVProject proj, CSVPublication publ) {
		return Arrays.asList(res.getListPersType(), dep.getListDepType(),
										gr.getListGrType(), proj.getListProjType(), publ.getListPublType());
	}
        
    /************************************************** GETTERS / SETTERS ***************************************************/
    
    public ObjectFactory getFactory() {
        return factory;
    }

    public CERIF getCerif() {
        return cerif;
    }
}
