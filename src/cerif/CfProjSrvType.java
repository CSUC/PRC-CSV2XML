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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for cfProj_Srv__Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cfProj_Srv__Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cfProjId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfId__Type"/>
 *         &lt;element name="cfSrvId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfId__Type"/>
 *         &lt;group ref="{urn:xmlns:org:eurocris:cerif-1.6-2}cfCoreClassWithFraction__Group"/>
 *         &lt;element name="cfAvailability" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cfConditions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cfPrice" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfAmount__Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cfProj_Srv__Type", propOrder = {
    "cfProjId",
    "cfSrvId",
    "cfClassId",
    "cfClassSchemeId",
    "cfStartDate",
    "cfEndDate",
    "cfFraction",
    "cfAvailability",
    "cfConditions",
    "cfPrice"
})
public class CfProjSrvType {

    protected String cfAvailability;
    @XmlElement(required = true)
    protected String cfClassId;
    @XmlElement(required = true)
    protected String cfClassSchemeId;
    protected String cfConditions;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar cfEndDate;
    protected Float cfFraction;
    protected CfAmountType cfPrice;
    @XmlElement(required = true)
    protected String cfProjId;
    @XmlElement(required = true)
    protected String cfSrvId;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar cfStartDate;

    /**
     * Gets the value of the cfAvailability property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfAvailability() {
        return cfAvailability;
    }

    /**
     * Gets the value of the cfClassId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfClassId() {
        return cfClassId;
    }

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
     * Gets the value of the cfConditions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfConditions() {
        return cfConditions;
    }

    /**
     * Gets the value of the cfEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCfEndDate() {
        return cfEndDate;
    }

    /**
     * Gets the value of the cfFraction property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getCfFraction() {
        return cfFraction;
    }

    /**
     * Gets the value of the cfPrice property.
     * 
     * @return
     *     possible object is
     *     {@link CfAmountType }
     *     
     */
    public CfAmountType getCfPrice() {
        return cfPrice;
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
     * Gets the value of the cfSrvId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfSrvId() {
        return cfSrvId;
    }

    /**
     * Gets the value of the cfStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCfStartDate() {
        return cfStartDate;
    }

    /**
     * Sets the value of the cfAvailability property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfAvailability(String value) {
        this.cfAvailability = value;
    }

    /**
     * Sets the value of the cfClassId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfClassId(String value) {
        this.cfClassId = value;
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
     * Sets the value of the cfConditions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfConditions(String value) {
        this.cfConditions = value;
    }

    /**
     * Sets the value of the cfEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCfEndDate(XMLGregorianCalendar value) {
        this.cfEndDate = value;
    }

    /**
     * Sets the value of the cfFraction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setCfFraction(Float value) {
        this.cfFraction = value;
    }

    /**
     * Sets the value of the cfPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link CfAmountType }
     *     
     */
    public void setCfPrice(CfAmountType value) {
        this.cfPrice = value;
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

    /**
     * Sets the value of the cfSrvId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfSrvId(String value) {
        this.cfSrvId = value;
    }

    /**
     * Sets the value of the cfStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCfStartDate(XMLGregorianCalendar value) {
        this.cfStartDate = value;
    }

}
