
package org.mitre.boogie.xml.v23_4.generated;

import java.math.BigInteger;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * 
 *                 The air-to-air y-band TACAN channel to be used 
 *                 by the tanker during air refueling operations. 
 *                 The tanker and receivers need to be 63 apart as 
 *                 in the receiver sets 29, the tanker sets 92.
 *                 Channel: 64-126
 *                 Mode: X or Y
 *             
 * 
 * <p>Java class for AirRefuelingTacanTanker complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AirRefuelingTacanTanker"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="channel" type="{}TankerTacanChannels"/&gt;
 *         &lt;element name="mode" type="{}TacanMode"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AirRefuelingTacanTanker", propOrder = {
    "channel",
    "mode"
})
public class AirRefuelingTacanTanker {

    @XmlElement(required = true)
    protected BigInteger channel;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TacanMode mode;

    /**
     * Gets the value of the channel property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getChannel() {
        return channel;
    }

    /**
     * Sets the value of the channel property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setChannel(BigInteger value) {
        this.channel = value;
    }

    /**
     * Gets the value of the mode property.
     * 
     * @return
     *     possible object is
     *     {@link TacanMode }
     *     
     */
    public TacanMode getMode() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value
     *     allowed object is
     *     {@link TacanMode }
     *     
     */
    public void setMode(TacanMode value) {
        this.mode = value;
    }

}
