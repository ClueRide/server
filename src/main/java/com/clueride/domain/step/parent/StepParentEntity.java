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
 * Created by jett on 6/19/19.
 */
package com.clueride.domain.step.parent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Connection between Steps and the Achievements they would earn --
 * their parents.
 */
@Entity
@Table(name = "sub_achievement_with_parent")
public class StepParentEntity {

    @Id
    @Column(name = "step_id")
    private int id;

    @Column(name = "parent_id")
    private int parentId;

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

}
