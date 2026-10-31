package com.periodicals.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaperTypeTest {

    @ParameterizedTest
    @EnumSource(PaperType.class)
    void fromXmlValueFindsEveryType(PaperType type) {
        assertEquals(type, PaperType.fromXmlValue(type.getXmlValue()));
    }

    @Test
    void fromXmlValueRejectsUnknownText() {
        assertThrows(IllegalArgumentException.class, () -> PaperType.fromXmlValue("poster"));
    }
}