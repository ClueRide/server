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
 * Created by jett on 8/10/19.
 */
package com.clueride.domain.page;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Instance of a Page record.
 */
@Immutable
public class Page {
    private final Integer id;
    private final String pageSlug;
    private final Integer postId;
    private final String pageUrlAsString;
    private final String title;
    private final Integer categoryId;
    private final String categoryName;

    Page(PageEntity pageEntity) {
        this.id = pageEntity.getId();
        this.pageSlug = pageEntity.getPageSlug();
        this.postId = pageEntity.getPostId();
        this.pageUrlAsString = pageEntity.getPageUrlAsString();
        this.title = pageEntity.getTitle();
        this.categoryId = pageEntity.getCategoryId();
        this.categoryName = pageEntity.getCategoryName();
    }

    public Integer getId() {
        return id;
    }

    public String getPageSlug() {
        return pageSlug;
    }

    public Integer getPostId() {
        return postId;
    }

    public String getPageUrlAsString() {
        return pageUrlAsString;
    }

    public String getTitle() {
        return title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
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
