//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.08.18 at 08:52:34 AM CEST 
//


package cerif;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cfProjAbstr__Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cfProjAbstr__Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cfProjId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfId__Type"/>
 *         &lt;element name="cfAbstr" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfMLangString__Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cfProjAbstr__Type", propOrder = {
    "cfProjId",
    "cfAbstr"
})
public class CfProjAbstrType {

    @XmlElement(required = true)
    protected CfMLangStringType cfAbstr;
    @XmlElement(required = true)
    protected String cfProjId;

    /**
     * Gets the value of the cfAbstr property.
     * 
     * @return
     *     possible object is
     *     {@link CfMLangStringType }
     *     
     */
    public CfMLangStringType getCfAbstr() {
        return cfAbstr;
    }

    /**
     * Gets the value of the cfProjId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfProjId() {
        return cfProjId;
    }

    /**
     * Sets the value of the cfAbstr property.
     * 
     * @param value
     *     allowed object is
     *     {@link CfMLangStringType }
     *     
     */
    public void setCfAbstr(CfMLangStringType value) {
        this.cfAbstr = value;
    }

    /**
     * Sets the value of the cfProjId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfProjId(String value) {
        this.cfProjId = value;
    }

}
