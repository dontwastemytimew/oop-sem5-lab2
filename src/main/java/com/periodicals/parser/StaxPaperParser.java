package com.periodicals.parser;

import com.periodicals.model.Paper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaxPaperParser implements PaperParser {

    private static final Logger LOGGER = LogManager.getLogger(StaxPaperParser.class);

    @Override
    public List<Paper> parse(InputStream xml) throws PaperParseException {
        try {
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(xml);
            List<Paper> papers = readPapers(reader);
            LOGGER.info("StAX parser read {} papers", papers.size());
            return papers;
        } catch (XMLStreamException e) {
            throw new PaperParseException("Cannot parse XML with StAX", e);
        }
    }

    private List<Paper> readPapers(XMLStreamReader reader)
            throws XMLStreamException, PaperParseException {
        List<Paper> papers = new ArrayList<>();
        Map<String, String> fields = null;
        String id = null;
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if (PaperTags.PAPER.equals(tag)) {
                    fields = new HashMap<>();
                    id = reader.getAttributeValue(null, PaperTags.ID);
                } else if (fields != null && !PaperTags.CHARS.equals(tag)) {
                    fields.put(tag, reader.getElementText().trim());
                }
            } else if (event == XMLStreamConstants.END_ELEMENT
                    && PaperTags.PAPER.equals(reader.getLocalName())) {
                papers.add(PaperFactory.create(id, fields));
                fields = null;
            }
        }
        return papers;
    }
}