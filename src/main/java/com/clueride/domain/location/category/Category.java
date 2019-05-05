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
 * Created by jett on 5/4/19.
 */
package com.clueride.domain.location.category;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a Category (grouping) of Location Types.
 *
 * Not clear at the time of creation of this class whether this will remain hierarchical,
 * but it is starting out this way.
 */
@Immutable
public class Category {
    private final int id;
    private final String name;
    private final String description;
    private final String icon;
    private final String iconColor;

    Category(CategoryEntity categoryEntity) {
        this.id = categoryEntity.getId();
        this.name = categoryEntity.getName();
        this.description = categoryEntity.getDescription();
        this.icon = categoryEntity.getIcon();
        this.iconColor = categoryEntity.getIconColor();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getIconColor() {
        return iconColor;
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
