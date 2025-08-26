package com.denyyyys.fluentLift.model.postgres.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TextBlockType {
    BIG_HEADING("bigHeading"),
    SMALL_HEADING("smallHeading"),
    PARAGRAPH("paragraph");

    private final String value;

    TextBlockType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
