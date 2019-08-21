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
 * Created by jett on 6/17/19.
 */
package com.clueride.domain.achievement;

import java.util.Date;

import javax.annotation.concurrent.Immutable;

/**
 * Attributes for an Achievement for a given user.
 */
@Immutable
public class Achievement {
    private final int id;
    private final int stepId;
    private final int userId;
    private final String title;
    private final String postType;
    private final Date earned;

    public Achievement(AchievementEntity achievementEntity) {
        this.id = achievementEntity.getId();
        this.stepId = achievementEntity.getStepId();
        this.userId = achievementEntity.getUserId();
        this.title = achievementEntity.getTitle();
        this.postType = achievementEntity.getPostType();
        this.earned = achievementEntity.getEarned();
    }

    public int getId() {
        return id;
    }

    public int getStepId() {
        return stepId;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getPostType() {
        return postType;
    }

    public Date getEarned() {
        return earned;
    }

}
