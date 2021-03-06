package com.clueride.domain.flag;

import com.clueride.domain.flag.reason.FlagReason;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.*;

@Entity
@Table(name="flag")
public class FlagEntity {
    @Inject
    @Transient
    private static Logger LOGGER;

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "flag_pk_sequence")
    @SequenceGenerator(name="flag_pk_sequence", sequenceName = "flag_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name="attraction_id")
    private Integer attractionId;

    @Column
    private FlagReason reason;

    @Column(name="attribute")
    private FlaggedAttribute flaggedAttribute;

    @Column
    private String description;

    @Column(name="open_badge_event_id")
    private Integer openBadgeEventId;

    @Column(name="close_badge_event_id")
    private Integer closeBadgeEventId;

    @Column(name="resolution")
    private FlagResolution flagResolution;

    public Flag build() {
        return new Flag(this);
    }

    public static FlagEntity builder() {
        return new FlagEntity();
    }

    public static FlagEntity from(Flag flag) {
        return builder()
                .withId(flag.getId())
                .withAttractionId(flag.getAttractionId())
                .withReason(flag.getReason())
                .withFlaggedAttribute(flag.getFlaggedAttribute())
                .withDescription(flag.getDescription())
                .withOpenBadgeEventId(flag.getOpenBadgeEventId())
                .withCloseBadgeEventId(flag.getCloseBadgeEventId())
                ;
    }

    public Integer getId() {
        return id;
    }

    public FlagEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getAttractionId() {
        return attractionId;
    }

    public FlagEntity withAttractionId(Integer attractionId) {
        this.attractionId = attractionId;
        return this;
    }

    public FlagReason getReason() {
        return reason;
    }

    public FlagEntity withReason(FlagReason reason) {
        this.reason = reason;
        return this;
    }

    public FlaggedAttribute getFlaggedAttribute() {
        return flaggedAttribute;
    }

    public FlagEntity withFlaggedAttribute(FlaggedAttribute flaggedAttribute) {
        this.flaggedAttribute = flaggedAttribute;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FlagEntity withDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getOpenBadgeEventId() {
        return openBadgeEventId;
    }

    public FlagEntity withOpenBadgeEventId(Integer openBadgeEventId) {
        this.openBadgeEventId = openBadgeEventId;
        return this;
    }

    public Integer getCloseBadgeEventId() {
        return closeBadgeEventId;
    }

    public FlagEntity withCloseBadgeEventId(Integer closeBadgeEventId) {
        this.closeBadgeEventId = closeBadgeEventId;
        return this;
    }

    public FlagResolution getFlagResolution() {
        return flagResolution;
    }

    public FlagEntity withFlagResolution(FlagResolution flagResolution) {
        this.flagResolution = flagResolution;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
