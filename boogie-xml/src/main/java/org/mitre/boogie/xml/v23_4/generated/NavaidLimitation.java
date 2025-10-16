
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class represents the information in the 424 navaid limitation continuation record.
 * 
 * <p>Java class for NavaidLimitation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NavaidLimitation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="navaidLimitationCode" type="{http://www.arinc424.com/xml/enumerations}NavaidLimitationCode" minOccurs="0"/&gt;
 *         &lt;element name="componentAffectedIndicator" type="{http://www.arinc424.com/xml/enumerations}ComponentAffectedIndicator" minOccurs="0"/&gt;
 *         &lt;element name="sectorDetails" type="{http://www.arinc424.com/xml/datatypes}SectorDetails" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="sequenceEndIndicator" type="{http://www.arinc424.com/xml/datatypes}SequenceEndIndicator" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NavaidLimitation", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "navaidLimitationCode",
    "componentAffectedIndicator",
    "sectorDetails",
    "sequenceEndIndicator"
})
public class NavaidLimitation {

    @XmlSchemaType(name = "string")
    protected NavaidLimitationCode navaidLimitationCode;
    @XmlSchemaType(name = "string")
    protected ComponentAffectedIndicator componentAffectedIndicator;
    protected List<SectorDetails> sectorDetails;
    protected String sequenceEndIndicator;

    /**
     * Gets the value of the navaidLimitationCode property.
     * 
     * @return
     *     possible object is
     *     {@link NavaidLimitationCode }
     *     
     */
    public NavaidLimitationCode getNavaidLimitationCode() {
        return navaidLimitationCode;
    }

    /**
     * Sets the value of the navaidLimitationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link NavaidLimitationCode }
     *     
     */
    public void setNavaidLimitationCode(NavaidLimitationCode value) {
        this.navaidLimitationCode = value;
    }

    /**
     * Gets the value of the componentAffectedIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link ComponentAffectedIndicator }
     *     
     */
    public ComponentAffectedIndicator getComponentAffectedIndicator() {
        return componentAffectedIndicator;
    }

    /**
     * Sets the value of the componentAffectedIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComponentAffectedIndicator }
     *     
     */
    public void setComponentAffectedIndicator(ComponentAffectedIndicator value) {
        this.componentAffectedIndicator = value;
    }

    /**
     * Gets the value of the sectorDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the sectorDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSectorDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SectorDetails }
     * 
     * 
     */
    public List<SectorDetails> getSectorDetails() {
        if (sectorDetails == null) {
            sectorDetails = new ArrayList<SectorDetails>();
        }
        return this.sectorDetails;
    }

    /**
     * Gets the value of the sequenceEndIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSequenceEndIndicator() {
        return sequenceEndIndicator;
    }

    /**
     * Sets the value of the sequenceEndIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSequenceEndIndicator(String value) {
        this.sequenceEndIndicator = value;
    }

}
