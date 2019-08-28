/**
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created 11/17/15.
 */
package com.clueride.domain.account.member;

import java.io.Serializable;
import java.util.Collections;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Member implements Serializable {
    private int id;
    private Integer badgeOSId;
    private String displayName;
    private String firstName;
    private String lastName;
    // TODO: CA-272 bring in support for Email Addresses
//    private InternetAddress emailAddress;
    private String emailAddress;
    // TODO: CA-272 Bring in support for Phone Numbers
//    private Phonenumber.PhoneNumber phone;
    private String phoneNumber;
    private String imageUrl;

    /** Supporting Jackson. */
    public Member() {}

    public Member(String name) {
        this.displayName = name;
    }

    public Member(MemberEntity builder) {
        this.id = builder.getId();
        this.badgeOSId = builder.getBadgeOSId();
        this.displayName = builder.getDisplayName();
        this.firstName = builder.getFirstName();
        this.lastName = builder.getLastName();
        this.emailAddress = builder.getEmailAddress();
        this.phoneNumber = builder.getPhone();
        this.imageUrl = builder.getImageUrl();
    }

    public int getId() {
        return id;
    }

    public Integer getBadgeOSId() {
        return badgeOSId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, Collections.singletonList("id"));
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
