
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following leg contains the fields used in Enroute Airway leg.
 * 
 * <p>Java class for AirwayLeg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirwayLeg"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Leg"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="airwayRouteType" type="{http://www.arinc424.com/xml/enumerations}EnrouteAirwayRouteType" minOccurs="0"/&gt;
 *         &lt;element name="qualifier1" type="{http://www.arinc424.com/xml/enumerations}AirwayQualifier1" minOccurs="0"/&gt;
 *         &lt;element name="qualifier2" type="{http://www.arinc424.com/xml/enumerations}AirwayQualifier2" minOccurs="0"/&gt;
 *         &lt;element name="rnavPbnNavSpec" type="{http://www.arinc424.com/xml/enumerations}AirwayRnavPbnNavSpec" minOccurs="0"/&gt;
 *         &lt;element name="rnpPbnNavSpec" type="{http://www.arinc424.com/xml/enumerations}AirwayRnpPbnNavSpec" minOccurs="0"/&gt;
 *         &lt;element name="cruiseTableRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
 *         &lt;element name="legDirectionRestriction" type="{http://www.arinc424.com/xml/enumerations}EnrouteAirwayDirectionalRestriction" minOccurs="0"/&gt;
 *         &lt;element name="routeDistanceFrom" type="{http://www.arinc424.com/xml/datatypes}LegDistance" minOccurs="0"/&gt;
 *         &lt;element name="euIndicator" type="{http://www.arinc424.com/xml/enumerations}EuIndicator" minOccurs="0"/&gt;
 *         &lt;element name="fixRadiusTransitionIndicator" type="{http://www.arinc424.com/xml/datatypes}FixedRadiusTransitionIndicator" minOccurs="0"/&gt;
 *         &lt;element name="inboundCourse" type="{http://www.arinc424.com/xml/datatypes}Course" minOccurs="0"/&gt;
 *         &lt;element name="waypointDescription" type="{http://www.arinc424.com/xml/datatypes}AirwayWaypointDescription" minOccurs="0"/&gt;
 *         &lt;element name="level" type="{http://www.arinc424.com/xml/enumerations}Level" minOccurs="0"/&gt;
 *         &lt;element name="rho" type="{http://www.arinc424.com/xml/datatypes}Rho" minOccurs="0"/&gt;
 *         &lt;element name="rnp" type="{http://www.arinc424.com/xml/datatypes}RequiredNavigationPerformance" minOccurs="0"/&gt;
 *         &lt;element name="rvsmMinMaxLevels" type="{http://www.arinc424.com/xml/datatypes}HoldRvsmMinimumMaximumAltitudeConstraint" minOccurs="0"/&gt;
 *         &lt;element name="theta" type="{http://www.arinc424.com/xml/datatypes}Theta" minOccurs="0"/&gt;
 *         &lt;element name="verticalScaleFactor" type="{http://www.arinc424.com/xml/datatypes}VerticalScaleFactor" minOccurs="0"/&gt;
 *         &lt;element name="outboundCourse" type="{http://www.arinc424.com/xml/datatypes}Course" minOccurs="0"/&gt;
 *         &lt;element name="minimumAltitudes" type="{http://www.arinc424.com/xml/datatypes}RouteMinimumAltitude" maxOccurs="2" minOccurs="0"/&gt;
 *         &lt;element name="maximumAltitudes" type="{http://www.arinc424.com/xml/datatypes}RouteMaximumAltitude" maxOccurs="2" minOccurs="0"/&gt;
 *         &lt;element name="restrictiveAirspaceRef" type="{}RestrictiveAirspaceReference" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirwayLeg", propOrder = {
    "airwayRouteType",
    "qualifier1",
    "qualifier2",
    "rnavPbnNavSpec",
    "rnpPbnNavSpec",
    "cruiseTableRef",
    "legDirectionRestriction",
    "routeDistanceFrom",
    "euIndicator",
    "fixRadiusTransitionIndicator",
    "inboundCourse",
    "waypointDescription",
    "level",
    "rho",
    "rnp",
    "rvsmMinMaxLevels",
    "theta",
    "verticalScaleFactor",
    "outboundCourse",
    "minimumAltitudes",
    "maximumAltitudes",
    "restrictiveAirspaceRef"
})
public class AirwayLeg
    extends Leg
{

    @XmlSchemaType(name = "string")
    protected EnrouteAirwayRouteType airwayRouteType;
    @XmlSchemaType(name = "string")
    protected AirwayQualifier1 qualifier1;
    @XmlSchemaType(name = "string")
    protected AirwayQualifier2 qualifier2;
    @XmlSchemaType(name = "string")
    protected AirwayRnavPbnNavSpec rnavPbnNavSpec;
    @XmlSchemaType(name = "string")
    protected AirwayRnpPbnNavSpec rnpPbnNavSpec;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object cruiseTableRef;
    @XmlSchemaType(name = "string")
    protected EnrouteAirwayDirectionalRestriction legDirectionRestriction;
    protected BigDecimal routeDistanceFrom;
    @XmlSchemaType(name = "string")
    protected EuIndicator euIndicator;
    protected BigDecimal fixRadiusTransitionIndicator;
    protected Course inboundCourse;
    protected AirwayWaypointDescription waypointDescription;
    @XmlSchemaType(name = "string")
    protected Level level;
    protected BigDecimal rho;
    protected BigDecimal rnp;
    protected HoldRvsmMinimumMaximumAltitudeConstraint rvsmMinMaxLevels;
    protected BigDecimal theta;
    @XmlSchemaType(name = "unsignedInt")
    protected Long verticalScaleFactor;
    protected Course outboundCourse;
    protected List<RouteMinimumAltitude> minimumAltitudes;
    protected List<RouteMaximumAltitude> maximumAltitudes;
    protected RestrictiveAirspaceReference restrictiveAirspaceRef;

    /**
     * Gets the value of the airwayRouteType property.
     * 
     * @return
     *     possible object is
     *     {@link EnrouteAirwayRouteType }
     *     
     */
    public EnrouteAirwayRouteType getAirwayRouteType() {
        return airwayRouteType;
    }

    /**
     * Sets the value of the airwayRouteType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnrouteAirwayRouteType }
     *     
     */
    public void setAirwayRouteType(EnrouteAirwayRouteType value) {
        this.airwayRouteType = value;
    }

    /**
     * Gets the value of the qualifier1 property.
     * 
     * @return
     *     possible object is
     *     {@link AirwayQualifier1 }
     *     
     */
    public AirwayQualifier1 getQualifier1() {
        return qualifier1;
    }

    /**
     * Sets the value of the qualifier1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirwayQualifier1 }
     *     
     */
    public void setQualifier1(AirwayQualifier1 value) {
        this.qualifier1 = value;
    }

    /**
     * Gets the value of the qualifier2 property.
     * 
     * @return
     *     possible object is
     *     {@link AirwayQualifier2 }
     *     
     */
    public AirwayQualifier2 getQualifier2() {
        return qualifier2;
    }

    /**
     * Sets the value of the qualifier2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirwayQualifier2 }
     *     
     */
    public void setQualifier2(AirwayQualifier2 value) {
        this.qualifier2 = value;
    }

    /**
     * Gets the value of the rnavPbnNavSpec property.
     * 
     * @return
     *     possible object is
     *     {@link AirwayRnavPbnNavSpec }
     *     
     */
    public AirwayRnavPbnNavSpec getRnavPbnNavSpec() {
        return rnavPbnNavSpec;
    }

    /**
     * Sets the value of the rnavPbnNavSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirwayRnavPbnNavSpec }
     *     
     */
    public void setRnavPbnNavSpec(AirwayRnavPbnNavSpec value) {
        this.rnavPbnNavSpec = value;
    }

    /**
     * Gets the value of the rnpPbnNavSpec property.
     * 
     * @return
     *     possible object is
     *     {@link AirwayRnpPbnNavSpec }
     *     
     */
    public AirwayRnpPbnNavSpec getRnpPbnNavSpec() {
        return rnpPbnNavSpec;
    }

    /**
     * Sets the value of the rnpPbnNavSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirwayRnpPbnNavSpec }
     *     
     */
    public void setRnpPbnNavSpec(AirwayRnpPbnNavSpec value) {
        this.rnpPbnNavSpec = value;
    }

    /**
     * Gets the value of the cruiseTableRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCruiseTableRef() {
        return cruiseTableRef;
    }

    /**
     * Sets the value of the cruiseTableRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCruiseTableRef(Object value) {
        this.cruiseTableRef = value;
    }

    /**
     * Gets the value of the legDirectionRestriction property.
     * 
     * @return
     *     possible object is
     *     {@link EnrouteAirwayDirectionalRestriction }
     *     
     */
    public EnrouteAirwayDirectionalRestriction getLegDirectionRestriction() {
        return legDirectionRestriction;
    }

    /**
     * Sets the value of the legDirectionRestriction property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnrouteAirwayDirectionalRestriction }
     *     
     */
    public void setLegDirectionRestriction(EnrouteAirwayDirectionalRestriction value) {
        this.legDirectionRestriction = value;
    }

    /**
     * Gets the value of the routeDistanceFrom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRouteDistanceFrom() {
        return routeDistanceFrom;
    }

    /**
     * Sets the value of the routeDistanceFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRouteDistanceFrom(BigDecimal value) {
        this.routeDistanceFrom = value;
    }

    /**
     * Gets the value of the euIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link EuIndicator }
     *     
     */
    public EuIndicator getEuIndicator() {
        return euIndicator;
    }

    /**
     * Sets the value of the euIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link EuIndicator }
     *     
     */
    public void setEuIndicator(EuIndicator value) {
        this.euIndicator = value;
    }

    /**
     * Gets the value of the fixRadiusTransitionIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFixRadiusTransitionIndicator() {
        return fixRadiusTransitionIndicator;
    }

    /**
     * Sets the value of the fixRadiusTransitionIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFixRadiusTransitionIndicator(BigDecimal value) {
        this.fixRadiusTransitionIndicator = value;
    }

    /**
     * Gets the value of the inboundCourse property.
     * 
     * @return
     *     possible object is
     *     {@link Course }
     *     
     */
    public Course getInboundCourse() {
        return inboundCourse;
    }

    /**
     * Sets the value of the inboundCourse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Course }
     *     
     */
    public void setInboundCourse(Course value) {
        this.inboundCourse = value;
    }

    /**
     * Gets the value of the waypointDescription property.
     * 
     * @return
     *     possible object is
     *     {@link AirwayWaypointDescription }
     *     
     */
    public AirwayWaypointDescription getWaypointDescription() {
        return waypointDescription;
    }

    /**
     * Sets the value of the waypointDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirwayWaypointDescription }
     *     
     */
    public void setWaypointDescription(AirwayWaypointDescription value) {
        this.waypointDescription = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link Level }
     *     
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link Level }
     *     
     */
    public void setLevel(Level value) {
        this.level = value;
    }

    /**
     * Gets the value of the rho property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRho() {
        return rho;
    }

    /**
     * Sets the value of the rho property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRho(BigDecimal value) {
        this.rho = value;
    }

    /**
     * Gets the value of the rnp property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRnp() {
        return rnp;
    }

    /**
     * Sets the value of the rnp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRnp(BigDecimal value) {
        this.rnp = value;
    }

    /**
     * Gets the value of the rvsmMinMaxLevels property.
     * 
     * @return
     *     possible object is
     *     {@link HoldRvsmMinimumMaximumAltitudeConstraint }
     *     
     */
    public HoldRvsmMinimumMaximumAltitudeConstraint getRvsmMinMaxLevels() {
        return rvsmMinMaxLevels;
    }

    /**
     * Sets the value of the rvsmMinMaxLevels property.
     * 
     * @param value
     *     allowed object is
     *     {@link HoldRvsmMinimumMaximumAltitudeConstraint }
     *     
     */
    public void setRvsmMinMaxLevels(HoldRvsmMinimumMaximumAltitudeConstraint value) {
        this.rvsmMinMaxLevels = value;
    }

    /**
     * Gets the value of the theta property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTheta() {
        return theta;
    }

    /**
     * Sets the value of the theta property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTheta(BigDecimal value) {
        this.theta = value;
    }

    /**
     * Gets the value of the verticalScaleFactor property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getVerticalScaleFactor() {
        return verticalScaleFactor;
    }

    /**
     * Sets the value of the verticalScaleFactor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setVerticalScaleFactor(Long value) {
        this.verticalScaleFactor = value;
    }

    /**
     * Gets the value of the outboundCourse property.
     * 
     * @return
     *     possible object is
     *     {@link Course }
     *     
     */
    public Course getOutboundCourse() {
        return outboundCourse;
    }

    /**
     * Sets the value of the outboundCourse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Course }
     *     
     */
    public void setOutboundCourse(Course value) {
        this.outboundCourse = value;
    }

    /**
     * Gets the value of the minimumAltitudes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the minimumAltitudes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMinimumAltitudes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RouteMinimumAltitude }
     * 
     * 
     */
    public List<RouteMinimumAltitude> getMinimumAltitudes() {
        if (minimumAltitudes == null) {
            minimumAltitudes = new ArrayList<RouteMinimumAltitude>();
        }
        return this.minimumAltitudes;
    }

    /**
     * Gets the value of the maximumAltitudes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the maximumAltitudes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMaximumAltitudes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RouteMaximumAltitude }
     * 
     * 
     */
    public List<RouteMaximumAltitude> getMaximumAltitudes() {
        if (maximumAltitudes == null) {
            maximumAltitudes = new ArrayList<RouteMaximumAltitude>();
        }
        return this.maximumAltitudes;
    }

    /**
     * Gets the value of the restrictiveAirspaceRef property.
     * 
     * @return
     *     possible object is
     *     {@link RestrictiveAirspaceReference }
     *     
     */
    public RestrictiveAirspaceReference getRestrictiveAirspaceRef() {
        return restrictiveAirspaceRef;
    }

    /**
     * Sets the value of the restrictiveAirspaceRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link RestrictiveAirspaceReference }
     *     
     */
    public void setRestrictiveAirspaceRef(RestrictiveAirspaceReference value) {
        this.restrictiveAirspaceRef = value;
    }

}
