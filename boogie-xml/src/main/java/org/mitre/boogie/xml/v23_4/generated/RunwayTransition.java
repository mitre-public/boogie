
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This abstract element represents of shared information between SID and STAR Runway Transitions.
 * 
 * <p>Java class for RunwayTransition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RunwayTransition"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}ProcedureRoute"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="routeQualifications" type="{http://www.arinc424.com/xml/datatypes}RouteQualifications" minOccurs="0"/&gt;
 *         &lt;element name="rnavPbnNavSpec" type="{http://www.arinc424.com/xml/enumerations}RnavPbnNavSpec" minOccurs="0"/&gt;
 *         &lt;element name="rnpPbnNavSpec" type="{http://www.arinc424.com/xml/enumerations}RnpPbnNavSpec" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RunwayTransition", propOrder = {
    "routeQualifications",
    "rnavPbnNavSpec",
    "rnpPbnNavSpec"
})
@XmlSeeAlso({
    SidRunwayTransition.class,
    StarRunwayTransition.class
})
public abstract class RunwayTransition
    extends ProcedureRoute
{

    protected RouteQualifications routeQualifications;
    @XmlSchemaType(name = "string")
    protected RnavPbnNavSpec rnavPbnNavSpec;
    @XmlSchemaType(name = "string")
    protected RnpPbnNavSpec rnpPbnNavSpec;

    /**
     * Gets the value of the routeQualifications property.
     * 
     * @return
     *     possible object is
     *     {@link RouteQualifications }
     *     
     */
    public RouteQualifications getRouteQualifications() {
        return routeQualifications;
    }

    /**
     * Sets the value of the routeQualifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link RouteQualifications }
     *     
     */
    public void setRouteQualifications(RouteQualifications value) {
        this.routeQualifications = value;
    }

    /**
     * Gets the value of the rnavPbnNavSpec property.
     * 
     * @return
     *     possible object is
     *     {@link RnavPbnNavSpec }
     *     
     */
    public RnavPbnNavSpec getRnavPbnNavSpec() {
        return rnavPbnNavSpec;
    }

    /**
     * Sets the value of the rnavPbnNavSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link RnavPbnNavSpec }
     *     
     */
    public void setRnavPbnNavSpec(RnavPbnNavSpec value) {
        this.rnavPbnNavSpec = value;
    }

    /**
     * Gets the value of the rnpPbnNavSpec property.
     * 
     * @return
     *     possible object is
     *     {@link RnpPbnNavSpec }
     *     
     */
    public RnpPbnNavSpec getRnpPbnNavSpec() {
        return rnpPbnNavSpec;
    }

    /**
     * Sets the value of the rnpPbnNavSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link RnpPbnNavSpec }
     *     
     */
    public void setRnpPbnNavSpec(RnpPbnNavSpec value) {
        this.rnpPbnNavSpec = value;
    }

}
