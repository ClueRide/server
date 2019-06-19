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
package com.clueride.domain.achievement.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;

/**
 * Knows how to parse the '_badgeos_achievements' value for the 'wp_usermeta' table.
 */
public class ParsedAchievementServiceImpl implements ParsedAchievementService {
    @Inject
    private Logger LOGGER;

    @Inject
    private RawAchievementStore rawAchievementStore;

    @Override
    public List<ParsedAchievement> getAchievementForUser(int userId) {
        return parseRawAchievement(
                rawAchievementStore.getRawAchievementsForUser(userId)
        );
    }

    private List<ParsedAchievement> parseRawAchievement(RawAchievement rawAchievement) {
        List<ParsedAchievement> achievements = new ArrayList<>();
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

    private ParsedAchievement elementStringToAchievement(String[] attributes) {
        Map<String, String> attributeMap = mapKeyValuePairs(attributes);

        ParsedAchievement parsedAchievement = new ParsedAchievement();
        for (String key : attributeMap.keySet()) {
            String value = attributeMap.get(key);
            if (key.contains("ID")) {
                parsedAchievement.withPostId(getId(value));
            } else if (key.contains("title")) {
                parsedAchievement.withTitle(getTitle(value));
            } else if (key.contains("post_type")) {
                parsedAchievement.withPostType(getPostType(value));
            } else if (key.contains("date_earned")) {
                parsedAchievement.withEarned(getDateEarned(value));
            }
        }

        return parsedAchievement;
    }

    /* Map the raw WP line into Key:Value pairs. */
    private Map<String, String> mapKeyValuePairs(String[] attributes) {
        Map<String, String> attributeMap = new HashMap<>();
        for (int i=0; i<attributes.length-1; i+=2) {
            attributeMap.put(attributes[i], attributes[i+1]);
        }
        return attributeMap;
    }

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
        return attribute.substring(indexOfColon).replace("\"", "");
    }

    private Date getDateEarned(String attribute) {
        long epochMilliseconds = Long.parseLong(attribute.substring(2)) * 1000;
        return new Date(epochMilliseconds);
    }

}
