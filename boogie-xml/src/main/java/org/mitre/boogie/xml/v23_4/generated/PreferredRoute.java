
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Preferred Route Record.
 * 
 * <p>Java class for PreferredRoute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PreferredRoute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Route"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="preferredRouteRouteType" type="{http://www.arinc424.com/xml/enumerations}PreferredRouteRouteType"/&gt;
 *         &lt;element name="timesOfOperation" type="{http://www.arinc424.com/xml/datatypes}TimesOfOperation" minOccurs="0"/&gt;
 *         &lt;element name="preferredRouteLeg" type="{}PreferredRouteLeg" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PreferredRoute", propOrder = {
    "preferredRouteRouteType",
    "timesOfOperation",
    "preferredRouteLeg"
})
public class PreferredRoute
    extends Route
{

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected PreferredRouteRouteType preferredRouteRouteType;
    protected TimesOfOperation timesOfOperation;
    @XmlElement(required = true)
    protected List<PreferredRouteLeg> preferredRouteLeg;

    /**
     * Gets the value of the preferredRouteRouteType property.
     * 
     * @return
     *     possible object is
     *     {@link PreferredRouteRouteType }
     *     
     */
    public PreferredRouteRouteType getPreferredRouteRouteType() {
        return preferredRouteRouteType;
    }

    /**
     * Sets the value of the preferredRouteRouteType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PreferredRouteRouteType }
     *     
     */
    public void setPreferredRouteRouteType(PreferredRouteRouteType value) {
        this.preferredRouteRouteType = value;
    }

    /**
     * Gets the value of the timesOfOperation property.
     * 
     * @return
     *     possible object is
     *     {@link TimesOfOperation }
     *     
     */
    public TimesOfOperation getTimesOfOperation() {
        return timesOfOperation;
    }

    /**
     * Sets the value of the timesOfOperation property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimesOfOperation }
     *     
     */
    public void setTimesOfOperation(TimesOfOperation value) {
        this.timesOfOperation = value;
    }

    /**
     * Gets the value of the preferredRouteLeg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the preferredRouteLeg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreferredRouteLeg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PreferredRouteLeg }
     * 
     * 
     */
    public List<PreferredRouteLeg> getPreferredRouteLeg() {
        if (preferredRouteLeg == null) {
            preferredRouteLeg = new ArrayList<PreferredRouteLeg>();
        }
        return this.preferredRouteLeg;
    }

}
