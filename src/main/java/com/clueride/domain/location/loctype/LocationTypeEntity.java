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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.clueride.domain.location.category.CategoryEntity;

/**
 * Persistable Builder for {@link LocationType} instances.
 */
@Entity
@Table(name = "location_type")
@JsonIgnoreProperties(ignoreUnknown = true)
public final class LocationTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_type_pk_sequence")
    @SequenceGenerator(name = "location_type_pk_sequence", sequenceName = "location_type_id_seq", allocationSize = 1)
    private Integer id;

    private String name;
    private String icon;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @Column(name = "theme_id")
    private Integer themeId;

    /**
     * Generate instance from Builder's values.
     *
     * @return instance of LocationType based on Builder's values.
     */
    public LocationType build() {
        return new LocationType(this);
    }

    public static LocationTypeEntity builder() {
        return new LocationTypeEntity();
    }

    /**
     * Creates Builder from a given instance of LocationType.
     *
     * @param locationType the instance whose data to use.
     * @return Mutable instance of the Builder for the Location Type.
     */
    public static LocationTypeEntity from(LocationType locationType) {
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

    public LocationTypeEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public LocationTypeEntity withName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public LocationTypeEntity withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public LocationTypeEntity withIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public CategoryEntity getCategory() {
        return categoryEntity;
    }

    public LocationTypeEntity withCategory(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
        return this;
    }

    public Integer getThemeId() {
        return themeId;
    }

}
