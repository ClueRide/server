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
 * Created by jett on 7/29/19.
 */
package com.clueride.domain.achievement.map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Persistable Entity for the Many-to-Many map from Attraction to the Step
 * awarded when arriving at the Attraction.
 *
 * In the database, there are generally multiple awarded achievements (Steps)
 * per Location ID. To turn this into a map, the records are keyed by the Location ID
 * and the multiple Steps are collected in a List.
 *
 * This is starting out as Read-Only until we come up with a more automated way to
 * maintain the records in the table.
 */
@Entity
@Table(name = "arrival_to_step_map")
public class ArrivalStepEntity {
    @Id
    private int id;

    @Column(name = "location_id")
    private int locationId;

    @Column(name = "step_id")
    private int stepId;

    @Column(name = "badge_id")
    private int badgeId;

    public int getId() {
        return id;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getStepId() {
        return stepId;
    }

    public int getBadgeId() {
        return badgeId;
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
