package com.periodicals.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaperTest {

    private static final PaperChars CHARS = PaperChars.builder().colored(true).pageCount(96).build();

    private static Paper.Builder validPaper() {
        return Paper.builder()
                .id("p1")
                .title("Forbes Ukraine")
                .type(PaperType.MAGAZINE)
                .monthly(true)
                .chars(CHARS);
    }

    @Test
    void buildFailsWhenRequiredFieldIsMissing() {
        assertThrows(NullPointerException.class, () -> validPaper().id(null).build());
        assertThrows(NullPointerException.class, () -> validPaper().title(null).build());
        assertThrows(NullPointerException.class, () -> validPaper().type(null).build());
        assertThrows(NullPointerException.class, () -> validPaper().chars(null).build());
    }

    @Test
    void equalsAndHashCodeComparesAllFields() {
        Paper paper = validPaper().build();

        assertEquals(paper, paper);
        assertEquals(paper, validPaper().build());
        assertEquals(paper.hashCode(), validPaper().build().hashCode());
        assertNotEquals(paper, null);
        assertNotEquals(paper, "text");
        assertNotEquals(paper, validPaper().id("p2").build());
        assertNotEquals(paper, validPaper().title("Other").build());
        assertNotEquals(paper, validPaper().type(PaperType.BOOKLET).build());
        assertNotEquals(paper, validPaper().monthly(false).build());
        assertNotEquals(paper, validPaper().chars(PaperChars.builder().pageCount(1).build()).build());
    }

}