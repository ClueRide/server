/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 11/25/17.
 */
package com.clueride.domain.badge.event;

import java.security.Principal;
import java.util.Date;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * DTO for the data captured for Badge-worthy deeds.
 */
@Immutable
public class BadgeEvent {
    private Integer id;
    private Date timestamp;
    private Principal principal;
    private Integer memberId;
    private Integer badgeOSId;
    private String methodName;
    private Class methodClass;
    private Object returnValue;
    private String returnValueAsJson;

    BadgeEvent(BadgeEventEntity builder) {
        this.id = builder.getId();
        this.timestamp = builder.getTimestamp();
        this.principal = builder.getPrincipal();
        this.memberId = builder.getMemberId();
        this.badgeOSId = builder.getBadgeOSId();
        this.methodClass = builder.getMethodClass();
        this.methodName = builder.getMethodName();
        this.returnValue = builder.getReturnValue();
        this.returnValueAsJson = builder.getReturnValueAsJson();
    }

    public Integer getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public Integer getBadgeOSId() {
        return badgeOSId;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class getMethodClass() {
        return methodClass;
    }

    public Object getReturnValue() {
        return returnValue;
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
        return new ToStringBuilder(this)
                .append("\nbadgeOSId", badgeOSId)
                .append("\nmethodName", methodName)
                .append("\nmethodClass", methodClass)
                .append("\nreturnValueAsJson", returnValueAsJson)
                .toString();
    }
}
