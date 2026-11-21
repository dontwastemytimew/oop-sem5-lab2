package com.periodicals.parser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class StaxPaperParser extends AbstractPaperParser {

    public StaxPaperParser() {
        super("StAX");
    }

    @Override
    protected void read(InputStream xml, PaperHandler handler) throws XMLStreamException {
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(xml);
        boolean insidePaper = false;
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if (PaperTags.PAPER.equals(tag)) {
                    handler.startPaper(reader.getAttributeValue(null, PaperTags.ID));
                    insidePaper = true;
                } else if (insidePaper && !PaperTags.CHARS.equals(tag)) {
                    handler.setField(tag, reader.getElementText().trim());
                }
            } else if (event == XMLStreamConstants.END_ELEMENT
                    && PaperTags.PAPER.equals(reader.getLocalName())) {
                handler.endPaper();
                insidePaper = false;
            }
        }
    }
}