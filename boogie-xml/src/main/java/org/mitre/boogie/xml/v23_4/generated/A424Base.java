
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Base Class for the A424 Records.
 * 
 * <p>Java class for A424Base complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="A424Base"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="supplementalData" type="{http://www.arinc424.com/xml/datatypes}SupplementalData" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "A424Base", propOrder = {
    "supplementalData"
})
@XmlSeeAlso({
    CruisingTable.class,
    Airspace.class,
    A424ObjectWithId.class,
    A424Record.class,
    AeroTelecomNetwork.class
})
public abstract class A424Base {

    protected SupplementalData supplementalData;

    /**
     * Gets the value of the supplementalData property.
     * 
     * @return
     *     possible object is
     *     {@link SupplementalData }
     *     
     */
    public SupplementalData getSupplementalData() {
        return supplementalData;
    }

    /**
     * Sets the value of the supplementalData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupplementalData }
     *     
     */
    public void setSupplementalData(SupplementalData value) {
        this.supplementalData = value;
    }

}
