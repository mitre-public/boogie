package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ArincAreaCode implements Serializable {
    /**
     * Africa
     *
     */
    AFR,

    /**
     * Canada
     *
     */
    CAN,

    /**
     * Eastern Europe and Asia
     *
     */
    EEU,

    /**
     * Europe
     *
     */
    EUR,

    /**
     * Latin America
     *
     */
    LAM,

    /**
     * Middle East
     *
     */
    MES,

    /**
     * Pacific
     *
     */
    PAC,

    /**
     * South America
     *
     */
    SAM,

    /**
     * South Pacific
     *
     */
    SPA,

    /**
     * United States
     *
     */
    USA;

    public static final Set<String> VALID = Arrays.stream(ArincAreaCode.values()).map(ArincAreaCode::name).collect(Collectors.toSet());
}
