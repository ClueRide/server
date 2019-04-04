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
 * Created by jett on 4/3/19.
 */
package com.clueride.domain.image;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Holds the URL link for a particular Image stored on the file system.
 */
@Immutable
public class ImageLink {
    private final Integer id;
    private final String url;

    /**
     * Default constructor
     * @param imageLinkEntity mutable instance that is persisted to the database.
     */
    public ImageLink(ImageLinkEntity imageLinkEntity) {
        this.id = imageLinkEntity.getId();
        this.url = imageLinkEntity.getUrl();
    }

    /**
     * Obtain a Builder instance for this class.
     * @return empty Builder for an {@link ImageLink}.
     */
    public static ImageLinkEntity builder() {
        return new ImageLinkEntity();
    }

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
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
