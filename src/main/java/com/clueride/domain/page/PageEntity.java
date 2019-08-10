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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class for any type of Page Link, but carries
 * specific attributes appropriate for Word Press pages.
 */
@Entity(name = "Page")
@Table(name = "page")
public class PageEntity {
    @Id
    private Integer id;

    @Column(name = "page_slug")
    private String pageSlug;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "page_url")
    private String pageUrlAsString;

    private String title;

    @Column(name = "page_category")
    private Integer categoryId;

    @Column(name = "category_name")
    private String categoryName;

    public Page build() {
        return new Page(this);
    }

    public Integer getId() {
        return id;
    }

    public PageEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getPageSlug() {
        return pageSlug;
    }

    public PageEntity withPageSlug(String pageSlug) {
        this.pageSlug = pageSlug;
        return this;
    }

    public Integer getPostId() {
        return postId;
    }

    public PageEntity withPostId(Integer postId) {
        this.postId = postId;
        return this;
    }

    public String getPageUrlAsString() {
        return pageUrlAsString;
    }

    public PageEntity withPageUrlAsString(String pageUrlAsString) {
        this.pageUrlAsString = pageUrlAsString;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PageEntity withTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public PageEntity withCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public PageEntity withCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

}
