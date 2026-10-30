package com.periodicals.xml;

import com.periodicals.parser.PaperParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.OutputStream;

public class XmlTransformer {

    private static final Logger LOGGER = LogManager.getLogger(XmlTransformer.class);

    public void transform(InputStream xml, InputStream xsl, OutputStream result)
            throws PaperParseException {
        try {
            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer(new StreamSource(xsl));
            transformer.transform(new StreamSource(xml), new StreamResult(result));
            LOGGER.info("XSL transformation finished");
        } catch (TransformerException e) {
            throw new PaperParseException("XSL transformation failed", e);
        }
    }
}