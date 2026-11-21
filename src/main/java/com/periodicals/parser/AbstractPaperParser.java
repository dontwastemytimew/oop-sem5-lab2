package com.periodicals.parser;

import com.periodicals.model.Paper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Common part of all parsers: subclasses only say how to read the XML,
 * the handler builds the papers, and errors are reported in one way.
 */
public abstract class AbstractPaperParser implements PaperParser {

    private final Logger logger = LogManager.getLogger(getClass());
    private final String name;

    protected AbstractPaperParser(String name) {
        this.name = name;
    }

    @Override
    public final List<Paper> parse(InputStream xml) throws PaperParseException {
        PaperHandler handler = new PaperHandler();
        try {
            read(xml, handler);
        } catch (ParserConfigurationException | SAXException | XMLStreamException
                 | IOException e) {
            throw new PaperParseException("Cannot parse XML with " + name, e);
        } catch (IllegalArgumentException e) {
            throw new PaperParseException(
                    "Invalid paper data (" + name + "): " + e.getMessage(), e);
        }
        logger.info("{} parser read {} papers", name, handler.getPapers().size());
        return handler.getPapers();
    }

    protected abstract void read(InputStream xml, PaperHandler handler)
            throws ParserConfigurationException, SAXException, XMLStreamException, IOException;
}