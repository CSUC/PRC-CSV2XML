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
 * <p>Java class for cfFDCRightsMMPricing__Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cfFDCRightsMMPricing__Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cfDCId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cfDCScheme" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cfDCLangTag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cfFDCTrans" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cfFDCPriceConstraint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cfFDCRightsMMPricing__Type", propOrder = {
    "cfDCId",
    "cfDCScheme",
    "cfDCLangTag",
    "cfFDCTrans",
    "cfFDCPriceConstraint"
})
public class CfFDCRightsMMPricingType {

    @XmlElement(required = true)
    protected String cfDCId;
    @XmlElement(required = true)
    protected String cfDCLangTag;
    @XmlElement(required = true)
    protected String cfDCScheme;
    protected String cfFDCPriceConstraint;
    @XmlElement(required = true)
    protected String cfFDCTrans;

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
     * Gets the value of the cfFDCPriceConstraint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfFDCPriceConstraint() {
        return cfFDCPriceConstraint;
    }

    /**
     * Gets the value of the cfFDCTrans property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfFDCTrans() {
        return cfFDCTrans;
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
     * Sets the value of the cfFDCPriceConstraint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfFDCPriceConstraint(String value) {
        this.cfFDCPriceConstraint = value;
    }

    /**
     * Sets the value of the cfFDCTrans property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfFDCTrans(String value) {
        this.cfFDCTrans = value;
    }

}
