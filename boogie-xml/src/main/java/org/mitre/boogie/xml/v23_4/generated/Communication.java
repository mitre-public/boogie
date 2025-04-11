
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Communication complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Communication"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}A424Record"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="location" type="{http://www.arinc424.com/xml/datatypes}Location" minOccurs="0"/&gt;
 *         &lt;element name="communicationAltitudes" type="{http://www.arinc424.com/xml/datatypes}AltitudeConstraint" minOccurs="0"/&gt;
 *         &lt;element name="callSign" type="{http://www.arinc424.com/xml/datatypes}CallSign" minOccurs="0"/&gt;
 *         &lt;element name="communicationClass" type="{http://www.arinc424.com/xml/enumerations}CommunicationClass" minOccurs="0"/&gt;
 *         &lt;element name="communicationType" type="{http://www.arinc424.com/xml/enumerations}CommunicationType" minOccurs="0"/&gt;
 *         &lt;element name="frequencyUnits" type="{http://www.arinc424.com/xml/enumerations}FrequencyUnits" minOccurs="0"/&gt;
 *         &lt;element name="radarService" type="{http://www.arinc424.com/xml/enumerations}Radar" minOccurs="0"/&gt;
 *         &lt;element name="h24Indicator" type="{http://www.arinc424.com/xml/enumerations}H24Indicator" minOccurs="0"/&gt;
 *         &lt;element name="modulation" type="{http://www.arinc424.com/xml/enumerations}Modulation" minOccurs="0"/&gt;
 *         &lt;element name="sequenceNumber" type="{http://www.arinc424.com/xml/datatypes}SequenceNumber"/&gt;
 *         &lt;element name="signalEmission" type="{http://www.arinc424.com/xml/enumerations}SignalEmission" minOccurs="0"/&gt;
 *         &lt;element name="transmitFrequency" type="{http://www.arinc424.com/xml/datatypes}Frequency" minOccurs="0"/&gt;
 *         &lt;element name="receiveFrequency" type="{http://www.arinc424.com/xml/datatypes}Frequency" minOccurs="0"/&gt;
 *         &lt;element name="remoteFacilityRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="timesOfOperation" type="{http://www.arinc424.com/xml/datatypes}TimesOfOperation" minOccurs="0"/&gt;
 *         &lt;element name="transmitterSiteElevation" type="{http://www.arinc424.com/xml/datatypes}FacilityElevation" minOccurs="0"/&gt;
 *         &lt;element name="transmitterSiteMagVar" type="{http://www.arinc424.com/xml/datatypes}MagneticVariation" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Communication", propOrder = {
    "location",
    "communicationAltitudes",
    "callSign",
    "communicationClass",
    "communicationType",
    "frequencyUnits",
    "radarService",
    "h24Indicator",
    "modulation",
    "sequenceNumber",
    "signalEmission",
    "transmitFrequency",
    "receiveFrequency",
    "remoteFacilityRef",
    "timesOfOperation",
    "transmitterSiteElevation",
    "transmitterSiteMagVar"
})
@XmlSeeAlso({
    PortCommunication.class,
    EnrouteCommunication.class
})
public abstract class Communication
    extends A424Record
{

    protected Location location;
    protected AltitudeConstraint communicationAltitudes;
    protected String callSign;
    @XmlSchemaType(name = "string")
    protected CommunicationClass communicationClass;
    @XmlSchemaType(name = "string")
    protected CommunicationType communicationType;
    @XmlSchemaType(name = "string")
    protected FrequencyUnits frequencyUnits;
    @XmlSchemaType(name = "string")
    protected Radar radarService;
    @XmlSchemaType(name = "string")
    protected H24Indicator h24Indicator;
    @XmlSchemaType(name = "string")
    protected Modulation modulation;
    @XmlSchemaType(name = "unsignedInt")
    protected long sequenceNumber;
    @XmlSchemaType(name = "string")
    protected SignalEmission signalEmission;
    protected Frequency transmitFrequency;
    protected Frequency receiveFrequency;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object remoteFacilityRef;
    protected TimesOfOperation timesOfOperation;
    protected Integer transmitterSiteElevation;
    protected MagneticVariation transmitterSiteMagVar;

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setLocation(Location value) {
        this.location = value;
    }

    /**
     * Gets the value of the communicationAltitudes property.
     * 
     * @return
     *     possible object is
     *     {@link AltitudeConstraint }
     *     
     */
    public AltitudeConstraint getCommunicationAltitudes() {
        return communicationAltitudes;
    }

    /**
     * Sets the value of the communicationAltitudes property.
     * 
     * @param value
     *     allowed object is
     *     {@link AltitudeConstraint }
     *     
     */
    public void setCommunicationAltitudes(AltitudeConstraint value) {
        this.communicationAltitudes = value;
    }

    /**
     * Gets the value of the callSign property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallSign() {
        return callSign;
    }

    /**
     * Sets the value of the callSign property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallSign(String value) {
        this.callSign = value;
    }

    /**
     * Gets the value of the communicationClass property.
     * 
     * @return
     *     possible object is
     *     {@link CommunicationClass }
     *     
     */
    public CommunicationClass getCommunicationClass() {
        return communicationClass;
    }

    /**
     * Sets the value of the communicationClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommunicationClass }
     *     
     */
    public void setCommunicationClass(CommunicationClass value) {
        this.communicationClass = value;
    }

    /**
     * Gets the value of the communicationType property.
     * 
     * @return
     *     possible object is
     *     {@link CommunicationType }
     *     
     */
    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    /**
     * Sets the value of the communicationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommunicationType }
     *     
     */
    public void setCommunicationType(CommunicationType value) {
        this.communicationType = value;
    }

    /**
     * Gets the value of the frequencyUnits property.
     * 
     * @return
     *     possible object is
     *     {@link FrequencyUnits }
     *     
     */
    public FrequencyUnits getFrequencyUnits() {
        return frequencyUnits;
    }

    /**
     * Sets the value of the frequencyUnits property.
     * 
     * @param value
     *     allowed object is
     *     {@link FrequencyUnits }
     *     
     */
    public void setFrequencyUnits(FrequencyUnits value) {
        this.frequencyUnits = value;
    }

    /**
     * Gets the value of the radarService property.
     * 
     * @return
     *     possible object is
     *     {@link Radar }
     *     
     */
    public Radar getRadarService() {
        return radarService;
    }

    /**
     * Sets the value of the radarService property.
     * 
     * @param value
     *     allowed object is
     *     {@link Radar }
     *     
     */
    public void setRadarService(Radar value) {
        this.radarService = value;
    }

    /**
     * Gets the value of the h24Indicator property.
     * 
     * @return
     *     possible object is
     *     {@link H24Indicator }
     *     
     */
    public H24Indicator getH24Indicator() {
        return h24Indicator;
    }

    /**
     * Sets the value of the h24Indicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link H24Indicator }
     *     
     */
    public void setH24Indicator(H24Indicator value) {
        this.h24Indicator = value;
    }

    /**
     * Gets the value of the modulation property.
     * 
     * @return
     *     possible object is
     *     {@link Modulation }
     *     
     */
    public Modulation getModulation() {
        return modulation;
    }

    /**
     * Sets the value of the modulation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Modulation }
     *     
     */
    public void setModulation(Modulation value) {
        this.modulation = value;
    }

    /**
     * Gets the value of the sequenceNumber property.
     * 
     */
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the value of the sequenceNumber property.
     * 
     */
    public void setSequenceNumber(long value) {
        this.sequenceNumber = value;
    }

    /**
     * Gets the value of the signalEmission property.
     * 
     * @return
     *     possible object is
     *     {@link SignalEmission }
     *     
     */
    public SignalEmission getSignalEmission() {
        return signalEmission;
    }

    /**
     * Sets the value of the signalEmission property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignalEmission }
     *     
     */
    public void setSignalEmission(SignalEmission value) {
        this.signalEmission = value;
    }

    /**
     * Gets the value of the transmitFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Frequency }
     *     
     */
    public Frequency getTransmitFrequency() {
        return transmitFrequency;
    }

    /**
     * Sets the value of the transmitFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frequency }
     *     
     */
    public void setTransmitFrequency(Frequency value) {
        this.transmitFrequency = value;
    }

    /**
     * Gets the value of the receiveFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Frequency }
     *     
     */
    public Frequency getReceiveFrequency() {
        return receiveFrequency;
    }

    /**
     * Sets the value of the receiveFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frequency }
     *     
     */
    public void setReceiveFrequency(Frequency value) {
        this.receiveFrequency = value;
    }

    /**
     * Gets the value of the remoteFacilityRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRemoteFacilityRef() {
        return remoteFacilityRef;
    }

    /**
     * Sets the value of the remoteFacilityRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRemoteFacilityRef(Object value) {
        this.remoteFacilityRef = value;
    }

    /**
     * Gets the value of the timesOfOperation property.
     * 
     * @return
     *     possible object is
     *     {@link TimesOfOperation }
     *     
     */
    public TimesOfOperation getTimesOfOperation() {
        return timesOfOperation;
    }

    /**
     * Sets the value of the timesOfOperation property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimesOfOperation }
     *     
     */
    public void setTimesOfOperation(TimesOfOperation value) {
        this.timesOfOperation = value;
    }

    /**
     * Gets the value of the transmitterSiteElevation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTransmitterSiteElevation() {
        return transmitterSiteElevation;
    }

    /**
     * Sets the value of the transmitterSiteElevation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTransmitterSiteElevation(Integer value) {
        this.transmitterSiteElevation = value;
    }

    /**
     * Gets the value of the transmitterSiteMagVar property.
     * 
     * @return
     *     possible object is
     *     {@link MagneticVariation }
     *     
     */
    public MagneticVariation getTransmitterSiteMagVar() {
        return transmitterSiteMagVar;
    }

    /**
     * Sets the value of the transmitterSiteMagVar property.
     * 
     * @param value
     *     allowed object is
     *     {@link MagneticVariation }
     *     
     */
    public void setTransmitterSiteMagVar(MagneticVariation value) {
        this.transmitterSiteMagVar = value;
    }

}
