
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The following record contains the fields used in FIR UIR Record.
 * 
 * <p>Java class for FirUirSegment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FirUirSegment"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}AirspaceSegment"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="adjacentFirIdentifier" type="{http://www.arinc424.com/xml/datatypes}FirUirIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="adjacentUirIdentifier" type="{http://www.arinc424.com/xml/datatypes}FirUirIdentifier" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirUirSegment", propOrder = {
    "adjacentFirIdentifier",
    "adjacentUirIdentifier"
})
public class FirUirSegment
    extends AirspaceSegment
{

    protected String adjacentFirIdentifier;
    protected String adjacentUirIdentifier;

    /**
     * Gets the value of the adjacentFirIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdjacentFirIdentifier() {
        return adjacentFirIdentifier;
    }

    /**
     * Sets the value of the adjacentFirIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdjacentFirIdentifier(String value) {
        this.adjacentFirIdentifier = value;
    }

    /**
     * Gets the value of the adjacentUirIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdjacentUirIdentifier() {
        return adjacentUirIdentifier;
    }

    /**
     * Sets the value of the adjacentUirIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdjacentUirIdentifier(String value) {
        this.adjacentUirIdentifier = value;
    }

}
