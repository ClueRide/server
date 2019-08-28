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
 * Created by jett on 1/31/19.
 */
package com.clueride.domain.image;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * JPA Entity for Images that are referred to by ID.
 *
 * This maps a file's URL with a unique ID via database table.
 */
@Entity
@Table(name = "image")
public class ImageLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_pk_sequence")
    @SequenceGenerator(name = "image_pk_sequence", sequenceName = "image_id_seq", allocationSize = 1)
    private Integer id;

    @Column
    private String url;

    /** Empty constructor for JPA. */
    public ImageLinkEntity() {}

    public ImageLink build() {
        return new ImageLink(this);
    }

    public static ImageLinkEntity from(ImageLink imageLink) {
        return ImageLink.builder()
                .withId(imageLink.getId())
                .withUrl(imageLink.getUrl());
    }

    /** String constructor for REST API. */
    public ImageLinkEntity(String featuredImage) {
        this.url = featuredImage;
    }

    public Integer getId() {
        return id;
    }

    public ImageLinkEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ImageLinkEntity withUrl(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
