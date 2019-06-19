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
 * Created by jett on 6/17/19.
 */
package com.clueride.domain.achievement.parser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * What comes out of the BadgeOS system for a user's Achievements.
 */
@Entity
@Table(name = "wp_usermeta")
public class RawAchievement {
    @Id
    @Column(name = "umeta_id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "meta_value")
    private String value;

    /** Only used for the queries. */
    @Column(name = "meta_key")
    private String metaKey;

    public RawAchievement() {
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getValue() {
        return value;
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
