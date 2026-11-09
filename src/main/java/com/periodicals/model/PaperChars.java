package com.periodicals.model;

import java.util.Objects;
import java.util.Optional;

/**
 * Characteristics of a publication. The subscription index is absent
 * for the types it does not apply to.
 */
public final class PaperChars {

    private final boolean colored;
    private final int pageCount;
    private final boolean glossy;
    private final String subscriptionIndex;

    private PaperChars(Builder builder) {
        this.colored = builder.colored;
        this.pageCount = builder.pageCount;
        this.glossy = builder.glossy;
        this.subscriptionIndex = builder.subscriptionIndex;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isColored() {
        return colored;
    }

    public int getPageCount() {
        return pageCount;
    }

    public boolean isGlossy() {
        return glossy;
    }

    public Optional<String> getSubscriptionIndex() {
        return Optional.ofNullable(subscriptionIndex);
    }

    public static final class Builder {

        private boolean colored;
        private int pageCount;
        private boolean glossy;
        private String subscriptionIndex;

        private Builder() {
        }

        public Builder colored(boolean colored) {
            this.colored = colored;
            return this;
        }

        public Builder pageCount(int pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public Builder glossy(boolean glossy) {
            this.glossy = glossy;
            return this;
        }

        public Builder subscriptionIndex(String subscriptionIndex) {
            this.subscriptionIndex = subscriptionIndex;
            return this;
        }

        public PaperChars build() {
            return new PaperChars(this);
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
        PaperChars that = (PaperChars) o;
        return colored == that.colored
                && pageCount == that.pageCount
                && glossy == that.glossy
                && Objects.equals(subscriptionIndex, that.subscriptionIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colored, pageCount, glossy, subscriptionIndex);
    }

    @Override
    public String toString() {
        return "PaperChars{" +
                "colored=" + colored +
                ", pageCount=" + pageCount +
                ", glossy=" + glossy +
                ", subscriptionIndex='" + subscriptionIndex + '\'' +
                '}';
    }
}