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
package com.clueride.domain.badge.event;

import java.security.Principal;
import java.util.Date;
import java.util.Objects;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;

/**
 * Mutable instance of BadgeEvent.
 */
@Entity(name="badge_event")
public class BadgeEventBuilder {
    @Inject
    @Transient
    private Logger LOGGER;

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "badge_event_pk_sequence")
    @SequenceGenerator(name="badge_event_pk_sequence", sequenceName = "badge_event_id_seq", allocationSize = 1)
    private Integer id;

    @Column
    private Date timestamp;

    @Transient
    private Principal principal;

    @Column(name="member_id")
    private Integer memberId;

    @Column(name = "badgeos_id")
    private Integer badgeOSId;

    @Column(name="method_name")
    private String methodName;

    @Column(name="class_name")
    private String className;

    @Transient
    private Class methodClass;

    @Column(name="return_value")
    private String returnValueAsString;

    @Column(name="return_value_json")
    private String returnValueAsJson;

    @Transient
    private Object returnValue;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public BadgeEventBuilder () {}

    public BadgeEvent build() {
        return new BadgeEvent(this);
    }

    public static BadgeEventBuilder builder() {
        return new BadgeEventBuilder();
    }

    public static BadgeEventBuilder from(BadgeEvent badgeEvent) {
        return builder()
                .withId(badgeEvent.getId())
                .withPrincipal(badgeEvent.getPrincipal())
                .withMemberId(badgeEvent.getMemberId())
                .withBadgeOSId(badgeEvent.getBadgeOSId())
                .withTimestamp(badgeEvent.getTimestamp())
                .withMethodClass(badgeEvent.getMethodClass())
                .withClassName(badgeEvent.getMethodClass().getCanonicalName())
                .withMethodName(badgeEvent.getMethodName())
                .withReturnValue(badgeEvent.getReturnValue())
                .withReturnValueAsString(badgeEvent.getReturnValue().toString())
                ;
    }

    public Integer getId() {
        return id;
    }

    public BadgeEventBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public BadgeEventBuilder withTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public BadgeEventBuilder withMemberId(Integer memberId) {
        this.memberId = memberId;
        return this;
    }

    public Integer getBadgeOSId() {
        return badgeOSId;
    }

    public BadgeEventBuilder withBadgeOSId(Integer badgeOSId) {
        this.badgeOSId = badgeOSId;
        return this;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public BadgeEventBuilder withPrincipal(Principal principal) {
        this.principal = principal;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public BadgeEventBuilder withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public BadgeEventBuilder withClassName(String className) {
        this.className = className;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public Class getMethodClass() {
        return methodClass;
    }

    public BadgeEventBuilder withMethodClass(Class methodClass) {
        this.methodClass = methodClass;
        this.className = methodClass.getCanonicalName();
        return this;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public BadgeEventBuilder withReturnValue(Object returnValue) {
        this.returnValue = returnValue;
        this.returnValueAsString = Objects.toString(returnValue);
        try {
            this.returnValueAsJson = objectMapper.writeValueAsString(returnValue);
        } catch (JsonProcessingException e) {
            LOGGER.error("Trouble generating JSON: ", e);
        }
        return this;
    }

    private BadgeEventBuilder withReturnValueAsString(String returnValueAsString) {
        this.returnValueAsString = returnValueAsString;
        return this;
    }

    public String getReturnValueAsString() {
        return returnValueAsString;
    }

    public String getReturnValueAsJson() {
        return returnValueAsJson;
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
