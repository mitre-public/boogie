
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The Decision Height (DH) or Minimum Descent Height (MDH) associated with this landing minimum.
 * 
 * <p>Java class for MinimaHeight complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MinimaHeight"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="heightType" type="{http://www.arinc424.com/xml/enumerations}MinimaHeightType"/&gt;
 *         &lt;element name="height" type="{http://www.arinc424.com/xml/datatypes}AltitudeValue"/&gt;
 *         &lt;element name="heightUom" type="{http://www.arinc424.com/xml/enumerations}HeightUnitsIndicator"/&gt;
 *         &lt;element name="isDerived" type="{http://www.arinc424.com/xml/datatypes}IsDerived" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MinimaHeight", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "heightType",
    "height",
    "heightUom",
    "isDerived"
})
public class MinimaHeight {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected MinimaHeightType heightType;
    @XmlSchemaType(name = "integer")
    protected int height;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected HeightUnitsIndicator heightUom;
    @XmlElement(defaultValue = "false")
    protected Boolean isDerived;

    /**
     * Gets the value of the heightType property.
     * 
     * @return
     *     possible object is
     *     {@link MinimaHeightType }
     *     
     */
    public MinimaHeightType getHeightType() {
        return heightType;
    }

    /**
     * Sets the value of the heightType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MinimaHeightType }
     *     
     */
    public void setHeightType(MinimaHeightType value) {
        this.heightType = value;
    }

    /**
     * Gets the value of the height property.
     * 
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     */
    public void setHeight(int value) {
        this.height = value;
    }

    /**
     * Gets the value of the heightUom property.
     * 
     * @return
     *     possible object is
     *     {@link HeightUnitsIndicator }
     *     
     */
    public HeightUnitsIndicator getHeightUom() {
        return heightUom;
    }

    /**
     * Sets the value of the heightUom property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeightUnitsIndicator }
     *     
     */
    public void setHeightUom(HeightUnitsIndicator value) {
        this.heightUom = value;
    }

    /**
     * Gets the value of the isDerived property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDerived() {
        return isDerived;
    }

    /**
     * Sets the value of the isDerived property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDerived(Boolean value) {
        this.isDerived = value;
    }

}
