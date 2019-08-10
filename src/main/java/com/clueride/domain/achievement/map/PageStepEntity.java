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
package com.clueride.domain.achievement.map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Instances of Page to Step Map entries.
 */
@Entity(name = "PageStep")
@Table(name = "page_to_step_map")
public class PageStepEntity {
    @Id
    private Integer id;

    @Column(name = "page_id")
    private Integer pageId;

    @Column(name = "step_id")
    private Integer stepId;

    public PageStep build() {
        return new PageStep(this);
    }

    public Integer getId() {
        return id;
    }

    public PageStepEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getPageId() {
        return pageId;
    }

    public PageStepEntity withPageId(Integer pageId) {
        this.pageId = pageId;
        return this;
    }

    public Integer getStepId() {
        return stepId;
    }

    public PageStepEntity withStepId(Integer stepId) {
        this.stepId = stepId;
        return this;
    }

}
