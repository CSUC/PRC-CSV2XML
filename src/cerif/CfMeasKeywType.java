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
 * <p>Java class for cfMeasKeyw__Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cfMeasKeyw__Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cfMeasId" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfId__Type"/>
 *         &lt;element name="cfKeyw" type="{urn:xmlns:org:eurocris:cerif-1.6-2}cfMLangString__Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cfMeasKeyw__Type", propOrder = {
    "cfMeasId",
    "cfKeyw"
})
public class CfMeasKeywType {

    @XmlElement(required = true)
    protected CfMLangStringType cfKeyw;
    @XmlElement(required = true)
    protected String cfMeasId;

    /**
     * Gets the value of the cfKeyw property.
     * 
     * @return
     *     possible object is
     *     {@link CfMLangStringType }
     *     
     */
    public CfMLangStringType getCfKeyw() {
        return cfKeyw;
    }

    /**
     * Gets the value of the cfMeasId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCfMeasId() {
        return cfMeasId;
    }

    /**
     * Sets the value of the cfKeyw property.
     * 
     * @param value
     *     allowed object is
     *     {@link CfMLangStringType }
     *     
     */
    public void setCfKeyw(CfMLangStringType value) {
        this.cfKeyw = value;
    }

    /**
     * Sets the value of the cfMeasId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCfMeasId(String value) {
        this.cfMeasId = value;
    }

}
