
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PrecisionApproachNavaid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PrecisionApproachNavaid"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Navaid"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="approachAngle" type="{http://www.arinc424.com/xml/datatypes}PrecisionApproachAngle" minOccurs="0"/&gt;
 *         &lt;element name="approachCourseBearing" type="{http://www.arinc424.com/xml/datatypes}Bearing" minOccurs="0"/&gt;
 *         &lt;element name="category" type="{http://www.arinc424.com/xml/enumerations}PrecisionApproachCategory" minOccurs="0"/&gt;
 *         &lt;element name="runwayIdentifier" type="{http://www.arinc424.com/xml/datatypes}RunwayIdentifier" minOccurs="0"/&gt;
 *         &lt;element name="runwayRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PrecisionApproachNavaid", propOrder = {
    "approachAngle",
    "approachCourseBearing",
    "category",
    "runwayIdentifier",
    "runwayRef"
})
@XmlSeeAlso({
    Gls.class,
    Mls.class,
    LocalizerGlideslope.class
})
public abstract class PrecisionApproachNavaid
    extends Navaid
{

    protected BigDecimal approachAngle;
    protected Bearing approachCourseBearing;
    @XmlSchemaType(name = "string")
    protected PrecisionApproachCategory category;
    protected RunwayIdentifier runwayIdentifier;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object runwayRef;

    /**
     * Gets the value of the approachAngle property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getApproachAngle() {
        return approachAngle;
    }

    /**
     * Sets the value of the approachAngle property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setApproachAngle(BigDecimal value) {
        this.approachAngle = value;
    }

    /**
     * Gets the value of the approachCourseBearing property.
     * 
     * @return
     *     possible object is
     *     {@link Bearing }
     *     
     */
    public Bearing getApproachCourseBearing() {
        return approachCourseBearing;
    }

    /**
     * Sets the value of the approachCourseBearing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bearing }
     *     
     */
    public void setApproachCourseBearing(Bearing value) {
        this.approachCourseBearing = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link PrecisionApproachCategory }
     *     
     */
    public PrecisionApproachCategory getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrecisionApproachCategory }
     *     
     */
    public void setCategory(PrecisionApproachCategory value) {
        this.category = value;
    }

    /**
     * Gets the value of the runwayIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link RunwayIdentifier }
     *     
     */
    public RunwayIdentifier getRunwayIdentifier() {
        return runwayIdentifier;
    }

    /**
     * Sets the value of the runwayIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link RunwayIdentifier }
     *     
     */
    public void setRunwayIdentifier(RunwayIdentifier value) {
        this.runwayIdentifier = value;
    }

    /**
     * Gets the value of the runwayRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRunwayRef() {
        return runwayRef;
    }

    /**
     * Sets the value of the runwayRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRunwayRef(Object value) {
        this.runwayRef = value;
    }

}
