package com.periodicals.parser;

import com.periodicals.model.Paper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomPaperParser implements PaperParser {

    private static final Logger LOGGER = LogManager.getLogger(DomPaperParser.class);

    @Override
    public List<Paper> parse(InputStream xml) throws PaperParseException {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(xml);
            NodeList nodes = document.getElementsByTagName(PaperTags.PAPER);
            List<Paper> papers = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                papers.add(readPaper((Element) nodes.item(i)));
            }
            LOGGER.info("DOM parser read {} papers", papers.size());
            return papers;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new PaperParseException("Cannot parse XML with DOM", e);
        }
    }

    private Paper readPaper(Element paperElement) throws PaperParseException {
        Map<String, String> fields = new HashMap<>();
        NodeList descendants = paperElement.getElementsByTagName("*");
        for (int i = 0; i < descendants.getLength(); i++) {
            Element element = (Element) descendants.item(i);
            boolean isLeaf = element.getElementsByTagName("*").getLength() == 0;
            if (isLeaf) {
                fields.put(element.getTagName(), element.getTextContent().trim());
            }
        }
        return PaperFactory.create(paperElement.getAttribute(PaperTags.ID), fields);
    }
}