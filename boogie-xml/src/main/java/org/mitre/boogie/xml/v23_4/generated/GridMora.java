
package org.mitre.boogie.xml.v23_4.generated;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This following record contains the fields used in Grid Mora Record.  The Grid MORA table will contain records describing the MORA for each Latitude and Longitude block. Each record will contain thirty blocks and the “Starting Longitude” field defines the lower left corner for the first block of each record.  Grid MORA values clear all terrain and obstructions by 1000 feet in areas where the highest elevations are 5000 feet MSL or lower. MORA values clear all terrain by 2000 feet in areas where the highest elevations are 5001 feet MSL or higher.
 * 
 * <p>Java class for GridMora complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GridMora"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="startingPosition" type="{http://www.arinc424.com/xml/datatypes}Location"/&gt;
 *         &lt;element name="mora" type="{http://www.arinc424.com/xml/datatypes}Mora" maxOccurs="30" minOccurs="30"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GridMora", propOrder = {
    "startingPosition",
    "mora"
})
public class GridMora
    extends A424Record
{

    @XmlElement(required = true)
    protected Location startingPosition;
    @XmlElement(required = true)
    protected List<Mora> mora;

    /**
     * Gets the value of the startingPosition property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getStartingPosition() {
        return startingPosition;
    }

    /**
     * Sets the value of the startingPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setStartingPosition(Location value) {
        this.startingPosition = value;
    }

    /**
     * Gets the value of the mora property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the mora property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMora().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Mora }
     * 
     * 
     */
    public List<Mora> getMora() {
        if (mora == null) {
            mora = new ArrayList<Mora>();
        }
        return this.mora;
    }

}
