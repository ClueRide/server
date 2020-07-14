package com.clueride.domain.account.badgeos;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * Holds a single Attribute for a given user.
 */
@Entity
@Table(name="wp_usermeta")
public class UserAttributeEntity {
    @Id
    @Column(name = "umeta_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private BadgeOsUserEntity badgeOsUserEntity;

    @Column(name = "meta_key")
    private String userAttributeKey;

    @Column(name = "meta_value")
    private String userAttributeValue;

    public static UserAttributeEntity builder() {
        return new UserAttributeEntity();
    }

    public Integer getId() {
        return id;
    }

    public UserAttributeEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public BadgeOsUserEntity getBadgeOsUserEntity() {
        return badgeOsUserEntity;
    }

    public UserAttributeEntity withBadgeOsUserEntity(BadgeOsUserEntity badgeOsUserEntity) {
        this.badgeOsUserEntity = badgeOsUserEntity;
        return this;
    }

    public String getUserAttributeKey() {
        return userAttributeKey;
    }

    public UserAttributeEntity withUserAttributeKey(String userAttributeKey) {
        this.userAttributeKey = userAttributeKey;
        return this;
    }

    public String getUserAttributeValue() {
        return userAttributeValue;
    }

    public UserAttributeEntity withUserAttributeValue(String userAttributeValue) {
        this.userAttributeValue = userAttributeValue;
        return this;
    }

    /* Standard helpers. */
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
