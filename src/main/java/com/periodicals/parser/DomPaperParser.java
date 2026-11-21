package com.periodicals.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class DomPaperParser extends AbstractPaperParser {

    public DomPaperParser() {
        super("DOM");
    }

    @Override
    protected void read(InputStream xml, PaperHandler handler)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        builder.setErrorHandler(new DefaultHandler());
        Document document = builder.parse(xml);
        NodeList papers = document.getElementsByTagName(PaperTags.PAPER);
        for (int i = 0; i < papers.getLength(); i++) {
            readPaper((Element) papers.item(i), handler);
        }
    }

    private void readPaper(Element paper, PaperHandler handler) {
        handler.startPaper(paper.getAttribute(PaperTags.ID));
        NodeList descendants = paper.getElementsByTagName("*");
        for (int i = 0; i < descendants.getLength(); i++) {
            Element element = (Element) descendants.item(i);
            boolean isLeaf = element.getElementsByTagName("*").getLength() == 0;
            if (isLeaf) {
                handler.setField(element.getTagName(), element.getTextContent().trim());
            }
        }
        handler.endPaper();
    }
}