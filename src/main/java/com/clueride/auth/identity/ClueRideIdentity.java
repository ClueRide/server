/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 10/13/18.
 */
package com.clueride.auth.identity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.concurrent.Immutable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.Transient;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Attributes for a given actor as provided by the Identity Service.
 *
 * We may want to wrap my Member class in this.
 *
 * This is sample JSON returned from Auth0:
 * {
 *   "sub": "google-oauth2|{some number}",
 *   "given_name": "Clue",
 *   "family_name": "Ride",
 *   "nickname": "clueride",
 *   "name": "Clue Ride",
 *   "picture": "{image URL}",
 *   "gender": "male",
 *   "locale": "en",
 *   "updated_at": "2018-10-16T02:16:25.716Z",
 *   "email": "clueride@gmail.com",
 *   "email_verified": true
 * }
 */
@Immutable
public class ClueRideIdentity {
    private final String sub;
    private final String givenName;
    private final String familyName;
    private final String displayName;
    private final String nickName;
    private final URL pictureUrl;
    private final String gender;
    private final String locale;
    private final Date updatedAt;
    private final InternetAddress email;
    private final Boolean emailVerified;

    public ClueRideIdentity(Builder builder) {
        this.sub = requireNonNull(builder.getSub(), "sub is required");
        this.givenName = requireNonNull(builder.getGivenName(), "given name expects an Optional");
        this.familyName = requireNonNull(builder.getFamilyName(), "family name expects an Optional");
        this.nickName = requireNonNull(builder.getNickName(), "nickname is required");
        this.displayName = requireNonNull(builder.getDisplayName(), "display name is required");
        this.pictureUrl = builder.getPictureUrl();
        this.gender = builder.getGender();
        this.locale = builder.getLocale();
        this.updatedAt = builder.getUpdatedAt();
        this.email = builder.getEmail();
        this.emailVerified = builder.getEmailVerified();
    }

    public String getSub() {
        return sub;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public URL getPictureUrl() {
        return pictureUrl;
    }

    public String getGender() {
        return gender;
    }

    public String getLocale() {
        return locale;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public InternetAddress getEmail() {
        return email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
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

    public static class Builder {
        private String sub;
        private String givenName;
        private String familyName;
        private String nickName;
        private String displayName;
        private String pictureUrlString;
        private String gender;
        private String locale;
        private String emailString;
        private Boolean emailVerified;
        @Transient
        private URL pictureUrl;
        private Date updatedAt;
        private InternetAddress email;


        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(ClueRideIdentity clueRideIdentity) {
            return builder()
                    .withSub(clueRideIdentity.getSub())
                    .withEmailString(clueRideIdentity.getEmail().toString())
                    .withEmailVerified(clueRideIdentity.getEmailVerified())
                    .withDisplayName(clueRideIdentity.getDisplayName())
                    .withGivenName(clueRideIdentity.getGivenName())
                    .withFamilyName(clueRideIdentity.getFamilyName())
                    .withNickName(clueRideIdentity.getNickName())
                    .withPictureUrl(clueRideIdentity.getPictureUrl().toString())
                    .withGender(clueRideIdentity.getGender())
                    .withLocale(clueRideIdentity.getLocale())
                    .withUpdatedAt(clueRideIdentity.getUpdatedAt())
                    ;
        }

        public ClueRideIdentity build() {
            return new ClueRideIdentity(this);
        }

        public String getSub() {
            return sub;
        }

        public Builder withSub(String sub) {
            this.sub = sub;
            return this;
        }

        public String getGivenName() {
            return Objects.toString(givenName, "");
        }

        @JsonProperty("given_name")
        public Builder withGivenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        public String getFamilyName() {
            return Objects.toString(familyName, "");
        }

        @JsonProperty("family_name")
        public Builder withFamilyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        public String getNickName() {
            return nickName;
        }

        @JsonProperty("nickname")
        public Builder withNickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        public String getDisplayName() {
            return displayName;
        }

        @JsonProperty("name")
        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public String getPictureUrlString() {
            return pictureUrlString;
        }

        public URL getPictureUrl() {
            try {
                pictureUrl = new URL(pictureUrlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return pictureUrl;
        }

        @JsonProperty("picture")
        public Builder withPictureUrl(String pictureUrl) {
            this.pictureUrlString = pictureUrl;
            return this;
        }

        public Builder withPictureUrl(URL pictureUrl) {
            this.pictureUrl = pictureUrl;
            return this;
        }

        public String getGender() {
            return Objects.toString(gender, "");
        }

        public Builder withGender(String gender) {
            this.gender = gender;
            return this;
        }

        public String getLocale() {
            return locale;
        }

        public Builder withLocale(String locale) {
            this.locale = locale;
            return this;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        @JsonProperty("updated_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
        public Builder withUpdatedAt(Date updatedAt) {
            this.updatedAt= updatedAt;
            return this;
        }

        public String getEmailString() {
            return emailString;
        }

        public InternetAddress getEmail() {
            try {
                email = new InternetAddress(emailString);
            } catch (AddressException e) {
                e.printStackTrace();
            }
            return email;
        }

        @JsonProperty("email")
        public Builder withEmailString(String emailString) {
            this.emailString = emailString;
            return this;
        }

        public Builder withEmail(InternetAddress emailAddress) {
            this.email = emailAddress;
            return this;
        }

        public Boolean getEmailVerified() {
            return emailVerified;
        }

        @JsonProperty("email_verified")
        public Builder withEmailVerified(Boolean emailVerified) {
            this.emailVerified = emailVerified;
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
}
