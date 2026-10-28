package com.periodicals.parser;

public class PaperParseException extends Exception {

    public PaperParseException(String message) {
        super(message);
    }

    public PaperParseException(String message, Throwable cause) {
        super(message, cause);
    }
}