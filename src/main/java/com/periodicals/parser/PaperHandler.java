package com.periodicals.parser;

import com.periodicals.model.Paper;
import com.periodicals.model.PaperChars;
import com.periodicals.model.PaperType;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.periodicals.parser.PaperTags.COLORED;
import static com.periodicals.parser.PaperTags.GLOSSY;
import static com.periodicals.parser.PaperTags.ID;
import static com.periodicals.parser.PaperTags.MONTHLY;
import static com.periodicals.parser.PaperTags.PAGE_COUNT;
import static com.periodicals.parser.PaperTags.PAPER;
import static com.periodicals.parser.PaperTags.SUBSCRIPTION_INDEX;
import static com.periodicals.parser.PaperTags.TITLE;
import static com.periodicals.parser.PaperTags.TYPE;

/**
 * Builds publications from what any parser reads. SAX uses it directly as an event
 * handler, DOM and StAX call startPaper, setField and endPaper themselves.
 */
class PaperHandler extends DefaultHandler {

    private final List<Paper> papers = new ArrayList<>();
    private final StringBuilder text = new StringBuilder();
    private Map<String, String> fields;
    private String id;

    List<Paper> getPapers() {
        return papers;
    }

    void startPaper(String id) {
        this.id = id;
        this.fields = new HashMap<>();
    }

    void setField(String tag, String value) {
        if (fields != null) {
            fields.put(tag, value);
        }
    }

    void endPaper() {
        papers.add(createPaper());
        fields = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) {
        text.setLength(0);
        if (PAPER.equals(qName)) {
            startPaper(attributes.getValue(ID));
        }
    }

    @Override
    public void characters(char[] chars, int start, int length) {
        text.append(chars, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (PAPER.equals(qName)) {
            endPaper();
        } else {
            setField(qName, text.toString().trim());
        }
    }

    private Paper createPaper() {
        PaperType type = PaperType.fromXmlValue(required(TYPE));
        boolean glossy = Boolean.parseBoolean(required(GLOSSY));
        if (type == PaperType.NEWSPAPER && glossy) {
            throw new IllegalArgumentException("Newspaper cannot be glossy: " + id);
        }

        PaperChars.Builder chars = PaperChars.builder()
                .colored(Boolean.parseBoolean(required(COLORED)))
                .pageCount(Integer.parseInt(required(PAGE_COUNT)))
                .glossy(glossy);
        Optional.ofNullable(fields.get(SUBSCRIPTION_INDEX)).ifPresent(chars::subscriptionIndex);

        return Paper.builder()
                .id(id)
                .title(required(TITLE))
                .type(type)
                .monthly(Boolean.parseBoolean(required(MONTHLY)))
                .chars(chars.build())
                .build();
    }

    private String required(String tag) {
        String value = fields.get(tag);
        if (value == null) {
            throw new IllegalArgumentException("Missing element " + tag + " in paper " + id);
        }
        return value;
    }
}