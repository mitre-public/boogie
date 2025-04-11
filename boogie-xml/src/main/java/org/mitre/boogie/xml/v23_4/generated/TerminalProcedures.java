
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TerminalProcedures complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TerminalProcedures"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sid" type="{}Sid" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="star" type="{}Star" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="approach" type="{}Approach" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TerminalProcedures", propOrder = {
    "sid",
    "star",
    "approach"
})
public class TerminalProcedures {

    protected List<Sid> sid;
    protected List<Star> star;
    protected List<Approach> approach;

    /**
     * Gets the value of the sid property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the sid property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSid().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Sid }
     * 
     * 
     */
    public List<Sid> getSid() {
        if (sid == null) {
            sid = new ArrayList<Sid>();
        }
        return this.sid;
    }

    /**
     * Gets the value of the star property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the star property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStar().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Star }
     * 
     * 
     */
    public List<Star> getStar() {
        if (star == null) {
            star = new ArrayList<Star>();
        }
        return this.star;
    }

    /**
     * Gets the value of the approach property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the approach property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApproach().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Approach }
     * 
     * 
     */
    public List<Approach> getApproach() {
        if (approach == null) {
            approach = new ArrayList<Approach>();
        }
        return this.approach;
    }

}
