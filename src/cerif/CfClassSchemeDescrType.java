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
 * <p>Java class for cfClassSchemeDescr__Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cfClassSchemeDescr__Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cfClassSchemeId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfId__Type"/>
 *         &lt;element name="cfDescr" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfMLangString__Type"/>
 *         &lt;element name="cfDescrSrc" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfMLangString__Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cfClassSchemeDescr__Type", propOrder = {
    "cfClassSchemeId",
    "cfDescr",
    "cfDescrSrc"
})
public class CfClassSchemeDescrType {

    @XmlElement(required = true)
    protected String cfClassSchemeId;
    @XmlElement(required = true)
    protected CfMLangStringType cfDescr;
    protected CfMLangStringType cfDescrSrc;

    /**
     * Gets the value of the cfClassSchemeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfClassSchemeId() {
        return cfClassSchemeId;
    }

    /**
     * Gets the value of the cfDescr property.
     * 
     * @return
     *     possible object is
     *     {@link CfMLangStringType }
     *     
     */
    public CfMLangStringType getCfDescr() {
        return cfDescr;
    }

    /**
     * Gets the value of the cfDescrSrc property.
     * 
     * @return
     *     possible object is
     *     {@link CfMLangStringType }
     *     
     */
    public CfMLangStringType getCfDescrSrc() {
        return cfDescrSrc;
    }

    /**
     * Sets the value of the cfClassSchemeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfClassSchemeId(String value) {
        this.cfClassSchemeId = value;
    }

    /**
     * Sets the value of the cfDescr property.
     * 
     * @param value
     *     allowed object is
     *     {@link CfMLangStringType }
     *     
     */
    public void setCfDescr(CfMLangStringType value) {
        this.cfDescr = value;
    }

    /**
     * Sets the value of the cfDescrSrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link CfMLangStringType }
     *     
     */
    public void setCfDescrSrc(CfMLangStringType value) {
        this.cfDescrSrc = value;
    }

}
