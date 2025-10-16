
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Runway Identifier” field identifies the runways described in runway records and runways served by the ILS/MLS described in ILS/MLS records.
 * 
 * <p>Java class for RunwayIdentifier complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RunwayIdentifier"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="runwayLeftRightCenterType" type="{http://www.arinc424.com/xml/enumerations}RunwayLeftRightCenterType" minOccurs="0"/&gt;
 *         &lt;element name="runwayNumber" type="{http://www.arinc424.com/xml/datatypes}RunwayNumber"/&gt;
 *         &lt;element name="runwaySuffix" type="{http://www.arinc424.com/xml/enumerations}RunwaySuffix" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RunwayIdentifier", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "runwayLeftRightCenterType",
    "runwayNumber",
    "runwaySuffix"
})
public class RunwayIdentifier {

    @XmlSchemaType(name = "string")
    protected RunwayLeftRightCenterType runwayLeftRightCenterType;
    @XmlSchemaType(name = "unsignedInt")
    protected long runwayNumber;
    @XmlSchemaType(name = "string")
    protected RunwaySuffix runwaySuffix;

    /**
     * Gets the value of the runwayLeftRightCenterType property.
     * 
     * @return
     *     possible object is
     *     {@link RunwayLeftRightCenterType }
     *     
     */
    public RunwayLeftRightCenterType getRunwayLeftRightCenterType() {
        return runwayLeftRightCenterType;
    }

    /**
     * Sets the value of the runwayLeftRightCenterType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RunwayLeftRightCenterType }
     *     
     */
    public void setRunwayLeftRightCenterType(RunwayLeftRightCenterType value) {
        this.runwayLeftRightCenterType = value;
    }

    /**
     * Gets the value of the runwayNumber property.
     * 
     */
    public long getRunwayNumber() {
        return runwayNumber;
    }

    /**
     * Sets the value of the runwayNumber property.
     * 
     */
    public void setRunwayNumber(long value) {
        this.runwayNumber = value;
    }

    /**
     * Gets the value of the runwaySuffix property.
     * 
     * @return
     *     possible object is
     *     {@link RunwaySuffix }
     *     
     */
    public RunwaySuffix getRunwaySuffix() {
        return runwaySuffix;
    }

    /**
     * Sets the value of the runwaySuffix property.
     * 
     * @param value
     *     allowed object is
     *     {@link RunwaySuffix }
     *     
     */
    public void setRunwaySuffix(RunwaySuffix value) {
        this.runwaySuffix = value;
    }

}
