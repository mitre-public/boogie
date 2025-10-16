
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This type represents the details of an intermediate Fix
 * 
 * <p>Java class for IntermediateFixDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IntermediateFixDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fixRelatedTransitionCode" type="{http://www.arinc424.com/xml/enumerations}FixRelatedTransitionCode"/&gt;
 *         &lt;element name="intermediateDistance" type="{http://www.arinc424.com/xml/datatypes}AlongTrackDistance"/&gt;
 *         &lt;element name="fixRef" type="{http://www.arinc424.com/xml/datatypes}PointReference" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IntermediateFixDetails", namespace = "http://www.arinc424.com/xml/datatypes", propOrder = {
    "fixRelatedTransitionCode",
    "intermediateDistance",
    "fixRef"
})
public class IntermediateFixDetails {

    @XmlSchemaType(name = "unsignedInt")
    protected long fixRelatedTransitionCode;
    @XmlSchemaType(name = "unsignedInt")
    protected long intermediateDistance;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object fixRef;

    /**
     * Gets the value of the fixRelatedTransitionCode property.
     * 
     */
    public long getFixRelatedTransitionCode() {
        return fixRelatedTransitionCode;
    }

    /**
     * Sets the value of the fixRelatedTransitionCode property.
     * 
     */
    public void setFixRelatedTransitionCode(long value) {
        this.fixRelatedTransitionCode = value;
    }

    /**
     * Gets the value of the intermediateDistance property.
     * 
     */
    public long getIntermediateDistance() {
        return intermediateDistance;
    }

    /**
     * Sets the value of the intermediateDistance property.
     * 
     */
    public void setIntermediateDistance(long value) {
        this.intermediateDistance = value;
    }

    /**
     * Gets the value of the fixRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFixRef() {
        return fixRef;
    }

    /**
     * Sets the value of the fixRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFixRef(Object value) {
        this.fixRef = value;
    }

}
