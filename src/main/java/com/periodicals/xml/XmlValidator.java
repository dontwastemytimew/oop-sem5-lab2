package com.periodicals.xml;

import com.periodicals.parser.PaperParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;

public class XmlValidator {

    private static final Logger LOGGER = LogManager.getLogger(XmlValidator.class);

    public void validate(InputStream xml, InputStream xsd) throws PaperParseException {
        try {
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                    .newSchema(new StreamSource(xsd));
            schema.newValidator().validate(new StreamSource(xml));
            LOGGER.info("XML matches the schema");
        } catch (SAXException e) {
            throw new PaperParseException("XML does not match the schema: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new PaperParseException("Cannot read XML or schema", e);
        }
    }
}