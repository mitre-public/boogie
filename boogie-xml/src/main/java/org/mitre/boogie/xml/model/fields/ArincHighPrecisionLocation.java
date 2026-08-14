package org.mitre.boogie.xml.model.fields;

/**
 * Raw high-precision latitude and longitude values from an ARINC 424 XML document.
 *
 * <p>The XML schema defines these coordinates as strings, so this model preserves their original
 * representation instead of coercing them to a floating-point coordinate.
 */
public record ArincHighPrecisionLocation(String latitude, String longitude) {
}
