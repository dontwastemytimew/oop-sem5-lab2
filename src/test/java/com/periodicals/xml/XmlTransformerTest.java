package com.periodicals.xml;

import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlTransformerTest {

    @Test
    void groupsPapersByType() throws Exception {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        new XmlTransformer().transform(
                XmlTransformerTest.class.getResourceAsStream("/xml/papers.xml"),
                XmlTransformerTest.class.getResourceAsStream("/xml/papers.xsl"),
                result);

        String output = result.toString(StandardCharsets.UTF_8);
        XPath xpath = XPathFactory.newInstance().newXPath();
        assertEquals("3", evaluate(xpath, "count(/PapersByType/Group)", output));
        assertEquals("2", evaluate(xpath, "count(/PapersByType/Group[@type='newspaper']/Paper)", output));
    }

    private static String evaluate(XPath xpath, String expression, String xml) throws Exception {
        return xpath.evaluate(expression, new InputSource(new StringReader(xml)));
    }
}