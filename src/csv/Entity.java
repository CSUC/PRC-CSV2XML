/**
 * 
 */
package csv;


import marshal.MarshalCerif;

/**
 * @author amartinez
 *
 */
public interface Entity {
	void ReadCSV(MarshalCerif marshal, CSVResearcher researcher);
}
