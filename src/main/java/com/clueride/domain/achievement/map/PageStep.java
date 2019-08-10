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

import javax.annotation.concurrent.Immutable;

/**
 * Instance of a Page to Step Map record.
 */
@Immutable
public class PageStep {
    private final Integer id;
    private final Integer pageId;
    private final Integer stepId;

    PageStep(PageStepEntity pageStepEntity) {
        this.id = pageStepEntity.getId();
        this.pageId = pageStepEntity.getPageId();
        this.stepId = pageStepEntity.getStepId();
    }

    public Integer getId() {
        return id;
    }

    public Integer getPageId() {
        return pageId;
    }

    public Integer getStepId() {
        return stepId;
    }

}
