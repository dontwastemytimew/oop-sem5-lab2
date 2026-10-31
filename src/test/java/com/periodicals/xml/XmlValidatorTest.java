package com.periodicals.xml;

import com.periodicals.parser.PaperParseException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XmlValidatorTest {

    private static final String XSD_PATH = "/xml/papers.xsd";
    private static final String XML_PATH = "/xml/papers.xml";

    private static InputStream resource(String path) {
        return XmlValidatorTest.class.getResourceAsStream(path);
    }

    @Test
    void acceptsDocumentMatchingSchema() {
        assertDoesNotThrow(() ->
                new XmlValidator().validate(resource(XML_PATH), resource(XSD_PATH)));
    }

    @Test
    void rejectsDocumentBreakingSchema() {
        String unknownType = """
                <Papers>
                    <Paper id="p1">
                        <Title>Poster</Title>
                        <Type>poster</Type>
                        <Monthly>true</Monthly>
                        <Chars><Colored>true</Colored><PageCount>5</PageCount></Chars>
                    </Paper>
                </Papers>
                """;
        InputStream xml = new ByteArrayInputStream(unknownType.getBytes(StandardCharsets.UTF_8));

        assertThrows(PaperParseException.class,
                () -> new XmlValidator().validate(xml, resource(XSD_PATH)));
    }
}