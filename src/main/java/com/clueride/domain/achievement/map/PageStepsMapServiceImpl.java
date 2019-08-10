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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;

/**
 * Implementation of {@link PageStepsMapService}.
 */
public class PageStepsMapServiceImpl implements PageStepsMapService {
    @Inject
    private Logger LOGGER;

    @Inject
    private PageStepsStore pageStepsStore;

    private static HashMap<Integer, List<Integer>> pageStepsMap;

    @Override
    public Map<Integer, List<Integer>> loadMap() {
        LOGGER.debug("Loading Page -> Steps Map");
        /* Flush out any existing copy, we're re-loading. */
        pageStepsMap = new HashMap<>();

        List<PageStepEntity> pageStepEntityList = pageStepsStore.getAllRecords();
        for (PageStepEntity entity : pageStepEntityList) {
            int pageId = entity.getPageId();
            int stepId = entity.getStepId();
            List<Integer> pageStepIds;
            if (pageStepsMap.containsKey(pageId)) {
                pageStepIds = pageStepsMap.get(pageId);
            } else {
                pageStepIds = new ArrayList<>();
                pageStepsMap.put(pageId, pageStepIds);
            }
            pageStepIds.add(stepId);
        }
        return pageStepsMap;
    }

}
