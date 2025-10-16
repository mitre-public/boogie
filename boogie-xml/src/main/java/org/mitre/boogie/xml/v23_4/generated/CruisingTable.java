
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for CruisingTable complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CruisingTable"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Base"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cruiseTableIdentifier" type="{http://www.arinc424.com/xml/datatypes}CruiseTableIdentifier"/&gt;
 *         &lt;element name="cruiseDirection" type="{}CruiseDirection" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="referenceId" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CruisingTable", propOrder = {
    "cruiseTableIdentifier",
    "cruiseDirection"
})
public class CruisingTable
    extends A424Base
{

    @XmlElement(required = true)
    protected String cruiseTableIdentifier;
    @XmlElement(required = true)
    protected List<CruiseDirection> cruiseDirection;
    @XmlAttribute(name = "referenceId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String referenceId;

    /**
     * Gets the value of the cruiseTableIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCruiseTableIdentifier() {
        return cruiseTableIdentifier;
    }

    /**
     * Sets the value of the cruiseTableIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCruiseTableIdentifier(String value) {
        this.cruiseTableIdentifier = value;
    }

    /**
     * Gets the value of the cruiseDirection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the cruiseDirection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCruiseDirection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CruiseDirection }
     * 
     * 
     */
    public List<CruiseDirection> getCruiseDirection() {
        if (cruiseDirection == null) {
            cruiseDirection = new ArrayList<CruiseDirection>();
        }
        return this.cruiseDirection;
    }

    /**
     * Gets the value of the referenceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceId() {
        return referenceId;
    }

    /**
     * Sets the value of the referenceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceId(String value) {
        this.referenceId = value;
    }

}
