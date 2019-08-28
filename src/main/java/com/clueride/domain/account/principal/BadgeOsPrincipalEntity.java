/*
 * Copyright 2019 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by jett on 1/13/19.
 */
package com.clueride.domain.account.principal;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Builder for BadgeOsPrincipal.
 */
@Entity
@Table(name = "wp_users")
public class BadgeOsPrincipalEntity {
    @Id
    private Integer id;

    @Column(name = "display_name")
    private String name;

    @Transient
    private InternetAddress emailAddress;

    @Column(name = "user_email")
    private String emailAddressString;

    public BadgeOsPrincipal build() {
        return new BadgeOsPrincipal(this);
    }

    public static BadgeOsPrincipalEntity builder() {
        return new BadgeOsPrincipalEntity();
    }

    public String getName() {
        return name;
    }

    public BadgeOsPrincipalEntity withName(String name) {
        this.name = name;
        return this;
    }

    public InternetAddress getEmailAddress() {
        emailStringToInternetAddress(emailAddressString);
        return this.emailAddress;
    }

    public BadgeOsPrincipalEntity withEmailAddress(InternetAddress emailAddress) {
        this.emailAddressString = emailAddress.toString();
        this.emailAddress = emailAddress;
        return this;
    }

    public BadgeOsPrincipalEntity withEmailAddressString(String emailAddressString) {
        this.emailAddressString = emailAddressString;
        emailStringToInternetAddress(emailAddressString);
        return this;
    }

    private void emailStringToInternetAddress(String emailAddressString) {
        try {
            this.emailAddress = new InternetAddress(emailAddressString);
        } catch (AddressException e) {
            this.emailAddress = null;
            e.printStackTrace();
        }
    }

    public Integer getBadgeOsUserId() {
        return id;
    }

    public BadgeOsPrincipalEntity withId(Integer badgeOsUserId) {
        return withBadgeOsUserId(badgeOsUserId);
    }

    public BadgeOsPrincipalEntity withBadgeOsUserId(Integer badgeOsUserId) {
        this.id = badgeOsUserId;
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
