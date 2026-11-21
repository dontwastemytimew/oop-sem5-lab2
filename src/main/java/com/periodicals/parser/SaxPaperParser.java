package com.periodicals.parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class SaxPaperParser extends AbstractPaperParser {

    public SaxPaperParser() {
        super("SAX");
    }

    @Override
    protected void read(InputStream xml, PaperHandler handler)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory.newInstance().newSAXParser().parse(xml, handler);
    }
}