package com.periodicals.model;

/**
 * Kind of a periodical publication.
 */
public enum PaperType {

    NEWSPAPER("newspaper"),
    MAGAZINE("magazine"),
    BOOKLET("booklet");

    private final String xmlValue;

    PaperType(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    public String getXmlValue() {
        return xmlValue;
    }

    /**
     * Finds a type by the text used in the XML document, e.g. "magazine".
     */
    public static PaperType fromXmlValue(String xmlValue) {
        for (PaperType type : values()) {
            if (type.xmlValue.equals(xmlValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown paper type: " + xmlValue);
    }
}