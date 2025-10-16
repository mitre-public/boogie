
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Altitude Limitation” is used on navaid limitation continuation records.  It defines the altitude(s) at which the limitation applies (See 424 section 5.209).
 * 
 * <p>Java class for AltitudeLimitation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AltitudeLimitation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.arinc424.com/xml/datatypes}Alpha"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="lowerAltitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeConstraint"/&gt;
 *         &lt;element name="upperAltitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeConstraint"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AltitudeLimitation", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "lowerAltitude",
    "upperAltitude"
})
public class AltitudeLimitation
    extends Alpha
{

    @XmlElement(required = true)
    protected AltitudeConstraint lowerAltitude;
    @XmlElement(required = true)
    protected AltitudeConstraint upperAltitude;

    /**
     * Gets the value of the lowerAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link AltitudeConstraint }
     *     
     */
    public AltitudeConstraint getLowerAltitude() {
        return lowerAltitude;
    }

    /**
     * Sets the value of the lowerAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link AltitudeConstraint }
     *     
     */
    public void setLowerAltitude(AltitudeConstraint value) {
        this.lowerAltitude = value;
    }

    /**
     * Gets the value of the upperAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link AltitudeConstraint }
     *     
     */
    public AltitudeConstraint getUpperAltitude() {
        return upperAltitude;
    }

    /**
     * Sets the value of the upperAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link AltitudeConstraint }
     *     
     */
    public void setUpperAltitude(AltitudeConstraint value) {
        this.upperAltitude = value;
    }

}
