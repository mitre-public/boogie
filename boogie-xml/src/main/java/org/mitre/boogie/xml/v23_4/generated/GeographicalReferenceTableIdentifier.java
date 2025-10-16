
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * The “Geographical Reference Table Identifier” will be used to provide a unique identification for each Geographical Entity. As the “Geographical Entity” field is a large field with no established content, this two character code will act as a pseudo key for the record.
 * 
 * <p>Java class for GeographicalReferenceTableIdentifier complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeographicalReferenceTableIdentifier"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="geographicalReferenceTableIdentifierPos1" type="{http://www.arinc424.com/xml/datatypes}GeographicalReferenceTableIdentifierPos1"/&gt;
 *         &lt;element name="geographicalReferenceTableIdentifierPos2" type="{http://www.arinc424.com/xml/datatypes}GeographicalReferenceTableIdentifierPos2"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeographicalReferenceTableIdentifier", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "geographicalReferenceTableIdentifierPos1",
    "geographicalReferenceTableIdentifierPos2"
})
public class GeographicalReferenceTableIdentifier {

    @XmlElement(required = true)
    protected String geographicalReferenceTableIdentifierPos1;
    @XmlSchemaType(name = "unsignedInt")
    protected long geographicalReferenceTableIdentifierPos2;

    /**
     * Gets the value of the geographicalReferenceTableIdentifierPos1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeographicalReferenceTableIdentifierPos1() {
        return geographicalReferenceTableIdentifierPos1;
    }

    /**
     * Sets the value of the geographicalReferenceTableIdentifierPos1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeographicalReferenceTableIdentifierPos1(String value) {
        this.geographicalReferenceTableIdentifierPos1 = value;
    }

    /**
     * Gets the value of the geographicalReferenceTableIdentifierPos2 property.
     * 
     */
    public long getGeographicalReferenceTableIdentifierPos2() {
        return geographicalReferenceTableIdentifierPos2;
    }

    /**
     * Sets the value of the geographicalReferenceTableIdentifierPos2 property.
     * 
     */
    public void setGeographicalReferenceTableIdentifierPos2(long value) {
        this.geographicalReferenceTableIdentifierPos2 = value;
    }

}
