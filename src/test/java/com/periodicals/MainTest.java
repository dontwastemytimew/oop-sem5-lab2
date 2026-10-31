package com.periodicals;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private static final Path OUTPUT = Path.of("output", "papers-by-type.xml");

    @Test
    void writesPapersGroupedByType() throws IOException {
        Files.deleteIfExists(OUTPUT);

        Main.main(new String[0]);

        assertTrue(Files.readString(OUTPUT).contains("<Group type=\"newspaper\">"));
    }
}