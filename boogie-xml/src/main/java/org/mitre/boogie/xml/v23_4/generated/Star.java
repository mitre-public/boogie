
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This class is an abstraction of STAR. STAR records are represented by this class.
 * 
 * <p>Java class for Star complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Star"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}SidStar"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="starRunwayTransition" type="{}StarRunwayTransition" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="starEnrouteTransition" type="{}StarEnrouteTransition" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="starCommonRoute" type="{}StarCommonRoute" minOccurs="0"/&gt;
 *         &lt;element name="isContinuousDescent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Star", propOrder = {
    "starRunwayTransition",
    "starEnrouteTransition",
    "starCommonRoute",
    "isContinuousDescent"
})
public class Star
    extends SidStar
{

    protected List<StarRunwayTransition> starRunwayTransition;
    protected List<StarEnrouteTransition> starEnrouteTransition;
    protected StarCommonRoute starCommonRoute;
    @XmlElement(defaultValue = "false")
    protected Boolean isContinuousDescent;

    /**
     * Gets the value of the starRunwayTransition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the starRunwayTransition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStarRunwayTransition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StarRunwayTransition }
     * 
     * 
     */
    public List<StarRunwayTransition> getStarRunwayTransition() {
        if (starRunwayTransition == null) {
            starRunwayTransition = new ArrayList<StarRunwayTransition>();
        }
        return this.starRunwayTransition;
    }

    /**
     * Gets the value of the starEnrouteTransition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the starEnrouteTransition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStarEnrouteTransition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StarEnrouteTransition }
     * 
     * 
     */
    public List<StarEnrouteTransition> getStarEnrouteTransition() {
        if (starEnrouteTransition == null) {
            starEnrouteTransition = new ArrayList<StarEnrouteTransition>();
        }
        return this.starEnrouteTransition;
    }

    /**
     * Gets the value of the starCommonRoute property.
     * 
     * @return
     *     possible object is
     *     {@link StarCommonRoute }
     *     
     */
    public StarCommonRoute getStarCommonRoute() {
        return starCommonRoute;
    }

    /**
     * Sets the value of the starCommonRoute property.
     * 
     * @param value
     *     allowed object is
     *     {@link StarCommonRoute }
     *     
     */
    public void setStarCommonRoute(StarCommonRoute value) {
        this.starCommonRoute = value;
    }

    /**
     * Gets the value of the isContinuousDescent property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsContinuousDescent() {
        return isContinuousDescent;
    }

    /**
     * Sets the value of the isContinuousDescent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsContinuousDescent(Boolean value) {
        this.isContinuousDescent = value;
    }

}
