
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SidLeg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SidLeg"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}SidStarLeg"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isEngineOutDisarmPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isInitialDepartureFix" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isQuietClimbRestorePoint" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SidLeg", propOrder = {
    "isEngineOutDisarmPoint",
    "isInitialDepartureFix",
    "isQuietClimbRestorePoint"
})
public class SidLeg
    extends SidStarLeg
{

    @XmlElement(defaultValue = "false")
    protected Boolean isEngineOutDisarmPoint;
    @XmlElement(defaultValue = "false")
    protected Boolean isInitialDepartureFix;
    @XmlElement(defaultValue = "false")
    protected Boolean isQuietClimbRestorePoint;

    /**
     * Gets the value of the isEngineOutDisarmPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsEngineOutDisarmPoint() {
        return isEngineOutDisarmPoint;
    }

    /**
     * Sets the value of the isEngineOutDisarmPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsEngineOutDisarmPoint(Boolean value) {
        this.isEngineOutDisarmPoint = value;
    }

    /**
     * Gets the value of the isInitialDepartureFix property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsInitialDepartureFix() {
        return isInitialDepartureFix;
    }

    /**
     * Sets the value of the isInitialDepartureFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsInitialDepartureFix(Boolean value) {
        this.isInitialDepartureFix = value;
    }

    /**
     * Gets the value of the isQuietClimbRestorePoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsQuietClimbRestorePoint() {
        return isQuietClimbRestorePoint;
    }

    /**
     * Sets the value of the isQuietClimbRestorePoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsQuietClimbRestorePoint(Boolean value) {
        this.isQuietClimbRestorePoint = value;
    }

}
