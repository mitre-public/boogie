
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This data type contains all of the information that is needed to identify parts of a SID used in the company route
 * 
 * <p>Java class for ViaSid complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ViaSid"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sidRef" type="{http://www.w3.org/2001/XMLSchema}IDREF"/&gt;
 *         &lt;element name="sidSelectedRoutes" type="{}SidStarSelectedRoutes" minOccurs="0"/&gt;
 *         &lt;element name="cycleDate" type="{http://www.arinc424.com/xml/datatypes}CycleDate"/&gt;
 *         &lt;element name="cruiseAltitude" type="{http://www.arinc424.com/xml/datatypes}AltitudeValue" minOccurs="0"/&gt;
 *         &lt;element name="toFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViaSid", propOrder = {
    "sidRef",
    "sidSelectedRoutes",
    "cycleDate",
    "cruiseAltitude",
    "toFixRef"
})
public class ViaSid {

    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object sidRef;
    protected SidStarSelectedRoutes sidSelectedRoutes;
    @XmlElement(required = true)
    protected String cycleDate;
    @XmlSchemaType(name = "integer")
    protected Integer cruiseAltitude;
    @XmlElement(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object toFixRef;

    /**
     * Gets the value of the sidRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSidRef() {
        return sidRef;
    }

    /**
     * Sets the value of the sidRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSidRef(Object value) {
        this.sidRef = value;
    }

    /**
     * Gets the value of the sidSelectedRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link SidStarSelectedRoutes }
     *     
     */
    public SidStarSelectedRoutes getSidSelectedRoutes() {
        return sidSelectedRoutes;
    }

    /**
     * Sets the value of the sidSelectedRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link SidStarSelectedRoutes }
     *     
     */
    public void setSidSelectedRoutes(SidStarSelectedRoutes value) {
        this.sidSelectedRoutes = value;
    }

    /**
     * Gets the value of the cycleDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCycleDate() {
        return cycleDate;
    }

    /**
     * Sets the value of the cycleDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCycleDate(String value) {
        this.cycleDate = value;
    }

    /**
     * Gets the value of the cruiseAltitude property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCruiseAltitude() {
        return cruiseAltitude;
    }

    /**
     * Sets the value of the cruiseAltitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCruiseAltitude(Integer value) {
        this.cruiseAltitude = value;
    }

    /**
     * Gets the value of the toFixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getToFixRef() {
        return toFixRef;
    }

    /**
     * Sets the value of the toFixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setToFixRef(Object value) {
        this.toFixRef = value;
    }

}
