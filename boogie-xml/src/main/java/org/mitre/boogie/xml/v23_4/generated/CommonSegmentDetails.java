
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This type represents the details for a Common Segment.
 * 
 * <p>Java class for CommonSegmentDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CommonSegmentDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="alongTrackDistance" type="{http://www.arinc424.com/xml/datatypes}AlongTrackDistance"/&gt;
 *         &lt;element name="fixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommonSegmentDetails", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "alongTrackDistance",
    "fixRef"
})
public class CommonSegmentDetails {

    @XmlSchemaType(name = "unsignedInt")
    protected long alongTrackDistance;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object fixRef;

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
     * Gets the value of the fixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFixRef() {
        return fixRef;
    }

    /**
     * Sets the value of the fixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFixRef(Object value) {
        this.fixRef = value;
    }

}
