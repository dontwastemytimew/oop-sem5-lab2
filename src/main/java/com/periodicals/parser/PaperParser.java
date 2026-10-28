package com.periodicals.parser;

import com.periodicals.model.Paper;

import java.io.InputStream;
import java.util.List;

/**
 * Reads publications from an XML document. The caller closes the stream.
 */
public interface PaperParser {

    List<Paper> parse(InputStream xml) throws PaperParseException;
}