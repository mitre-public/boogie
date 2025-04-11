
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The RNP Level of service and authorization status.
 * 
 * <p>Java class for RnpLevelOfService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RnpLevelOfService"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rnpAuthorized" type="{http://www.arinc424.com/xml/enumerations}LevelOfServiceAuthorized"/&gt;
 *         &lt;element name="rnpValue" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RnpLevelOfService", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "rnpAuthorized",
    "rnpValue"
})
public class RnpLevelOfService {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected LevelOfServiceAuthorized rnpAuthorized;
    @XmlElement(required = true)
    protected BigDecimal rnpValue;

    /**
     * Gets the value of the rnpAuthorized property.
     * 
     * @return
     *     possible object is
     *     {@link LevelOfServiceAuthorized }
     *     
     */
    public LevelOfServiceAuthorized getRnpAuthorized() {
        return rnpAuthorized;
    }

    /**
     * Sets the value of the rnpAuthorized property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelOfServiceAuthorized }
     *     
     */
    public void setRnpAuthorized(LevelOfServiceAuthorized value) {
        this.rnpAuthorized = value;
    }

    /**
     * Gets the value of the rnpValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRnpValue() {
        return rnpValue;
    }

    /**
     * Sets the value of the rnpValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRnpValue(BigDecimal value) {
        this.rnpValue = value;
    }

}
