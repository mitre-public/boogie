
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DepartureOrArrivalPort complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DepartureOrArrivalPort"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="arrivalPortRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="departurePortRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DepartureOrArrivalPort", propOrder = {
    "arrivalPortRef",
    "departurePortRef"
})
public class DepartureOrArrivalPort {

    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object arrivalPortRef;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object departurePortRef;

    /**
     * Gets the value of the arrivalPortRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getArrivalPortRef() {
        return arrivalPortRef;
    }

    /**
     * Sets the value of the arrivalPortRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setArrivalPortRef(Object value) {
        this.arrivalPortRef = value;
    }

    /**
     * Gets the value of the departurePortRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getDeparturePortRef() {
        return departurePortRef;
    }

    /**
     * Sets the value of the departurePortRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setDeparturePortRef(Object value) {
        this.departurePortRef = value;
    }

}
