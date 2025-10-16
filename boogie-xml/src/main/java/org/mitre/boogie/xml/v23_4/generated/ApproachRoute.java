
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * This abstract element represents of shared information between SID and STAR Enroute Transitions.
 * 
 * <p>Java class for ApproachRoute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApproachRoute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}ProcedureRoute"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="qualifier1" type="{http://www.arinc424.com/xml/enumerations}ApproachQualifier1" minOccurs="0"/&gt;
 *         &lt;element name="qualifier2" type="{http://www.arinc424.com/xml/enumerations}ApproachQualifier2" minOccurs="0"/&gt;
 *         &lt;element name="approachPbnNavSpec" type="{http://www.arinc424.com/xml/enumerations}ApproachPbnNavSpec" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApproachRoute", propOrder = {
    "qualifier1",
    "qualifier2",
    "approachPbnNavSpec"
})
@XmlSeeAlso({
    FinalApproach.class,
    ApproachTransition.class,
    MissedApproach.class
})
public abstract class ApproachRoute
    extends ProcedureRoute
{

    @XmlSchemaType(name = "string")
    protected ApproachQualifier1 qualifier1;
    @XmlSchemaType(name = "string")
    protected ApproachQualifier2 qualifier2;
    @XmlSchemaType(name = "string")
    protected ApproachPbnNavSpec approachPbnNavSpec;

    /**
     * Gets the value of the qualifier1 property.
     * 
     * @return
     *     possible object is
     *     {@link ApproachQualifier1 }
     *     
     */
    public ApproachQualifier1 getQualifier1() {
        return qualifier1;
    }

    /**
     * Sets the value of the qualifier1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApproachQualifier1 }
     *     
     */
    public void setQualifier1(ApproachQualifier1 value) {
        this.qualifier1 = value;
    }

    /**
     * Gets the value of the qualifier2 property.
     * 
     * @return
     *     possible object is
     *     {@link ApproachQualifier2 }
     *     
     */
    public ApproachQualifier2 getQualifier2() {
        return qualifier2;
    }

    /**
     * Sets the value of the qualifier2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApproachQualifier2 }
     *     
     */
    public void setQualifier2(ApproachQualifier2 value) {
        this.qualifier2 = value;
    }

    /**
     * Gets the value of the approachPbnNavSpec property.
     * 
     * @return
     *     possible object is
     *     {@link ApproachPbnNavSpec }
     *     
     */
    public ApproachPbnNavSpec getApproachPbnNavSpec() {
        return approachPbnNavSpec;
    }

    /**
     * Sets the value of the approachPbnNavSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApproachPbnNavSpec }
     *     
     */
    public void setApproachPbnNavSpec(ApproachPbnNavSpec value) {
        this.approachPbnNavSpec = value;
    }

}
