package com.periodicals.service;

import com.periodicals.model.Paper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import static com.periodicals.model.PaperFixtures.paper;
import static com.periodicals.model.PaperType.BOOKLET;
import static com.periodicals.model.PaperType.MAGAZINE;
import static com.periodicals.model.PaperType.NEWSPAPER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaperComparatorsTest {

    private static List<String> titlesAfterSorting(List<Paper> papers,
                                                   Comparator<Paper> order) {
        List<Paper> sorted = new ArrayList<>(papers);
        sorted.sort(order);
        return sorted.stream().map(Paper::getTitle).toList();
    }

    @Test
    void byTitleFollowsUkrainianAlphabet() {
        List<Paper> papers = List.of(
                paper("p1", "Жираф", MAGAZINE, 10),
                paper("p2", "Єнот", MAGAZINE, 10),
                paper("p3", "Еталон", MAGAZINE, 10));

        assertEquals(List.of("Еталон", "Єнот", "Жираф"),
                titlesAfterSorting(papers, PaperComparators.BY_TITLE));
    }

    @Test
    void byPageCountGoesFromSmallestToLargest() {
        List<Paper> papers = List.of(
                paper("p1", "B", MAGAZINE, 96),
                paper("p2", "A", MAGAZINE, 4),
                paper("p3", "C", MAGAZINE, 32));

        assertEquals(List.of("A", "C", "B"),
                titlesAfterSorting(papers, PaperComparators.BY_PAGE_COUNT));
    }

    @Test
    void byTypeThenTitleGroupsByTypeFirst() {
        List<Paper> papers = List.of(
                paper("p1", "Б", BOOKLET, 4),
                paper("p2", "Я", NEWSPAPER, 16),
                paper("p3", "А", MAGAZINE, 64),
                paper("p4", "А", NEWSPAPER, 32));

        assertEquals(List.of("А", "Я", "А", "Б"),
                titlesAfterSorting(papers, PaperComparators.BY_TYPE_THEN_TITLE));
    }
}