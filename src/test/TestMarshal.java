/**
 * 
 */
package test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;

import csv.CSVDepartment;
import csv.CSVGroup;
import csv.CSVProject;
import csv.CSVPublication;
import csv.CSVResearcher;
import csv.Factory;
import marshal.MarshalCerif;


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
		Marshal(new MarshalCerif(UNIVERSITY));
	}
	
	private static void Marshal(MarshalCerif marshalCERIF) throws UnsupportedEncodingException{
		try {	
			new Factory(Arrays.asList(
					 			new CSVResearcher(INV, marshalCERIF),
					 			new CSVDepartment(DEPT, marshalCERIF, DEPT_INV),
					 			new CSVGroup(GR, marshalCERIF, GR_INV),
					 			new CSVProject(PROJ, marshalCERIF, PROJ_INV),
					 			new CSVPublication(PUBL, marshalCERIF, PUBL_INV)
					)).MakeXML(marshalCERIF, OUT);			
		}finally{
			Logger.getLogger(TestMarshal.class.getName()).info("Done!");
		}
	}
}
