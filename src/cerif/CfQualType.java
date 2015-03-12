//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.08.18 at 08:52:34 AM CEST 
//


package cerif;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for cfQual__Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cfQual__Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cfQualId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfId__Type"/>
 *         &lt;element name="cfURI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="cfDescr" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfMLangString__Type"/>
 *           &lt;element name="cfKeyw" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfMLangString__Type"/>
 *           &lt;element name="cfTitle" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfMLangString__Type"/>
 *           &lt;element name="cfPers_Qual">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="cfPersId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfId__Type"/>
 *                     &lt;group ref="{urn:xmlns:org:eurocris:cerif-1.6-2}cfCoreClassWithFraction__Group"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="cfQual_Class" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfCoreClassWithFraction__Type"/>
 *           &lt;element name="cfFedId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfFedId__EmbType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cfQual__Type", propOrder = {
    "cfQualId",
    "cfURI",
    "cfDescrOrCfKeywOrCfTitle"
})
public class CfQualType {

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="cfPersId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfId__Type"/>
     *         &lt;group ref="{urn:xmlns:org:eurocris:cerif-1.6-2}cfCoreClassWithFraction__Group"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "cfPersId",
        "cfClassId",
        "cfClassSchemeId",
        "cfStartDate",
        "cfEndDate",
        "cfFraction"
    })
    public static class CfPersQual {

        @XmlElement(required = true)
        protected String cfClassId;
        @XmlElement(required = true)
        protected String cfClassSchemeId;
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar cfEndDate;
        protected Float cfFraction;
        @XmlElement(required = true)
        protected String cfPersId;
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
    @XmlElementRefs({
        @XmlElementRef(name = "cfKeyw", namespace = "urn:xmlns:org:eurocris:cerif-1.6-2", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "cfTitle", namespace = "urn:xmlns:org:eurocris:cerif-1.6-2", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "cfDescr", namespace = "urn:xmlns:org:eurocris:cerif-1.6-2", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "cfQual_Class", namespace = "urn:xmlns:org:eurocris:cerif-1.6-2", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "cfPers_Qual", namespace = "urn:xmlns:org:eurocris:cerif-1.6-2", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "cfFedId", namespace = "urn:xmlns:org:eurocris:cerif-1.6-2", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> cfDescrOrCfKeywOrCfTitle;
    @XmlElement(required = true)
    protected String cfQualId;

    protected String cfURI;

    /**
     * Gets the value of the cfDescrOrCfKeywOrCfTitle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cfDescrOrCfKeywOrCfTitle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCfDescrOrCfKeywOrCfTitle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link CfCoreClassWithFractionType }{@code >}
     * {@link JAXBElement }{@code <}{@link CfMLangStringType }{@code >}
     * {@link JAXBElement }{@code <}{@link CfQualType.CfPersQual }{@code >}
     * {@link JAXBElement }{@code <}{@link CfMLangStringType }{@code >}
     * {@link JAXBElement }{@code <}{@link CfMLangStringType }{@code >}
     * {@link JAXBElement }{@code <}{@link CfFedIdEmbType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getCfDescrOrCfKeywOrCfTitle() {
        if (cfDescrOrCfKeywOrCfTitle == null) {
            cfDescrOrCfKeywOrCfTitle = new ArrayList<JAXBElement<?>>();
        }
        return this.cfDescrOrCfKeywOrCfTitle;
    }

    /**
     * Gets the value of the cfQualId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfQualId() {
        return cfQualId;
    }

    /**
     * Gets the value of the cfURI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfURI() {
        return cfURI;
    }

    /**
     * Sets the value of the cfQualId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfQualId(String value) {
        this.cfQualId = value;
    }


    /**
     * Sets the value of the cfURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfURI(String value) {
        this.cfURI = value;
    }

}
