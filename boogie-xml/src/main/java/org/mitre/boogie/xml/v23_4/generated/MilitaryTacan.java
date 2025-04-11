
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MilitaryTacan complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MilitaryTacan"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}DmeTacan"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="stationDeclination" type="{http://www.arinc424.com/xml/datatypes}StationDeclination" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MilitaryTacan", propOrder = {
    "stationDeclination"
})
public class MilitaryTacan
    extends DmeTacan
{

    protected StationDeclination stationDeclination;

    /**
     * Gets the value of the stationDeclination property.
     * 
     * @return
     *     possible object is
     *     {@link StationDeclination }
     *     
     */
    public StationDeclination getStationDeclination() {
        return stationDeclination;
    }

    /**
     * Sets the value of the stationDeclination property.
     * 
     * @param value
     *     allowed object is
     *     {@link StationDeclination }
     *     
     */
    public void setStationDeclination(StationDeclination value) {
        this.stationDeclination = value;
    }

}
