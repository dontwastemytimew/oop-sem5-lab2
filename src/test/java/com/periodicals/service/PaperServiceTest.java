package com.periodicals.service;

import com.periodicals.model.Paper;
import com.periodicals.parser.PaperParseException;
import com.periodicals.parser.PaperParser;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static com.periodicals.model.PaperFixtures.paper;
import static com.periodicals.model.PaperType.MAGAZINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaperServiceTest {

    private static final InputStream NO_INPUT = InputStream.nullInputStream();

    @Test
    void returnsPapersSortedByGivenOrder() throws PaperParseException {
        PaperParser parser = xml -> List.of(
                paper("p1", "Б", MAGAZINE, 64),
                paper("p2", "А", MAGAZINE, 32));

        List<String> ids = new PaperService(parser)
                .loadSorted(NO_INPUT, PaperComparators.BY_TITLE).stream()
                .map(Paper::getId)
                .toList();

        assertEquals(List.of("p2", "p1"), ids);
    }

    @Test
    void passesParseFailureToCaller() {
        PaperParser parser = xml -> {
            throw new PaperParseException("broken");
        };
        PaperService service = new PaperService(parser);

        assertThrows(PaperParseException.class,
                () -> service.loadSorted(NO_INPUT, PaperComparators.BY_TITLE));
    }
}