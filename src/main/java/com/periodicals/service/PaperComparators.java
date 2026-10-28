package com.periodicals.service;

import com.periodicals.model.Paper;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public final class PaperComparators {

    private static final Collator UKRAINIAN = Collator.getInstance(Locale.of("uk"));

    public static final Comparator<Paper> BY_TITLE =
            Comparator.comparing(Paper::getTitle, UKRAINIAN);

    public static final Comparator<Paper> BY_PAGE_COUNT =
            Comparator.comparingInt(paper -> paper.getChars().getPageCount());

    public static final Comparator<Paper> BY_TYPE_THEN_TITLE =
            Comparator.comparing(Paper::getType).thenComparing(BY_TITLE);

    private PaperComparators() {
    }
}