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

/**
 * Attributes for an Achievement for a given user.
 */
public class Achievement {
    private int stepId;
    private String title;
    private String postType;
    private Date earned;

    public int getStepId() {
        return stepId;
    }

    /** PostID == StepID. */
    public Achievement withPostId(int postId) {
        this.stepId = postId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Achievement withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPostType() {
        return postType;
    }

    public Achievement withPostType(String postType) {
        this.postType = postType;
        return this;
    }

    public Date getEarned() {
        return earned;
    }

    public Achievement withEarned(Date earned) {
        this.earned = earned;
        return this;
    }

}
