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
 * <p>Java class for cfPers_ResPubl__Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cfPers_ResPubl__Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cfPersId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfId__Type"/>
 *         &lt;element name="cfResPublId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfId__Type"/>
 *         &lt;group ref="{urn:xmlns:org:eurocris:cerif-1.6-2}cfCoreClassWithFraction__Group"/>
 *         &lt;element name="cfCopyright" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cfOrder" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cfPers_ResPubl__Type", propOrder = {
    "cfPersId",
    "cfResPublId",
    "cfClassId",
    "cfClassSchemeId",
    "cfStartDate",
    "cfEndDate",
    "cfFraction",
    "cfCopyright",
    "cfOrder"
})
public class CfPersResPublType {

    @XmlElement(required = true)
    protected String cfClassId;
    @XmlElement(required = true)
    protected String cfClassSchemeId;
    protected String cfCopyright;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar cfEndDate;
    protected Float cfFraction;
    protected Integer cfOrder;
    @XmlElement(required = true)
    protected String cfPersId;
    @XmlElement(required = true)
    protected String cfResPublId;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar cfStartDate;

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
     * Gets the value of the cfCopyright property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfCopyright() {
        return cfCopyright;
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
     * Gets the value of the cfOrder property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCfOrder() {
        return cfOrder;
    }

    /**
     * Gets the value of the cfPersId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfPersId() {
        return cfPersId;
    }

    /**
     * Gets the value of the cfResPublId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfResPublId() {
        return cfResPublId;
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
     * Sets the value of the cfCopyright property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfCopyright(String value) {
        this.cfCopyright = value;
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
     * Sets the value of the cfOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCfOrder(Integer value) {
        this.cfOrder = value;
    }

    /**
     * Sets the value of the cfPersId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfPersId(String value) {
        this.cfPersId = value;
    }

    /**
     * Sets the value of the cfResPublId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfResPublId(String value) {
        this.cfResPublId = value;
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
