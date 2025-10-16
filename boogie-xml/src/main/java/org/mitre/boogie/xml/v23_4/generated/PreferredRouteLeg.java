
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementRefs;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Preferred Route Leg.
 * 
 * <p>Java class for PreferredRouteLeg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PreferredRouteLeg"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Leg"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="aircraftUsageGroup" type="{http://www.arinc424.com/xml/datatypes}AircraftUsageGroup" minOccurs="0"/&gt;
 *         &lt;element name="areaCode" type="{http://www.arinc424.com/xml/enumerations}AreaCode" minOccurs="0"/&gt;
 *         &lt;element name="legDirectionRestriction" type="{http://www.arinc424.com/xml/enumerations}PreferredRouteDirectionalRestriction" minOccurs="0"/&gt;
 *         &lt;element name="initialAirportFix" type="{http://www.arinc424.com/xml/datatypes}InitialTerminusAirportFix" minOccurs="0"/&gt;
 *         &lt;element name="level" type="{http://www.arinc424.com/xml/enumerations}Level" minOccurs="0"/&gt;
 *         &lt;element name="minimumMaximumAltitudes" type="{http://www.arinc424.com/xml/datatypes}RouteMinimumMaximumAltitudeConstraint" minOccurs="0"/&gt;
 *         &lt;element name="preferredRouteUseIndicator" type="{http://www.arinc424.com/xml/datatypes}PreferredRouteUseIndicator"/&gt;
 *         &lt;element name="routeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="terminusAirportFix" type="{http://www.arinc424.com/xml/datatypes}InitialTerminusAirportFix" minOccurs="0"/&gt;
 *         &lt;element name="fixAltitudeConstraint" type="{http://www.arinc424.com/xml/datatypes}AltitudeConstraint" minOccurs="0"/&gt;
 *         &lt;element name="viaCode" type="{http://www.arinc424.com/xml/enumerations}PreferredRouteViaCodes" minOccurs="0"/&gt;
 *         &lt;element name="airwayRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
 *         &lt;element name="procedureRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/&gt;
 *         &lt;element name="initialAirportFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="toTerminusFixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PreferredRouteLeg", propOrder = {
    "rest"
})
public class PreferredRouteLeg
    extends Leg
{

    @XmlElementRefs({
        @XmlElementRef(name = "aircraftUsageGroup", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "areaCode", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "legDirectionRestriction", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "initialAirportFix", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "level", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "minimumMaximumAltitudes", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "preferredRouteUseIndicator", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "routeType", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "terminusAirportFix", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "fixAltitudeConstraint", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "viaCode", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "airwayRef", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "procedureRef", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "initialAirportFixRef", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "toTerminusFixRef", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> rest;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "AreaCode" is used by two different parts of a schema. See: 
     * line 116 of file:/Users/dbaker/git_public/boogie/boogie-jaxb/src/main/resources/supplement23_4/Records/Legs.xsd
     * line 37 of file:/Users/dbaker/git_public/boogie/boogie-jaxb/src/main/resources/supplement23_4/Records/BasicClasses.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link AircraftUsageGroup }{@code >}
     * {@link JAXBElement }{@code <}{@link AltitudeConstraint }{@code >}
     * {@link JAXBElement }{@code <}{@link AreaCode }{@code >}
     * {@link JAXBElement }{@code <}{@link Level }{@code >}
     * {@link JAXBElement }{@code <}{@link PreferredRouteDirectionalRestriction }{@code >}
     * {@link JAXBElement }{@code <}{@link PreferredRouteUseIndicator }{@code >}
     * {@link JAXBElement }{@code <}{@link PreferredRouteViaCodes }{@code >}
     * {@link JAXBElement }{@code <}{@link RouteMinimumMaximumAltitudeConstraint }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getRest() {
        if (rest == null) {
            rest = new ArrayList<JAXBElement<?>>();
        }
        return this.rest;
    }

}
