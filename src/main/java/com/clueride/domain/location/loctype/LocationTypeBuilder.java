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
package com.clueride.domain.location.loctype;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * Persistable Builder for {@link LocationType} instances.
 */
@Entity(name = "location_type")
public final class LocationTypeBuilder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_type_pk_sequence")
    @SequenceGenerator(name = "location_type_pk_sequence", sequenceName = "location_type_id_seq", allocationSize = 1)
    private Integer id;

    private String name;
    private String icon;
    private String description;

    /**
     * Generate instance from Builder's values.
     *
     * @return instance of LocationType based on Builder's values.
     */
    public LocationType build() {
        return new LocationType(this);
    }

    public static LocationTypeBuilder builder() {
        return new LocationTypeBuilder();
    }

    /**
     * Creates Builder from a given instance of LocationType.
     *
     * @param locationType the instance whose data to use.
     * @return Mutable instance of the Builder for the Location Type.
     */
    public static LocationTypeBuilder from(LocationType locationType) {
        return builder()
                .withId(locationType.getId())
                .withName(locationType.getName())
                .withDescription(locationType.getDescription())
                .withIcon(locationType.getIcon())
                ;
    }

    public Integer getId() {
        return id;
    }

    public LocationTypeBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public LocationTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public LocationTypeBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public LocationTypeBuilder withIcon(String icon) {
        this.icon = icon;
        return this;
    }

}
