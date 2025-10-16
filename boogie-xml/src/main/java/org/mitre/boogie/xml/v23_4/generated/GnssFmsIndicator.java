
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GnssFmsIndicator.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="GnssFmsIndicator"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="GnssFmsOverlayAuthNotPublished"/&gt;
 *     &lt;enumeration value="GnssOverlayAuthNavaidOperatingAndMonitored"/&gt;
 *     &lt;enumeration value="GnssOverlayAuthNoMonitorNavaidAuth"/&gt;
 *     &lt;enumeration value="GnssOverlayTitleIncludesGpsOrGnss"/&gt;
 *     &lt;enumeration value="FmsOverlayAuthorized"/&gt;
 *     &lt;enumeration value="SbasVerticalAuthorized"/&gt;
 *     &lt;enumeration value="SbasVerticalNotAuthorized"/&gt;
 *     &lt;enumeration value="SbasVerticalNotPublished"/&gt;
 *     &lt;enumeration value="StandAloneGNSS"/&gt;
 *     &lt;enumeration value="SbasVerticalNA"/&gt;
 *     &lt;enumeration value="OverlayAuthNotPublished"/&gt;
 *     &lt;enumeration value="PbnRnpGps"/&gt;
 *     &lt;enumeration value="LocOnlyIls"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "GnssFmsIndicator", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum GnssFmsIndicator {


    /**
     * Procedure Not Authorized for GNSS or FMS Overlay ? Authorization not published.
     * 
     */
    @XmlEnumValue("GnssFmsOverlayAuthNotPublished")
    GNSS_FMS_OVERLAY_AUTH_NOT_PUBLISHED("GnssFmsOverlayAuthNotPublished"),

    /**
     * Procedure Authorized for GNSS Overlay, primary Navaids operating and monitored. Authorization not published.
     * 
     */
    @XmlEnumValue("GnssOverlayAuthNavaidOperatingAndMonitored")
    GNSS_OVERLAY_AUTH_NAVAID_OPERATING_AND_MONITORED("GnssOverlayAuthNavaidOperatingAndMonitored"),

    /**
     * Procedure Authorized for GNSS Overlay, primary Navaids installed, not monitored Authorization is published. Example: Procedure Title includes (GPS) or (GNSS)
     * 
     */
    @XmlEnumValue("GnssOverlayAuthNoMonitorNavaidAuth")
    GNSS_OVERLAY_AUTH_NO_MONITOR_NAVAID_AUTH("GnssOverlayAuthNoMonitorNavaidAuth"),

    /**
     * Procedure Authorized for GNSS Overlay, Procedure Title includes GPS or GNSS
     * 
     */
    @XmlEnumValue("GnssOverlayTitleIncludesGpsOrGnss")
    GNSS_OVERLAY_TITLE_INCLUDES_GPS_OR_GNSS("GnssOverlayTitleIncludesGpsOrGnss"),

    /**
     * Procedure Authorized for FMS Overlay
     * 
     */
    @XmlEnumValue("FmsOverlayAuthorized")
    FMS_OVERLAY_AUTHORIZED("FmsOverlayAuthorized"),

    /**
     * RNAV (GPS), RNAV (RNP) or RNAV (GNSS) Procedure SBAS use authorized; SBAS-based vertical navigation authorized
     * 
     */
    @XmlEnumValue("SbasVerticalAuthorized")
    SBAS_VERTICAL_AUTHORIZED("SbasVerticalAuthorized"),

    /**
     * RNAV (GPS), RNAV (RNP), RNAV (GNSS) or RNAV Visual Procedure, SBAS-based vertical navigation NOT Authorized 
     * 
     */
    @XmlEnumValue("SbasVerticalNotAuthorized")
    SBAS_VERTICAL_NOT_AUTHORIZED("SbasVerticalNotAuthorized"),

    /**
     * RNAV (GPS) RNAV (RNP), or RNAV (GNSS) Procedure, SBAS-based vertical navigation use not published
     * 
     */
    @XmlEnumValue("SbasVerticalNotPublished")
    SBAS_VERTICAL_NOT_PUBLISHED("SbasVerticalNotPublished"),

    /**
     * Stand Alone GNSS Procedure
     * 
     */
    @XmlEnumValue("StandAloneGNSS")
    STAND_ALONE_GNSS("StandAloneGNSS"),

    /**
     * RNAV (GPS) RNAV (RNP), or RNAV (GNSS) Procedure within the SBAS operational footprint, but SBAS-based vertical navigation NOT Authorized
     * 
     */
    @XmlEnumValue("SbasVerticalNA")
    SBAS_VERTICAL_NA("SbasVerticalNA"),

    /**
     * Procedure Overlay Authorization not published
     * 
     */
    @XmlEnumValue("OverlayAuthNotPublished")
    OVERLAY_AUTH_NOT_PUBLISHED("OverlayAuthNotPublished"),

    /**
     * PBN RNP Approach provide as GPS.  The GNSS/FMS IND of G indicates that the GPS approach is an PBN RNAV approach provided with route type P.
     * 
     */
    @XmlEnumValue("PbnRnpGps")
    PBN_RNP_GPS("PbnRnpGps"),

    /**
     * Localizer only coding portion of ILS.  The GNSS/FMS IND of L indicates that the LOC approach is the Localizer only portion of an ILS approach which contains glideslope out information. 
     * 
     */
    @XmlEnumValue("LocOnlyIls")
    LOC_ONLY_ILS("LocOnlyIls");
    private final String value;

    GnssFmsIndicator(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GnssFmsIndicator fromValue(String v) {
        for (GnssFmsIndicator c: GnssFmsIndicator.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
