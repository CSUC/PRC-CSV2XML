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
 * <p>Java class for cfDCRightsMM__Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cfDCRightsMM__Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cfDCId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cfDCScheme" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cfDCLangTag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cfDCTrans" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cfDCValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cfDCType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cfDCRightsMM__Type", propOrder = {
    "cfDCId",
    "cfDCScheme",
    "cfDCLangTag",
    "cfDCTrans",
    "cfDCValue",
    "cfDCType"
})
public class CfDCRightsMMType {

    @XmlElement(required = true)
    protected String cfDCId;
    @XmlElement(required = true)
    protected String cfDCLangTag;
    @XmlElement(required = true)
    protected String cfDCScheme;
    @XmlElement(required = true)
    protected String cfDCTrans;
    protected String cfDCType;
    protected String cfDCValue;

    /**
     * Gets the value of the cfDCId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfDCId() {
        return cfDCId;
    }

    /**
     * Gets the value of the cfDCLangTag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfDCLangTag() {
        return cfDCLangTag;
    }

    /**
     * Gets the value of the cfDCScheme property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfDCScheme() {
        return cfDCScheme;
    }

    /**
     * Gets the value of the cfDCTrans property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfDCTrans() {
        return cfDCTrans;
    }

    /**
     * Gets the value of the cfDCType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfDCType() {
        return cfDCType;
    }

    /**
     * Gets the value of the cfDCValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfDCValue() {
        return cfDCValue;
    }

    /**
     * Sets the value of the cfDCId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfDCId(String value) {
        this.cfDCId = value;
    }

    /**
     * Sets the value of the cfDCLangTag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfDCLangTag(String value) {
        this.cfDCLangTag = value;
    }

    /**
     * Sets the value of the cfDCScheme property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfDCScheme(String value) {
        this.cfDCScheme = value;
    }

    /**
     * Sets the value of the cfDCTrans property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfDCTrans(String value) {
        this.cfDCTrans = value;
    }

    /**
     * Sets the value of the cfDCType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfDCType(String value) {
        this.cfDCType = value;
    }

    /**
     * Sets the value of the cfDCValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfDCValue(String value) {
        this.cfDCValue = value;
    }

}
