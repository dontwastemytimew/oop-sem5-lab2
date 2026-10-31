package com.periodicals.model;

public final class PaperFixtures {

    private PaperFixtures() {
    }

    public static Paper paper(String id, String title, PaperType type, int pageCount) {
        return Paper.builder()
                .id(id)
                .title(title)
                .type(type)
                .monthly(false)
                .chars(PaperChars.builder().colored(true).pageCount(pageCount).build())
                .build();
    }
}