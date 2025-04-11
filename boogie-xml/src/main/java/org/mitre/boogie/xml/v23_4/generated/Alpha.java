
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This Class represents the "Alpha" character type of the Arinc 424 data.
 * 
 * <p>Java class for Alpha complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Alpha"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Alpha", namespace = "http://www.arinc424.com/xml/datatypes")
@XmlSeeAlso({
    ServiceIndicator.class,
    AltitudeLimitation.class,
    PreferredRouteUseIndicator.class,
    AircraftUsageGroup.class,
    LegTypeCode.class,
    SpecialActivityAreaOperatingTimes.class
})
public class Alpha {


}
