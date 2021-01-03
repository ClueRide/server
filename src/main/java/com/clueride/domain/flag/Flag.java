package com.clueride.domain.flag;

import com.clueride.domain.flag.reason.FlagReason;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.concurrent.Immutable;

/**
 * Represents a Flagged Item on a given Attraction.
 */
@Immutable
public class Flag {
    private Integer id;
    private Integer attractionId;
    private FlagReason reason;
    private String description;
    private FlaggedAttribute flaggedAttribute;
    private Integer openBadgeEventId;
    private Integer closeBadgeEventId;
    private FlagResolution flagResolution;

    public Flag(FlagEntity builder) {
        this.id = builder.getId();
        this.attractionId = builder.getAttractionId();
        this.reason = builder.getReason();
        this.description = builder.getDescription();
        this.flaggedAttribute = builder.getFlaggedAttribute();
        this.openBadgeEventId = builder.getOpenBadgeEventId();
        this.closeBadgeEventId = builder.getCloseBadgeEventId();
        this.flagResolution = builder.getFlagResolution();
    }

    public Integer getId() {
        return id;
    }

    public Integer getAttractionId() {
        return attractionId;
    }

    public FlagReason getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }

    public FlaggedAttribute getFlaggedAttribute() {
        return flaggedAttribute;
    }

    public Integer getOpenBadgeEventId() {
        return openBadgeEventId;
    }

    public Integer getCloseBadgeEventId() {
        return closeBadgeEventId;
    }

    public FlagResolution getFlagResolution() {
        return flagResolution;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
