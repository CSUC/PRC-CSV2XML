/**
 * 
 */
package test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

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
	
	private static String fileOutPut = "./files/output/output.xml";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		MarshalCerif marshalCERIF = new MarshalCerif("1");
		try {
			CSVResearcher res = new CSVResearcher(INV, marshalCERIF);			
			CSVDepartment dep = new CSVDepartment(DEPT, marshalCERIF, DEPT_INV, res.getMapResearcher());				
			CSVGroup gr = new CSVGroup(GR, marshalCERIF, GR_INV, res.getMapResearcher());
			CSVProject proj = new CSVProject(PROJ, marshalCERIF, PROJ_INV, res.getMapResearcher());			
			CSVPublication publ = new CSVPublication(PUBL, marshalCERIF, PUBL_INV, res.getMapResearcher());
			
			List entities = marshalCERIF.extracted(res, dep, gr, proj, publ);
			
			marshalCERIF.createCerif(entities, 
								new FileOutputStream(fileOutPut),
								Boolean.TRUE, Charsets.UTF_8.toString());
			
		} catch (FileNotFoundException | JAXBException e) {
			Logger.getLogger(TestMarshal.class.getName()).info(e);
		}finally{
			Logger.getLogger(TestMarshal.class.getName()).info("DONE!");
		}
	}
}
