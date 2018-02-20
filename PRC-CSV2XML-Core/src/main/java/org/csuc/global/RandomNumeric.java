/**
 * 
 */
package org.csuc.global;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Creació identificadors únics numèrics basat amb el patró Singleton.
 * @author amartinez
 *
 */
public class RandomNumeric {
private static RandomNumeric singleton = new RandomNumeric( );

	/**
	 *  Static 'instance' method 
	 */
	public static RandomNumeric getInstance( ) {
		return singleton;
	}

	private List<String> listID = new ArrayList<String>();
	   
	/** A private Constructor prevents any other 
	 * class from instantiating.
	 */
	private RandomNumeric(){ }
	   
	public List<String> getListID() {
		return listID;
	}
	  
	public String newId(){
		String random = RandomStringUtils.randomNumeric(11);
		if(!listID.contains(random))	listID.add(random);
		return random;		
	}


}
