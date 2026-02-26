package org.mitre.boogie.xml.model;

public record ArincMsaSector(
    long sectorAltitude,
    long sectorBearingBegin,
    long sectorBearingEnd,
    long sectorRadius
) {
}
