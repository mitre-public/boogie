
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Ndb complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Ndb"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Navaid"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ndbClass" type="{http://www.arinc424.com/xml/datatypes}NdbNavaidClass"/&gt;
 *         &lt;element name="ndbFrequency" type="{http://www.arinc424.com/xml/datatypes}Frequency"/&gt;
 *         &lt;element name="typeOfEmission" type="{http://www.arinc424.com/xml/enumerations}TypeOfEmission" minOccurs="0"/&gt;
 *         &lt;element name="repetitionRate" type="{http://www.arinc424.com/xml/datatypes}RepetitionRate" minOccurs="0"/&gt;
 *         &lt;element name="navaidNdbEmissionType" type="{http://www.arinc424.com/xml/enumerations}NavaidNdbEmissionType" minOccurs="0"/&gt;
 *         &lt;element name="dmeTacanRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *         &lt;element name="ndbVoice" type="{http://www.arinc424.com/xml/enumerations}NavaidVoice" minOccurs="0"/&gt;
 *         &lt;element name="fraInfo" type="{http://www.arinc424.com/xml/datatypes}FraInfo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Ndb", propOrder = {
    "ndbClass",
    "ndbFrequency",
    "typeOfEmission",
    "repetitionRate",
    "navaidNdbEmissionType",
    "dmeTacanRef",
    "ndbVoice",
    "fraInfo"
})
@XmlSeeAlso({
    TerminalNdb.class
})
public class Ndb
    extends Navaid
{

    @XmlElement(required = true)
    protected NdbNavaidClass ndbClass;
    @XmlElement(required = true)
    protected Frequency ndbFrequency;
    @XmlSchemaType(name = "unsignedInt")
    protected Long typeOfEmission;
    @XmlSchemaType(name = "unsignedInt")
    protected Long repetitionRate;
    @XmlSchemaType(name = "string")
    protected NavaidNdbEmissionType navaidNdbEmissionType;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object dmeTacanRef;
    @XmlSchemaType(name = "string")
    protected NavaidVoice ndbVoice;
    protected FraInfo fraInfo;

    /**
     * Gets the value of the ndbClass property.
     * 
     * @return
     *     possible object is
     *     {@link NdbNavaidClass }
     *     
     */
    public NdbNavaidClass getNdbClass() {
        return ndbClass;
    }

    /**
     * Sets the value of the ndbClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link NdbNavaidClass }
     *     
     */
    public void setNdbClass(NdbNavaidClass value) {
        this.ndbClass = value;
    }

    /**
     * Gets the value of the ndbFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link Frequency }
     *     
     */
    public Frequency getNdbFrequency() {
        return ndbFrequency;
    }

    /**
     * Sets the value of the ndbFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frequency }
     *     
     */
    public void setNdbFrequency(Frequency value) {
        this.ndbFrequency = value;
    }

    /**
     * Gets the value of the typeOfEmission property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTypeOfEmission() {
        return typeOfEmission;
    }

    /**
     * Sets the value of the typeOfEmission property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTypeOfEmission(Long value) {
        this.typeOfEmission = value;
    }

    /**
     * Gets the value of the repetitionRate property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRepetitionRate() {
        return repetitionRate;
    }

    /**
     * Sets the value of the repetitionRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRepetitionRate(Long value) {
        this.repetitionRate = value;
    }

    /**
     * Gets the value of the navaidNdbEmissionType property.
     * 
     * @return
     *     possible object is
     *     {@link NavaidNdbEmissionType }
     *     
     */
    public NavaidNdbEmissionType getNavaidNdbEmissionType() {
        return navaidNdbEmissionType;
    }

    /**
     * Sets the value of the navaidNdbEmissionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link NavaidNdbEmissionType }
     *     
     */
    public void setNavaidNdbEmissionType(NavaidNdbEmissionType value) {
        this.navaidNdbEmissionType = value;
    }

    /**
     * Gets the value of the dmeTacanRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getDmeTacanRef() {
        return dmeTacanRef;
    }

    /**
     * Sets the value of the dmeTacanRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setDmeTacanRef(Object value) {
        this.dmeTacanRef = value;
    }

    /**
     * Gets the value of the ndbVoice property.
     * 
     * @return
     *     possible object is
     *     {@link NavaidVoice }
     *     
     */
    public NavaidVoice getNdbVoice() {
        return ndbVoice;
    }

    /**
     * Sets the value of the ndbVoice property.
     * 
     * @param value
     *     allowed object is
     *     {@link NavaidVoice }
     *     
     */
    public void setNdbVoice(NavaidVoice value) {
        this.ndbVoice = value;
    }

    /**
     * Gets the value of the fraInfo property.
     * 
     * @return
     *     possible object is
     *     {@link FraInfo }
     *     
     */
    public FraInfo getFraInfo() {
        return fraInfo;
    }

    /**
     * Sets the value of the fraInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link FraInfo }
     *     
     */
    public void setFraInfo(FraInfo value) {
        this.fraInfo = value;
    }

}
