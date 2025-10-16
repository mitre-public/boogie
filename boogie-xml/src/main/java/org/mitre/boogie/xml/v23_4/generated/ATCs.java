
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 Multiple air traffic control centers (ATCs)
 *                 might be involved with an air refueling route.
 *                 Here's the set of ATCs.
 *             
 * 
 * <p>Java class for ATCs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ATCs"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}ARTCC" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ATCs", propOrder = {
    "artcc"
})
public class ATCs {

    @XmlElement(name = "ARTCC", required = true)
    protected List<ATC> artcc;

    /**
     * Gets the value of the artcc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the artcc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getARTCC().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ATC }
     * 
     * 
     */
    public List<ATC> getARTCC() {
        if (artcc == null) {
            artcc = new ArrayList<ATC>();
        }
        return this.artcc;
    }

}
