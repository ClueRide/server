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
 * Created by jett on 6/16/19.
 */
package com.clueride.domain.step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;

/**
 * Default implementation of {@link StepService}.
 */
public class StepServiceImpl implements StepService {
    @Inject
    private Logger LOGGER;

    /** Cached copy of what comes out of the database: steps per rollup. */
    private static Map<Integer, Set<Step>> subStepsPerRollup;
    private static Map<Integer, StepEntity> stepEntityMap;
    private static Set<Integer> allStepIds;
    private static Set<Integer> topLevelRollupIds;
    private static Map<Integer, Set<Step>> stepsPerBadge;

    private final StepStore stepStore;

    @Inject
    public StepServiceImpl(
            StepStore stepStore
    ) {
        this.stepStore = stepStore;
        if (subStepsPerRollup == null) {
            stepEntityMap = populateEntityMap();
            subStepsPerRollup = populateSubStepsPerRollup();
            allStepIds = populateAllSteps();
            topLevelRollupIds = populateTopLevelRollups();
            stepsPerBadge = populateStepsPerBadge();
        }
    }

    private Map<Integer, StepEntity> populateEntityMap() {
        Map<Integer, StepEntity> result = new HashMap<>();
        for (StepEntity stepEntity : stepStore.getAllSteps()) {
            result.put(stepEntity.getId(), stepEntity);
        }
        return result;
    }

    /**
     * Takes a many-to-many relationship and turns it into a flattened tree.
     * @return Map from a top-level Badge to the complete list of Sub Steps.
     */
    private Map<Integer, Set<Step>> populateSubStepsPerRollup() {
        Map<Integer, Set<Step>> stepsByBadgeId = new HashMap<>();

        for (StepEntity entity : stepEntityMap.values()) {
            int parentId = entity.getBadgeId();

            /* Parent Index. */
            Set<Step> parentEntities;
            if (stepsByBadgeId.keySet().contains(parentId)) {
                parentEntities = stepsByBadgeId.get(parentId);
            } else {
                parentEntities = new HashSet<>();
                stepsByBadgeId.put(parentId, parentEntities);
            }
            parentEntities.add(entity.build());
        }

        return stepsByBadgeId;
    }

    private Set<Integer> populateAllSteps() {
        return new HashSet<>(stepEntityMap.keySet());
    }

    private Set<Integer> populateTopLevelRollups() {
        Set<Integer> result = new HashSet<>();
        for (StepEntity entity : stepEntityMap.values()) {
            /* Find the list of top-level Badges. */
            if (!allStepIds.contains(entity.getBadgeId())) {
                result.add(entity.getBadgeId());
            }
        }
        return result;
    }

    private Map<Integer, Set<Step>> populateStepsPerBadge() {
        Map<Integer, Set<Step>> result = new HashMap<>();
        for (Integer badgeId : topLevelRollupIds) {
            Set<Step> steps = new HashSet<>();
            result.put(badgeId, steps);
            fillSubStepsForRollup(badgeId, steps);
        }
        return result;
    }

    private void fillSubStepsForRollup(Integer rollupId, Set<Step> steps) {
        if (!subStepsPerRollup.containsKey(rollupId)) {
            LOGGER.error("Problem: no children for this rollup: {}", rollupId);
            return;
        }

        for (Step step : subStepsPerRollup.get(rollupId)) {
            int classId = step.getClassId();
            if (subStepsPerRollup.containsKey(classId)) {
                fillSubStepsForRollup(classId, steps);
            } else {
                steps.add(step);
            }
        }

    }

    @Override
    public List<Step> getAllSteps() {
        List<Step> steps = new ArrayList<>();
        for (StepEntity entity : stepStore.getAllSteps()) {
            steps.add(entity.build());
        }
        return steps;
    }

    /**
     * This implementation walks a database view that holds a single
     * level of nesting, whereas any given Badge may have multiple levels.
     *
     * To return the complete set of Steps, each retrieved step should be
     * checked to make sure it doesn't have sub-steps as well. This leads
     * to a recursive solution; it looks for circular recursiveness to avoid
     * an infinite loop.
     *
     * @param badgeId unique identifier for a Badge.
     * @return Complete list of Steps including the expansion of steps which
     * are roll-ups of a nested level of sub-steps.
     */
    @Override
    public Set<Step> getAllStepsForBadge(int badgeId) {
        return stepsPerBadge.get(badgeId);
    }

    @Override
    public Map<Integer, Set<Integer>> getParentMap() {
        Map<Integer, Set<Integer>> result = new HashMap<>();
        for (Integer badgeId : topLevelRollupIds) {
            HashSet<Integer> stepIds = new HashSet<>();
            result.put(badgeId, stepIds);
            for (Step step : stepsPerBadge.get(badgeId)) {
                stepIds.add(step.getId());
            }
        }
        return result;
    }

    @Override
    public StepEntity getStep(Integer stepId) {
        return stepEntityMap.get(stepId);
    }

}
