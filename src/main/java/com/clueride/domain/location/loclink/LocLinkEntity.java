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
 * Created by jett on 5/5/19.
 */
package com.clueride.domain.location.loclink;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents persistable instance of a Location Link.
 */
@Entity
@Table(name="loc_link")
public class LocLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loc_link_pk_sequence")
    @SequenceGenerator(name = "loc_link_pk_sequence", sequenceName = "loc_link_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name="url")
    private String link;

    public static LocLinkEntity builder() {
        return new LocLinkEntity();
    }

    public LocLink build() {
        return new LocLink(this);
    }

    public Integer getId() {
        return id;
    }

    public LocLinkEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getLink() {
        return link;
    }

    public LocLinkEntity withLink(String link) {
        this.link = link;
        return this;
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
