
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The Geographical Reference Table file contains information that permits the cross referencing of otherwise undefined geographical entities and Route Identifiers in the Preferred Route file. The contents are not standardized and may vary from data supplier to data supplier. The contents of such a file can only be used in conjunction with the Preferred Route file of the same database in which the file is presented.
 * 
 * <p>Java class for GeographicalReference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeographicalReference"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="geographicalEntity" type="{http://www.arinc424.com/xml/datatypes}GeographicalEntity" minOccurs="0"/&gt;
 *         &lt;element name="geographicalRefTableIdentifier" type="{http://www.arinc424.com/xml/datatypes}GeographicalReferenceTableIdentifier"/&gt;
 *         &lt;element name="preferredRouteDetails" type="{http://www.arinc424.com/xml/datatypes}PreferredRouteDetails" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeographicalReference", propOrder = {
    "geographicalEntity",
    "geographicalRefTableIdentifier",
    "preferredRouteDetails"
})
public class GeographicalReference
    extends A424Record
{

    protected String geographicalEntity;
    @XmlElement(required = true)
    protected GeographicalReferenceTableIdentifier geographicalRefTableIdentifier;
    @XmlElement(required = true)
    protected List<PreferredRouteDetails> preferredRouteDetails;

    /**
     * Gets the value of the geographicalEntity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeographicalEntity() {
        return geographicalEntity;
    }

    /**
     * Sets the value of the geographicalEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeographicalEntity(String value) {
        this.geographicalEntity = value;
    }

    /**
     * Gets the value of the geographicalRefTableIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link GeographicalReferenceTableIdentifier }
     *     
     */
    public GeographicalReferenceTableIdentifier getGeographicalRefTableIdentifier() {
        return geographicalRefTableIdentifier;
    }

    /**
     * Sets the value of the geographicalRefTableIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeographicalReferenceTableIdentifier }
     *     
     */
    public void setGeographicalRefTableIdentifier(GeographicalReferenceTableIdentifier value) {
        this.geographicalRefTableIdentifier = value;
    }

    /**
     * Gets the value of the preferredRouteDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the preferredRouteDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreferredRouteDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PreferredRouteDetails }
     * 
     * 
     */
    public List<PreferredRouteDetails> getPreferredRouteDetails() {
        if (preferredRouteDetails == null) {
            preferredRouteDetails = new ArrayList<PreferredRouteDetails>();
        }
        return this.preferredRouteDetails;
    }

}
