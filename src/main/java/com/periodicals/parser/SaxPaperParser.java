package com.periodicals.parser;

import com.periodicals.model.Paper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaxPaperParser implements PaperParser {

    private static final Logger LOGGER = LogManager.getLogger(SaxPaperParser.class);

    @Override
    public List<Paper> parse(InputStream xml) throws PaperParseException {
        PaperHandler handler = new PaperHandler();
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(xml, handler);
        } catch (SAXException e) {
            if (e.getCause() instanceof PaperParseException cause) {
                throw cause;
            }
            throw new PaperParseException("Cannot parse XML with SAX", e);
        } catch (ParserConfigurationException | IOException e) {
            throw new PaperParseException("Cannot parse XML with SAX", e);
        }
        LOGGER.info("SAX parser read {} papers", handler.getPapers().size());
        return handler.getPapers();
    }

    private static class PaperHandler extends DefaultHandler {

        private final List<Paper> papers = new ArrayList<>();
        private final StringBuilder text = new StringBuilder();
        private Map<String, String> fields;
        private String id;

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) {
            text.setLength(0);
            if (PaperTags.PAPER.equals(qName)) {
                fields = new HashMap<>();
                id = attributes.getValue(PaperTags.ID);
            }
        }

        @Override
        public void characters(char[] chars, int start, int length) {
            text.append(chars, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (PaperTags.PAPER.equals(qName)) {
                addPaper();
            } else if (fields != null && !PaperTags.CHARS.equals(qName)) {
                fields.put(qName, text.toString().trim());
            }
        }

        List<Paper> getPapers() {
            return papers;
        }

        private void addPaper() throws SAXException {
            try {
                papers.add(PaperFactory.create(id, fields));
            } catch (PaperParseException e) {
                throw new SAXException(e);
            } finally {
                fields = null;
            }
        }
    }
}