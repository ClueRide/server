package com.clueride.domain.flag.details;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FlagDetails {
    private FlaggedAttribute attribute;
    private String description;

    public FlaggedAttribute getAttribute() {
        return attribute;
    }

    public FlagDetails withAttribute(FlaggedAttribute attribute) {
        this.attribute = attribute;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FlagDetails withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
