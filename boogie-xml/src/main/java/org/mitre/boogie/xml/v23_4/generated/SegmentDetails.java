
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This type represents the details for a segment.
 * 
 * <p>Java class for SegmentDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SegmentDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="transitionIdentifier" type="{http://www.arinc424.com/xml/datatypes}CoreIdentifier"/&gt;
 *         &lt;element name="alongTrackDistance" type="{http://www.arinc424.com/xml/datatypes}AlongTrackDistance"/&gt;
 *         &lt;element name="fix" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SegmentDetails", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "transitionIdentifier",
    "alongTrackDistance",
    "fix"
})
public class SegmentDetails {

    @XmlElement(required = true)
    protected String transitionIdentifier;
    @XmlSchemaType(name = "unsignedInt")
    protected long alongTrackDistance;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object fix;

    /**
     * Gets the value of the transitionIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransitionIdentifier() {
        return transitionIdentifier;
    }

    /**
     * Sets the value of the transitionIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransitionIdentifier(String value) {
        this.transitionIdentifier = value;
    }

    /**
     * Gets the value of the alongTrackDistance property.
     * 
     */
    public long getAlongTrackDistance() {
        return alongTrackDistance;
    }

    /**
     * Sets the value of the alongTrackDistance property.
     * 
     */
    public void setAlongTrackDistance(long value) {
        this.alongTrackDistance = value;
    }

    /**
     * Gets the value of the fix property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFix() {
        return fix;
    }

    /**
     * Sets the value of the fix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFix(Object value) {
        this.fix = value;
    }

}
