package org.mitre.boogie.xml.model.fields;

public record ArincTaaSectorDetails(
    long sectorAltitude,
    long sectorBearingBegin,
    long sectorBearingEnd,
    long sectorRadius,
    long sectorRadiusStart,
    long sectorRadiusEnd,
    boolean procedureTurn
) {}
