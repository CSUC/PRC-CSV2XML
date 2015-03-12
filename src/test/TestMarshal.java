/**
 * 
 */
package test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import marshal.MarshalCerif;
import marshal.MarshalInvestigators;

import org.apache.commons.io.Charsets;
import org.apache.log4j.Logger;

import cerif.CfPersType;
import entity.Researcher;

/**
 * @author amartinez
 *
 */
public class TestMarshal {
	private static MarshalCerif marshalCERIF = new MarshalCerif("1");
	private static ArrayList<Researcher> Researchers;
	private static List<CfPersType> listPersType = new ArrayList<CfPersType>();
	
	private static String fileOutPut = "./output.xml";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Researchers = createResearchers();
		for(Researcher res : Researchers){
			listPersType.add(new MarshalInvestigators(marshalCERIF.getFactory(),
					res.getFIRST_NAMES(), 
					res.getFAMILY_NAMES(),
					res.getSIGNATURE_FIRST_NAMES(), 
					res.getSIGNATURE_FAMILY_NAMES(), 
					res.getORCID(), 
					res.getEMAIL(), 
					res.getUNIVERSITY()).getPers());			
		}
		try {
			marshalCERIF.createCerif(Arrays.asList(listPersType), 
					new FileOutputStream(fileOutPut),
					Boolean.TRUE, Charsets.UTF_8.toString());
		} catch (FileNotFoundException | JAXBException e) {
			Logger.getLogger(TestMarshal.class.getName()).info(e);
		}finally{
			Logger.getLogger(TestMarshal.class.getName()).info("DONE!");
		}
	}
	
	
	private static ArrayList<Researcher> createResearchers(){
		ArrayList<Researcher> arrayResearcher = new ArrayList<Researcher>();
		arrayResearcher.add(new Researcher(
				"M. Lourdes", "Ariño Carmona", null, null, "0000-0002-1436-9453", "a@a.com.com", "012"));
		arrayResearcher.add(new Researcher(
				"Elena", "Armario García", null, null, "0000-0002-1436-9453", "a@a.com", "012"));
		arrayResearcher.add(new Researcher(
				"Jordi", "Arús Caraltó", null, null, "0000-0002-1436-9453", "a@a.com", "012"));
		arrayResearcher.add(new Researcher(
				"Abel", "Ausina Ruiz", null, null, "0000-0002-1436-9453", "a@a.com", "012"));
		arrayResearcher.add(new Researcher(
				"Josep Maria", "Bacardí Tomàs", null, null, "0000-0002-1436-9453", "a@a.com", "012"));
		arrayResearcher.add(new Researcher(
				"Mercè", "Bacaria Colom", null, null, "0000-0002-1436-9453", "a@a.com", "012"));
		arrayResearcher.add(new Researcher(
				"Jesús", "Boix Borràs", null, null, "0000-0002-1436-9453", "a@a.com", "012"));
		return arrayResearcher;
	}
}
