
package org.mitre.boogie.xml.v23_4.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NavaidWeatherInfo.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="NavaidWeatherInfo"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="AUTOMATED"/&gt;
 *     &lt;enumeration value="SCHEDULED"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "NavaidWeatherInfo", namespace = "http://www.arinc424.com/xml/enumerations")
@XmlEnum
public enum NavaidWeatherInfo {


    /**
     * Automatic Transcribed Weather.The frequency of this Navaid is used for the continuous broadcast of some sort of automated weather system such as AWOS, ASOS, TWEB, AWIB, AWIS.
     * 
     */
    AUTOMATED,

    /**
     * Broadcast Scheduled Weather.The frequency of this Navaid is used for the scheduled, noncontinuous broadcast of some sort of automated weather system such as VOLMET.
     * 
     */
    SCHEDULED;

    public String value() {
        return name();
    }

    public static NavaidWeatherInfo fromValue(String v) {
        return valueOf(v);
    }

}
