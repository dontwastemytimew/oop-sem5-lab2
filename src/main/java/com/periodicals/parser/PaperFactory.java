package com.periodicals.parser;

import com.periodicals.model.Paper;
import com.periodicals.model.PaperChars;
import com.periodicals.model.PaperType;

import java.util.Map;
import java.util.Optional;

import static com.periodicals.parser.PaperTags.COLORED;
import static com.periodicals.parser.PaperTags.GLOSSY;
import static com.periodicals.parser.PaperTags.MONTHLY;
import static com.periodicals.parser.PaperTags.PAGE_COUNT;
import static com.periodicals.parser.PaperTags.SUBSCRIPTION_INDEX;
import static com.periodicals.parser.PaperTags.TITLE;
import static com.periodicals.parser.PaperTags.TYPE;

/**
 * Builds a {@link Paper} from texts read by any of the parsers,
 * so that the conversion rules exist in one place.
 */
final class PaperFactory {

    private PaperFactory() {
    }

    static Paper create(String id, Map<String, String> fields) throws PaperParseException {
        try {
            PaperChars.Builder chars = PaperChars.builder()
                    .colored(Boolean.parseBoolean(required(fields, COLORED)))
                    .pageCount(Integer.parseInt(required(fields, PAGE_COUNT)));
            optional(fields, GLOSSY).ifPresent(value -> chars.glossy(Boolean.parseBoolean(value)));
            optional(fields, SUBSCRIPTION_INDEX).ifPresent(chars::subscriptionIndex);

            return Paper.builder()
                    .id(id)
                    .title(required(fields, TITLE))
                    .type(PaperType.fromXmlValue(required(fields, TYPE)))
                    .monthly(Boolean.parseBoolean(required(fields, MONTHLY)))
                    .chars(chars.build())
                    .build();
        } catch (IllegalArgumentException e) {
            throw new PaperParseException("Invalid value in paper " + id, e);
        }
    }

    private static String required(Map<String, String> fields, String tag)
            throws PaperParseException {
        String value = fields.get(tag);
        if (value == null) {
            throw new PaperParseException("Missing element: " + tag);
        }
        return value;
    }

    private static Optional<String> optional(Map<String, String> fields, String tag) {
        return Optional.ofNullable(fields.get(tag));
    }
}