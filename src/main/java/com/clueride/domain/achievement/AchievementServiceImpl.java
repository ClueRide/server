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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.clueride.domain.achievement.raw.RawAchievement;
import com.clueride.domain.achievement.raw.RawAchievementStore;

/**
 * Knows how to parse the '_badgeos_achievements' value for the 'wp_usermeta' table.
 */
public class AchievementServiceImpl implements AchievementService {
    @Inject
    private Logger LOGGER;

    @Inject
    private RawAchievementStore rawAchievementStore;

    @Override
    public List<Achievement> getAchievementsForUser(int userId) {
        return parseRawAchievement(
            rawAchievementStore.getRawAchievementsForUser(userId)
        );
    }

    /**
     * Turns Single field from BadgeOS into a list of Achievements.
     *
     * "Lines" are delineated by enclosing curly-brackets.
     * Each line is an achievement with a set of name-value pairs.
     *
     * @param rawAchievement from BadgeOS.
     * @return List of Achievements.
     */
    private List<Achievement> parseRawAchievement(RawAchievement rawAchievement) {
        List<Achievement> achievements = new ArrayList<>();
        if (rawAchievement.getValue().length() > 0) {
            String[] lines = rawAchievement.getValue().split("\\{");
            for (String line : lines) {
                /* Only concerned about elements with an "ID" field. */
                if (line.contains("ID") && line.length() > 15) {
                    String[] attributes = line.split(";");
                    try {
                        achievements.add(elementStringToAchievement(attributes));
                    } catch (Exception e) {
                        LOGGER.error("Problem with line {}", line);
                        throw(e);
                    }
                }
            }
        }
        return achievements;
    }

    // TODO: May want to filter by either step or non-step
    private Achievement elementStringToAchievement(String[] attributes) {
        Map<String, String> attributeMap = mapKeyValuePairs(attributes);

        Achievement achievement = new Achievement();
        for (String key : attributeMap.keySet()) {
            String value = attributeMap.get(key);
            if (key.contains("ID")) {
                achievement.withPostId(getId(value));
            } else if (key.contains("title")) {
                achievement.withTitle(getTitle(value));
            } else if (key.contains("post_type")) {
                achievement.withPostType(getPostType(value));
            } else if (key.contains("date_earned")) {
                achievement.withEarned(getDateEarned(value));
            }
        }

        return achievement;
    }

    /* Map the raw BadgeOS line into Key:Value pairs. */
    private Map<String, String> mapKeyValuePairs(String[] attributes) {
        Map<String, String> attributeMap = new HashMap<>();
        for (int i=0; i<attributes.length-1; i+=2) {
            attributeMap.put(attributes[i], attributes[i+1]);
        }
        return attributeMap;
    }

    /* Field-specific parsing. */

    private int getId(String attribute) {
        if (attribute.startsWith("i")) {
            return Integer.parseInt(attribute.substring(2));
        } else if (attribute.startsWith("s")) {
            return Integer.parseInt(attribute.split(":")[2].replace("\"", ""));
        } else {
            LOGGER.error("Misunderstood format for ID: {}", attribute);
            return -1;
        }
    }

    private String getPostType(String attribute) {
        return attribute.split(":")[2].replace("\"", "");
    }

    private String getTitle(String attribute) {
        int indexOfColon = attribute.indexOf(":",2);
        return attribute.substring(indexOfColon + 1).replace("\"", "");
    }

    private Date getDateEarned(String attribute) {
        long epochMilliseconds = Long.parseLong(attribute.substring(2)) * 1000;
        return new Date(epochMilliseconds);
    }

}
