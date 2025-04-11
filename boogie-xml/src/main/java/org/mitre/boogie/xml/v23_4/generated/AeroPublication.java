
package org.mitre.boogie.xml.v23_4.generated;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="airports" type="{}Airports" minOccurs="0"/&gt;
 *         &lt;element name="heliports" type="{}Heliports" minOccurs="0"/&gt;
 *         &lt;element name="airways" type="{}Airways" minOccurs="0"/&gt;
 *         &lt;element name="enrouteWaypoints" type="{}EnrouteWaypoints" minOccurs="0"/&gt;
 *         &lt;element name="vhfNavaids" type="{}Navaids" minOccurs="0"/&gt;
 *         &lt;element name="enrouteNdbs" type="{}EnrouteNdbs" minOccurs="0"/&gt;
 *         &lt;element name="companyRoutes" type="{}CompanyRoutes" minOccurs="0"/&gt;
 *         &lt;element name="airspaces" type="{}Airspaces" minOccurs="0"/&gt;
 *         &lt;element name="alternates" type="{}Alternates" minOccurs="0"/&gt;
 *         &lt;element name="cruisingTables" type="{}CruisingTables" minOccurs="0"/&gt;
 *         &lt;element name="preferredRoutes" type="{}PreferredRoutes" minOccurs="0"/&gt;
 *         &lt;element name="enrouteCommunications" type="{}EnrouteCommunications" minOccurs="0"/&gt;
 *         &lt;element name="geographicalReferences" type="{}GeographicalReferences" minOccurs="0"/&gt;
 *         &lt;element name="gridMoras" type="{}GridMoras" minOccurs="0"/&gt;
 *         &lt;element name="supplementalData" type="{http://www.arinc424.com/xml/datatypes}SupplementalData" minOccurs="0"/&gt;
 *         &lt;element name="holdingPatterns" type="{}HoldingPatterns" minOccurs="0"/&gt;
 *         &lt;element name="aeroTelecomNetworks" type="{}AeroTelecomNetworks" minOccurs="0"/&gt;
 *         &lt;element name="airRefuelingRoutes" type="{}AirRefuelingRoutes" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{}MetaData"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "airports",
    "heliports",
    "airways",
    "enrouteWaypoints",
    "vhfNavaids",
    "enrouteNdbs",
    "companyRoutes",
    "airspaces",
    "alternates",
    "cruisingTables",
    "preferredRoutes",
    "enrouteCommunications",
    "geographicalReferences",
    "gridMoras",
    "supplementalData",
    "holdingPatterns",
    "aeroTelecomNetworks",
    "airRefuelingRoutes"
})
@XmlRootElement(name = "AeroPublication")
public class AeroPublication {

    protected Airports airports;
    protected Heliports heliports;
    protected Airways airways;
    protected EnrouteWaypoints enrouteWaypoints;
    protected Navaids vhfNavaids;
    protected EnrouteNdbs enrouteNdbs;
    protected CompanyRoutes companyRoutes;
    protected Airspaces airspaces;
    protected Alternates alternates;
    protected CruisingTables cruisingTables;
    protected PreferredRoutes preferredRoutes;
    protected EnrouteCommunications enrouteCommunications;
    protected GeographicalReferences geographicalReferences;
    protected GridMoras gridMoras;
    protected SupplementalData supplementalData;
    protected HoldingPatterns holdingPatterns;
    protected AeroTelecomNetworks aeroTelecomNetworks;
    protected AirRefuelingRoutes airRefuelingRoutes;
    @XmlAttribute(name = "creationDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creationDateTime;
    @XmlAttribute(name = "cycleDate")
    protected Integer cycleDate;
    @XmlAttribute(name = "databasePartNumber")
    protected String databasePartNumber;
    @XmlAttribute(name = "dataSupplierIdent")
    protected String dataSupplierIdent;
    @XmlAttribute(name = "dataSupplierTextField")
    protected String dataSupplierTextField;
    @XmlAttribute(name = "endOfValidity", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endOfValidity;
    @XmlAttribute(name = "fileName")
    protected String fileName;
    @XmlAttribute(name = "fileCrc")
    protected String fileCrc;
    @XmlAttribute(name = "isTestDataset")
    protected Boolean isTestDataset;
    @XmlAttribute(name = "language")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "language")
    protected String language;
    @XmlAttribute(name = "recordCount")
    protected Integer recordCount;
    @XmlAttribute(name = "startOfValidity", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar startOfValidity;
    @XmlAttribute(name = "supplierTextField")
    protected String supplierTextField;
    @XmlAttribute(name = "targetCustomerIdent")
    protected String targetCustomerIdent;
    @XmlAttribute(name = "uuid")
    protected String uuid;
    @XmlAttribute(name = "versionNumber")
    protected Integer versionNumber;
    @XmlAttribute(name = "codingRuleVersion")
    protected String codingRuleVersion;
    @XmlAttribute(name = "arinc424SchemaVersion")
    protected String arinc424SchemaVersion;
    @XmlAttribute(name = "arinc424Supplement")
    protected String arinc424Supplement;

    /**
     * Gets the value of the airports property.
     * 
     * @return
     *     possible object is
     *     {@link Airports }
     *     
     */
    public Airports getAirports() {
        return airports;
    }

    /**
     * Sets the value of the airports property.
     * 
     * @param value
     *     allowed object is
     *     {@link Airports }
     *     
     */
    public void setAirports(Airports value) {
        this.airports = value;
    }

    /**
     * Gets the value of the heliports property.
     * 
     * @return
     *     possible object is
     *     {@link Heliports }
     *     
     */
    public Heliports getHeliports() {
        return heliports;
    }

    /**
     * Sets the value of the heliports property.
     * 
     * @param value
     *     allowed object is
     *     {@link Heliports }
     *     
     */
    public void setHeliports(Heliports value) {
        this.heliports = value;
    }

    /**
     * Gets the value of the airways property.
     * 
     * @return
     *     possible object is
     *     {@link Airways }
     *     
     */
    public Airways getAirways() {
        return airways;
    }

    /**
     * Sets the value of the airways property.
     * 
     * @param value
     *     allowed object is
     *     {@link Airways }
     *     
     */
    public void setAirways(Airways value) {
        this.airways = value;
    }

    /**
     * Gets the value of the enrouteWaypoints property.
     * 
     * @return
     *     possible object is
     *     {@link EnrouteWaypoints }
     *     
     */
    public EnrouteWaypoints getEnrouteWaypoints() {
        return enrouteWaypoints;
    }

    /**
     * Sets the value of the enrouteWaypoints property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnrouteWaypoints }
     *     
     */
    public void setEnrouteWaypoints(EnrouteWaypoints value) {
        this.enrouteWaypoints = value;
    }

    /**
     * Gets the value of the vhfNavaids property.
     * 
     * @return
     *     possible object is
     *     {@link Navaids }
     *     
     */
    public Navaids getVhfNavaids() {
        return vhfNavaids;
    }

    /**
     * Sets the value of the vhfNavaids property.
     * 
     * @param value
     *     allowed object is
     *     {@link Navaids }
     *     
     */
    public void setVhfNavaids(Navaids value) {
        this.vhfNavaids = value;
    }

    /**
     * Gets the value of the enrouteNdbs property.
     * 
     * @return
     *     possible object is
     *     {@link EnrouteNdbs }
     *     
     */
    public EnrouteNdbs getEnrouteNdbs() {
        return enrouteNdbs;
    }

    /**
     * Sets the value of the enrouteNdbs property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnrouteNdbs }
     *     
     */
    public void setEnrouteNdbs(EnrouteNdbs value) {
        this.enrouteNdbs = value;
    }

    /**
     * Gets the value of the companyRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link CompanyRoutes }
     *     
     */
    public CompanyRoutes getCompanyRoutes() {
        return companyRoutes;
    }

    /**
     * Sets the value of the companyRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompanyRoutes }
     *     
     */
    public void setCompanyRoutes(CompanyRoutes value) {
        this.companyRoutes = value;
    }

    /**
     * Gets the value of the airspaces property.
     * 
     * @return
     *     possible object is
     *     {@link Airspaces }
     *     
     */
    public Airspaces getAirspaces() {
        return airspaces;
    }

    /**
     * Sets the value of the airspaces property.
     * 
     * @param value
     *     allowed object is
     *     {@link Airspaces }
     *     
     */
    public void setAirspaces(Airspaces value) {
        this.airspaces = value;
    }

    /**
     * Gets the value of the alternates property.
     * 
     * @return
     *     possible object is
     *     {@link Alternates }
     *     
     */
    public Alternates getAlternates() {
        return alternates;
    }

    /**
     * Sets the value of the alternates property.
     * 
     * @param value
     *     allowed object is
     *     {@link Alternates }
     *     
     */
    public void setAlternates(Alternates value) {
        this.alternates = value;
    }

    /**
     * Gets the value of the cruisingTables property.
     * 
     * @return
     *     possible object is
     *     {@link CruisingTables }
     *     
     */
    public CruisingTables getCruisingTables() {
        return cruisingTables;
    }

    /**
     * Sets the value of the cruisingTables property.
     * 
     * @param value
     *     allowed object is
     *     {@link CruisingTables }
     *     
     */
    public void setCruisingTables(CruisingTables value) {
        this.cruisingTables = value;
    }

    /**
     * Gets the value of the preferredRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link PreferredRoutes }
     *     
     */
    public PreferredRoutes getPreferredRoutes() {
        return preferredRoutes;
    }

    /**
     * Sets the value of the preferredRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link PreferredRoutes }
     *     
     */
    public void setPreferredRoutes(PreferredRoutes value) {
        this.preferredRoutes = value;
    }

    /**
     * Gets the value of the enrouteCommunications property.
     * 
     * @return
     *     possible object is
     *     {@link EnrouteCommunications }
     *     
     */
    public EnrouteCommunications getEnrouteCommunications() {
        return enrouteCommunications;
    }

    /**
     * Sets the value of the enrouteCommunications property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnrouteCommunications }
     *     
     */
    public void setEnrouteCommunications(EnrouteCommunications value) {
        this.enrouteCommunications = value;
    }

    /**
     * Gets the value of the geographicalReferences property.
     * 
     * @return
     *     possible object is
     *     {@link GeographicalReferences }
     *     
     */
    public GeographicalReferences getGeographicalReferences() {
        return geographicalReferences;
    }

    /**
     * Sets the value of the geographicalReferences property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeographicalReferences }
     *     
     */
    public void setGeographicalReferences(GeographicalReferences value) {
        this.geographicalReferences = value;
    }

    /**
     * Gets the value of the gridMoras property.
     * 
     * @return
     *     possible object is
     *     {@link GridMoras }
     *     
     */
    public GridMoras getGridMoras() {
        return gridMoras;
    }

    /**
     * Sets the value of the gridMoras property.
     * 
     * @param value
     *     allowed object is
     *     {@link GridMoras }
     *     
     */
    public void setGridMoras(GridMoras value) {
        this.gridMoras = value;
    }

    /**
     * Gets the value of the supplementalData property.
     * 
     * @return
     *     possible object is
     *     {@link SupplementalData }
     *     
     */
    public SupplementalData getSupplementalData() {
        return supplementalData;
    }

    /**
     * Sets the value of the supplementalData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupplementalData }
     *     
     */
    public void setSupplementalData(SupplementalData value) {
        this.supplementalData = value;
    }

    /**
     * Gets the value of the holdingPatterns property.
     * 
     * @return
     *     possible object is
     *     {@link HoldingPatterns }
     *     
     */
    public HoldingPatterns getHoldingPatterns() {
        return holdingPatterns;
    }

    /**
     * Sets the value of the holdingPatterns property.
     * 
     * @param value
     *     allowed object is
     *     {@link HoldingPatterns }
     *     
     */
    public void setHoldingPatterns(HoldingPatterns value) {
        this.holdingPatterns = value;
    }

    /**
     * Gets the value of the aeroTelecomNetworks property.
     * 
     * @return
     *     possible object is
     *     {@link AeroTelecomNetworks }
     *     
     */
    public AeroTelecomNetworks getAeroTelecomNetworks() {
        return aeroTelecomNetworks;
    }

    /**
     * Sets the value of the aeroTelecomNetworks property.
     * 
     * @param value
     *     allowed object is
     *     {@link AeroTelecomNetworks }
     *     
     */
    public void setAeroTelecomNetworks(AeroTelecomNetworks value) {
        this.aeroTelecomNetworks = value;
    }

    /**
     * Gets the value of the airRefuelingRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link AirRefuelingRoutes }
     *     
     */
    public AirRefuelingRoutes getAirRefuelingRoutes() {
        return airRefuelingRoutes;
    }

    /**
     * Sets the value of the airRefuelingRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link AirRefuelingRoutes }
     *     
     */
    public void setAirRefuelingRoutes(AirRefuelingRoutes value) {
        this.airRefuelingRoutes = value;
    }

    /**
     * Gets the value of the creationDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreationDateTime() {
        return creationDateTime;
    }

    /**
     * Sets the value of the creationDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreationDateTime(XMLGregorianCalendar value) {
        this.creationDateTime = value;
    }

    /**
     * Gets the value of the cycleDate property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCycleDate() {
        return cycleDate;
    }

    /**
     * Sets the value of the cycleDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCycleDate(Integer value) {
        this.cycleDate = value;
    }

    /**
     * Gets the value of the databasePartNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatabasePartNumber() {
        return databasePartNumber;
    }

    /**
     * Sets the value of the databasePartNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatabasePartNumber(String value) {
        this.databasePartNumber = value;
    }

    /**
     * Gets the value of the dataSupplierIdent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSupplierIdent() {
        return dataSupplierIdent;
    }

    /**
     * Sets the value of the dataSupplierIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSupplierIdent(String value) {
        this.dataSupplierIdent = value;
    }

    /**
     * Gets the value of the dataSupplierTextField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSupplierTextField() {
        return dataSupplierTextField;
    }

    /**
     * Sets the value of the dataSupplierTextField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSupplierTextField(String value) {
        this.dataSupplierTextField = value;
    }

    /**
     * Gets the value of the endOfValidity property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndOfValidity() {
        return endOfValidity;
    }

    /**
     * Sets the value of the endOfValidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndOfValidity(XMLGregorianCalendar value) {
        this.endOfValidity = value;
    }

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the fileCrc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileCrc() {
        return fileCrc;
    }

    /**
     * Sets the value of the fileCrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileCrc(String value) {
        this.fileCrc = value;
    }

    /**
     * Gets the value of the isTestDataset property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsTestDataset() {
        if (isTestDataset == null) {
            return false;
        } else {
            return isTestDataset;
        }
    }

    /**
     * Sets the value of the isTestDataset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsTestDataset(Boolean value) {
        this.isTestDataset = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the recordCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRecordCount() {
        return recordCount;
    }

    /**
     * Sets the value of the recordCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRecordCount(Integer value) {
        this.recordCount = value;
    }

    /**
     * Gets the value of the startOfValidity property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartOfValidity() {
        return startOfValidity;
    }

    /**
     * Sets the value of the startOfValidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartOfValidity(XMLGregorianCalendar value) {
        this.startOfValidity = value;
    }

    /**
     * Gets the value of the supplierTextField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSupplierTextField() {
        return supplierTextField;
    }

    /**
     * Sets the value of the supplierTextField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSupplierTextField(String value) {
        this.supplierTextField = value;
    }

    /**
     * Gets the value of the targetCustomerIdent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetCustomerIdent() {
        return targetCustomerIdent;
    }

    /**
     * Sets the value of the targetCustomerIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetCustomerIdent(String value) {
        this.targetCustomerIdent = value;
    }

    /**
     * Gets the value of the uuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the value of the uuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUuid(String value) {
        this.uuid = value;
    }

    /**
     * Gets the value of the versionNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVersionNumber() {
        return versionNumber;
    }

    /**
     * Sets the value of the versionNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVersionNumber(Integer value) {
        this.versionNumber = value;
    }

    /**
     * Gets the value of the codingRuleVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodingRuleVersion() {
        return codingRuleVersion;
    }

    /**
     * Sets the value of the codingRuleVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodingRuleVersion(String value) {
        this.codingRuleVersion = value;
    }

    /**
     * Gets the value of the arinc424SchemaVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArinc424SchemaVersion() {
        if (arinc424SchemaVersion == null) {
            return "4.0.0";
        } else {
            return arinc424SchemaVersion;
        }
    }

    /**
     * Sets the value of the arinc424SchemaVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArinc424SchemaVersion(String value) {
        this.arinc424SchemaVersion = value;
    }

    /**
     * Gets the value of the arinc424Supplement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArinc424Supplement() {
        if (arinc424Supplement == null) {
            return "23";
        } else {
            return arinc424Supplement;
        }
    }

    /**
     * Sets the value of the arinc424Supplement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArinc424Supplement(String value) {
        this.arinc424Supplement = value;
    }

}
