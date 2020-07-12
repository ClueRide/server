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
package com.clueride.domain.account.member;

import com.clueride.auth.identity.ClueRideIdentity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static java.util.Objects.requireNonNull;

/**
 * Builder for {@link Member} that is persistable.
 */
@Entity
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_pk_sequence")
    @SequenceGenerator(name = "member_pk_sequence", sequenceName = "member_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "badgeos_id")
    private Integer badgeOSId;

    @Column(name = "display_name")
    private String displayName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "primary_email")
    private String emailAddress;
    //        private InternetAddress emailAddress;
    @Column
    private String phone;
    //        private Phonenumber.PhoneNumber phone;

    @Column(name="image_url")
    private String imageUrl;

    public MemberEntity() {
    }

    public Member build() {
        return new Member(this);
    }

    public static MemberEntity builder() {
        return new MemberEntity();
    }

    public static MemberEntity from(Member member) {
        requireNonNull(member);
        return builder()
                .withId(member.getId())
                .withBadgeOSId(member.getBadgeOSId())
                .withPhone(member.getPhoneNumber())
                .withImageUrl(member.getImageUrl())
                .withFirstName(member.getFirstName())
                .withLastName(member.getLastName())
                .withDisplayName(member.getDisplayName())
                .withEmailAddress(member.getEmailAddress());
    }

    public static MemberEntity from(ClueRideIdentity clueRideIdentity) {
        requireNonNull(clueRideIdentity);
        return builder()
                .withFirstName(clueRideIdentity.getGivenName().get())
                .withLastName(clueRideIdentity.getFamilyName().get())
                .withEmailAddress(clueRideIdentity.getEmail().toString())
                .withDisplayName(clueRideIdentity.getDisplayName())
                .withImageUrl(clueRideIdentity.getPictureUrl().toString())
                ;
    }

    public Integer getId() {
        return id;
    }

    public MemberEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public MemberEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getBadgeOSId() {
        return badgeOSId;
    }

    public MemberEntity withBadgeOSId(Integer badgeOSId) {
        this.badgeOSId = badgeOSId;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public MemberEntity withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public MemberEntity withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public MemberEntity withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public MemberEntity withEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MemberEntity withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MemberEntity withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
