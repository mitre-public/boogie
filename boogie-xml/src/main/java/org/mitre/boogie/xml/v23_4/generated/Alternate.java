
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
 * The Alternate Record file contains a listing of up to six Alternate Airport Identifiers or, up to six Alternate Company Route Identifiers or any combination of Alternate Airport or Alternate Route Identifiers for a given departure airport, destination airport or enroute fix. The data content of the record is customer defined.
 * 
 * <p>Java class for Alternate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Alternate"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="alternateDetails" type="{http://www.arinc424.com/xml/datatypes}AlternateDetails" maxOccurs="unbounded"/&gt;
 *         &lt;element name="departureOrArrivalPort" type="{}DepartureOrArrivalPort"/&gt;
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
@XmlType(name = "Alternate", propOrder = {
    "alternateDetails",
    "departureOrArrivalPort"
})
public class Alternate
    extends A424Record
{

    @XmlElement(required = true)
    protected List<AlternateDetails> alternateDetails;
    @XmlElement(required = true)
    protected DepartureOrArrivalPort departureOrArrivalPort;
    @XmlAttribute(name = "referenceId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String referenceId;

    /**
     * Gets the value of the alternateDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the alternateDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlternateDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AlternateDetails }
     * 
     * 
     */
    public List<AlternateDetails> getAlternateDetails() {
        if (alternateDetails == null) {
            alternateDetails = new ArrayList<AlternateDetails>();
        }
        return this.alternateDetails;
    }

    /**
     * Gets the value of the departureOrArrivalPort property.
     * 
     * @return
     *     possible object is
     *     {@link DepartureOrArrivalPort }
     *     
     */
    public DepartureOrArrivalPort getDepartureOrArrivalPort() {
        return departureOrArrivalPort;
    }

    /**
     * Sets the value of the departureOrArrivalPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link DepartureOrArrivalPort }
     *     
     */
    public void setDepartureOrArrivalPort(DepartureOrArrivalPort value) {
        this.departureOrArrivalPort = value;
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
