
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Represents an ARINC 424 record type
 * 
 * <p>Java class for A424Record complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="A424Record"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Base"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="areaCode" type="{http://www.arinc424.com/xml/enumerations}AreaCode" minOccurs="0"/&gt;
 *         &lt;element name="customerCode" type="{http://www.arinc424.com/xml/datatypes}CustomerCode" minOccurs="0"/&gt;
 *         &lt;element name="cycleDate" type="{http://www.arinc424.com/xml/datatypes}CycleDate"/&gt;
 *         &lt;element name="notes" type="{http://www.arinc424.com/xml/datatypes}NotesText" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="recordType" type="{http://www.arinc424.com/xml/enumerations}RecordType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "A424Record", propOrder = {
    "areaCode",
    "customerCode",
    "cycleDate",
    "notes",
    "recordType"
})
@XmlSeeAlso({
    AirRefuelingRoute.class,
    AirspaceSegment.class,
    FirUir.class,
    EnrouteAirwaysRestriction.class,
    CompanyRoute.class,
    Leg.class,
    Communication.class,
    Alternate.class,
    GeographicalReference.class,
    SpecialActivityArea.class,
    HoldingPattern.class,
    GridMora.class,
    FlightPlanningRecord.class,
    PathPoint.class,
    Minima.class,
    Taa.class,
    Msa.class,
    A424Point.class
})
public abstract class A424Record
    extends A424Base
{

    @XmlSchemaType(name = "string")
    protected AreaCode areaCode;
    protected String customerCode;
    @XmlElement(required = true)
    protected String cycleDate;
    protected List<String> notes;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected RecordType recordType;

    /**
     * Gets the value of the areaCode property.
     * 
     * @return
     *     possible object is
     *     {@link AreaCode }
     *     
     */
    public AreaCode getAreaCode() {
        return areaCode;
    }

    /**
     * Sets the value of the areaCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link AreaCode }
     *     
     */
    public void setAreaCode(AreaCode value) {
        this.areaCode = value;
    }

    /**
     * Gets the value of the customerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * Sets the value of the customerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerCode(String value) {
        this.customerCode = value;
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
     * Gets the value of the notes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the notes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNotes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNotes() {
        if (notes == null) {
            notes = new ArrayList<String>();
        }
        return this.notes;
    }

    /**
     * Gets the value of the recordType property.
     * 
     * @return
     *     possible object is
     *     {@link RecordType }
     *     
     */
    public RecordType getRecordType() {
        return recordType;
    }

    /**
     * Sets the value of the recordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecordType }
     *     
     */
    public void setRecordType(RecordType value) {
        this.recordType = value;
    }

}
