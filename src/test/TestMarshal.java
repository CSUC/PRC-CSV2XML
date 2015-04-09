/**
 * 
 */
package test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import marshal.MarshalCerif;

import org.apache.commons.io.Charsets;
import org.apache.log4j.Logger;

import csv.CSVDepartment;
import csv.CSVGroup;
import csv.CSVProject;
import csv.CSVPublication;
import csv.CSVResearcher;


/**
 * @author amartinez
 *
 */
public class TestMarshal {
	private static String INV;
	private static String DEPT;
	private static String DEPT_INV;
	private static String GR;
	private static String GR_INV;
	private static String PROJ;
	private static String PROJ_INV;
	private static String PUBL;
	private static String PUBL_INV;
	
	private static String OUT = "./files/output/output.xml";
	
	/**
	 * Codi RUCT: https://www.educacion.gob.es/ruct/consultacentros?actual=centros
	 */
	private static String UNIVERSITY = "024";
			
	/**
	 * @param args
	 * @throws DatatypeConfigurationException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws DatatypeConfigurationException, UnsupportedEncodingException {
		Marshal();
	}
	
	private static void Marshal() throws UnsupportedEncodingException{
		MarshalCerif marshalCERIF = new MarshalCerif(UNIVERSITY);
		try {								
			CSVResearcher res = new CSVResearcher(INV, marshalCERIF);
			CSVDepartment dep = new CSVDepartment(DEPT, marshalCERIF, DEPT_INV, res);
			CSVGroup gr = new CSVGroup(GR, marshalCERIF, GR_INV, res);
			CSVProject proj = new CSVProject(PROJ, marshalCERIF, PROJ_INV, res);
			CSVPublication publ = new CSVPublication(PUBL, marshalCERIF, PUBL_INV, res);
						
			//Crear llistat d'uncheckeds
			if(!res.getUNCHECKEDS().isEmpty())	res.addUncheckeds();
			
			//Si no es vol generar totes les entitats de cop...
			//List ent = Arrays.asList(res.getListPersType());
					
			marshalCERIF.createCerif(marshalCERIF.extracted(res, dep, gr, proj, publ),
									new FileOutputStream(OUT),
									Boolean.TRUE,
									Charsets.UTF_8.toString());
			
		} catch (FileNotFoundException e) {
			Logger.getLogger(TestMarshal.class.getName()).info(e);
		} catch (JAXBException e) {
			Logger.getLogger(TestMarshal.class.getName()).info(e);
		}
	}
	
	
	

}
