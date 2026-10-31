package com.periodicals.parser;

import com.periodicals.model.Paper;
import com.periodicals.model.PaperChars;
import com.periodicals.model.PaperType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaperParsersTest {

    private static final String MAGAZINE = """
            <Paper id="p1">
                <Title>Forbes Ukraine</Title>
                <Type>magazine</Type>
                <Monthly>true</Monthly>
                <Chars>
                    <Colored>true</Colored>
                    <PageCount>96</PageCount>
                    <Glossy>true</Glossy>
                    <SubscriptionIndex>40123</SubscriptionIndex>
                </Chars>
            </Paper>
            """;

    private static final String NEWSPAPER = """
            <Paper id="p2">
                <Title>Голос України</Title>
                <Type>newspaper</Type>
                <Monthly>false</Monthly>
                <Chars>
                    <Colored>false</Colored>
                    <PageCount>32</PageCount>
                    <SubscriptionIndex>74321</SubscriptionIndex>
                </Chars>
            </Paper>
            """;

    private static Stream<PaperParser> parsers() {
        return Stream.of(new SaxPaperParser(), new DomPaperParser(), new StaxPaperParser());
    }

    private static InputStream xml(String papers) {
        String document = "<Papers>" + papers + "</Papers>";
        return new ByteArrayInputStream(document.getBytes(StandardCharsets.UTF_8));
    }

    private static void assertRejected(PaperParser parser, String papers) {
        assertThrows(PaperParseException.class, () -> parser.parse(xml(papers)));
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void readsAllFieldsOfPaper(PaperParser parser) throws PaperParseException {
        Paper expected = Paper.builder()
                .id("p1")
                .title("Forbes Ukraine")
                .type(PaperType.MAGAZINE)
                .monthly(true)
                .chars(PaperChars.builder()
                        .colored(true)
                        .pageCount(96)
                        .glossy(true)
                        .subscriptionIndex("40123")
                        .build())
                .build();

        assertEquals(List.of(expected), parser.parse(xml(MAGAZINE)));
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void leavesOptionalFieldsEmptyWhenAbsent(PaperParser parser) throws PaperParseException {
        Paper paper = parser.parse(xml(NEWSPAPER)).get(0);

        assertTrue(paper.getChars().getGlossy().isEmpty());
        assertEquals(Optional.of("74321"), paper.getChars().getSubscriptionIndex());
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void keepsDocumentOrder(PaperParser parser) throws PaperParseException {
        List<String> ids = parser.parse(xml(MAGAZINE + NEWSPAPER)).stream()
                .map(Paper::getId)
                .toList();

        assertEquals(List.of("p1", "p2"), ids);
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void rejectsMalformedXml(PaperParser parser) {
        assertRejected(parser, "<Paper>");
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void rejectsPaperWithoutRequiredElement(PaperParser parser) {
        assertRejected(parser, MAGAZINE.replace("<Title>Forbes Ukraine</Title>", ""));
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void rejectsUnknownPaperType(PaperParser parser) {
        assertRejected(parser, MAGAZINE.replace("magazine", "poster"));
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void rejectsNonNumericPageCount(PaperParser parser) {
        assertRejected(parser, MAGAZINE.replace("96", "many"));
    }

    @ParameterizedTest
    @MethodSource("parsers")
    void wrapsInputReadFailure(PaperParser parser) {
        InputStream broken = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("disk failure");
            }
        };

        assertThrows(PaperParseException.class, () -> parser.parse(broken));
    }
}