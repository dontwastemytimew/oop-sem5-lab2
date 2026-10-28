package com.periodicals.service;

import com.periodicals.model.Paper;
import com.periodicals.parser.PaperParseException;
import com.periodicals.parser.PaperParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PaperService {

    private final PaperParser parser;

    public PaperService(PaperParser parser) {
        this.parser = parser;
    }

    public List<Paper> loadSorted(InputStream xml, Comparator<Paper> order)
            throws PaperParseException {
        List<Paper> papers = new ArrayList<>(parser.parse(xml));
        papers.sort(order);
        return papers;
    }
}