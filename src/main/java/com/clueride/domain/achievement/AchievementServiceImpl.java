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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.slf4j.Logger;

/**
 * Implements {@link AchievementService}.
 */
public class AchievementServiceImpl implements AchievementService {
    @Inject
    private Logger LOGGER;

    @Inject
    private AchievementStore achievementStore;

    @Override
    public List<Achievement> getAchievementsForUser(int userId) {
        return StreamSupport.stream(
                achievementStore.getAchievementsForUser(userId).spliterator(),
                false
        ).map(
                AchievementServiceImpl::build
        ).collect(
                Collectors.toList()
        );
    }

    public static Achievement build(AchievementEntity achievementEntity) {
        return achievementEntity.build();
    }

}
