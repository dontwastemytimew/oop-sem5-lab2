package com.periodicals.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaperCharsTest {

    private static PaperChars.Builder fullChars() {
        return PaperChars.builder()
                .colored(true)
                .pageCount(96)
                .glossy(true)
                .subscriptionIndex("40123");
    }

    @Test
    void optionalFieldsAreEmptyWhenNotSet() {
        PaperChars chars = PaperChars.builder().colored(false).pageCount(16).build();

        assertTrue(chars.getGlossy().isEmpty());
        assertTrue(chars.getSubscriptionIndex().isEmpty());
    }

    @Test
    void equalsAndHashCodeComparesAllFields() {
        PaperChars chars = fullChars().build();

        assertEquals(chars, chars);
        assertEquals(chars, fullChars().build());
        assertEquals(chars.hashCode(), fullChars().build().hashCode());
        assertNotEquals(chars, null);
        assertNotEquals(chars, "text");
        assertNotEquals(chars, fullChars().colored(false).build());
        assertNotEquals(chars, fullChars().pageCount(8).build());
        assertNotEquals(chars, fullChars().glossy(false).build());
        assertNotEquals(chars, fullChars().subscriptionIndex("00000").build());
    }

}