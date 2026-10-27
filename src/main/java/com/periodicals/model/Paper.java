package com.periodicals.model;

import java.util.Objects;

/**
 * A periodical publication: newspaper, magazine or booklet.
 */
public final class Paper {

    private final String id;
    private final String title;
    private final PaperType type;
    private final boolean monthly;
    private final PaperChars chars;

    private Paper(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "id");
        this.title = Objects.requireNonNull(builder.title, "title");
        this.type = Objects.requireNonNull(builder.type, "type");
        this.monthly = builder.monthly;
        this.chars = Objects.requireNonNull(builder.chars, "chars");
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public PaperType getType() {
        return type;
    }

    public boolean isMonthly() {
        return monthly;
    }

    public PaperChars getChars() {
        return chars;
    }

    public static final class Builder {

        private String id;
        private String title;
        private PaperType type;
        private boolean monthly;
        private PaperChars chars;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder type(PaperType type) {
            this.type = type;
            return this;
        }

        public Builder monthly(boolean monthly) {
            this.monthly = monthly;
            return this;
        }

        public Builder chars(PaperChars chars) {
            this.chars = chars;
            return this;
        }

        public Paper build() {
            return new Paper(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Paper paper = (Paper) o;
        return monthly == paper.monthly
                && Objects.equals(id, paper.id)
                && Objects.equals(title, paper.title)
                && type == paper.type
                && Objects.equals(chars, paper.chars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, type, monthly, chars);
    }

    @Override
    public String toString() {
        return "Paper{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", monthly=" + monthly +
                ", chars=" + chars +
                '}';
    }
}