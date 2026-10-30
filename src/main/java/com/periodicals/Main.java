package com.periodicals;

import com.periodicals.model.Paper;
import com.periodicals.parser.DomPaperParser;
import com.periodicals.parser.PaperParseException;
import com.periodicals.parser.PaperParser;
import com.periodicals.parser.SaxPaperParser;
import com.periodicals.parser.StaxPaperParser;
import com.periodicals.service.PaperComparators;
import com.periodicals.service.PaperService;
import com.periodicals.xml.XmlTransformer;
import com.periodicals.xml.XmlValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static final String XML_PATH = "/xml/papers.xml";
    private static final String XSD_PATH = "/xml/papers.xsd";
    private static final String XSL_PATH = "/xml/papers.xsl";
    private static final Path OUTPUT_PATH = Path.of("output", "papers-by-type.xml");

    public static void main(String[] args) {
        try {
            validate();
            List<PaperParser> parsers = List.of(
                    new SaxPaperParser(), new DomPaperParser(), new StaxPaperParser());
            for (PaperParser parser : parsers) {
                printSorted(parser);
            }
            transform();
        } catch (PaperParseException | IOException e) {
            LOGGER.error("Application failed", e);
        }
    }

    private static void validate() throws PaperParseException, IOException {
        try (InputStream xml = open(XML_PATH); InputStream xsd = open(XSD_PATH)) {
            new XmlValidator().validate(xml, xsd);
        }
    }

    private static void printSorted(PaperParser parser) throws PaperParseException, IOException {
        try (InputStream xml = open(XML_PATH)) {
            List<Paper> papers = new PaperService(parser)
                    .loadSorted(xml, PaperComparators.BY_TYPE_THEN_TITLE);
            LOGGER.info("Papers sorted by type and title ({}):", parser.getClass().getSimpleName());
            papers.forEach(paper -> LOGGER.info("{}", paper));
        }
    }

    private static void transform() throws PaperParseException, IOException {
        Files.createDirectories(OUTPUT_PATH.getParent());
        try (InputStream xml = open(XML_PATH);
             InputStream xsl = open(XSL_PATH);
             OutputStream result = Files.newOutputStream(OUTPUT_PATH)) {
            new XmlTransformer().transform(xml, xsl, result);
        }
        LOGGER.info("Grouped XML written to {}", OUTPUT_PATH);
    }

    private static InputStream open(String resource) throws PaperParseException {
        InputStream stream = Main.class.getResourceAsStream(resource);
        if (stream == null) {
            throw new PaperParseException("Resource not found: " + resource);
        }
        return stream;
    }
}