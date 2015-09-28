/**
 * 
 */
package csv;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.Charsets;
import org.apache.log4j.Logger;

import com.drew.lang.annotations.NotNull;

import marshal.MarshalCerif;


/**
 * @author amartinez
 *
 */
public class Factory {
	@NotNull private CSVResearcher RES;
	@NotNull private CSVDepartment DEP;
	@NotNull private CSVGroup GR;
	@NotNull private CSVProject PROJ;
	@NotNull private CSVPublication PUBL;
	
	/**
	 * 
	 * @param input
	 */
	public Factory(@NotNull final List<Entity> input) {
		for(Entity ent : input){
			if(ent.getClass().equals(CSVResearcher.class))	RES = (CSVResearcher) ent;							
			if(ent.getClass().equals(CSVDepartment.class))	DEP = (CSVDepartment) ent;
			if(ent.getClass().equals(CSVGroup.class))	GR = (CSVGroup) ent;
			if(ent.getClass().equals(CSVProject.class))	PROJ = (CSVProject) ent;
			if(ent.getClass().equals(CSVPublication.class))	PUBL = (CSVPublication) ent;
		}
	}
	
	/**
	 * 
	 * @param marshalCERIF
	 * @param outFile
	 */
	public void MakeXML(MarshalCerif marshalCERIF, String outFile) {		
		RES.ReadCSV(marshalCERIF, null);
		DEP.ReadCSV(marshalCERIF, RES);
		GR.ReadCSV(marshalCERIF, RES);
		PROJ.ReadCSV(marshalCERIF, RES);
		PUBL.ReadCSV(marshalCERIF, RES);
		
		//Crear llistat d'uncheckeds
		if(!RES.getUNCHECKEDS().isEmpty())	RES.addUncheckeds();
		try {
			marshalCERIF.createCerif(marshalCERIF.extracted(RES, DEP, GR, PROJ, PUBL),//List entities type
					new FileOutputStream(outFile),//out file
					Boolean.TRUE, //Format
					Charsets.UTF_8.toString());//charset
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(Factory.class.getName()).info("[UnsupportedEncodingException]" + e);
		} catch (FileNotFoundException e) {
			Logger.getLogger(Factory.class.getName()).info("[FileNotFoundException]" + e);
		} catch (JAXBException e) {
			Logger.getLogger(Factory.class.getName()).info("[JAXBException]" + e);
		}
	}
}
