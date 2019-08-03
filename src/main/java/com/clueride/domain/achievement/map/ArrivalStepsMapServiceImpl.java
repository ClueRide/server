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
 * Created by jett on 7/28/19.
 */
package com.clueride.domain.achievement.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.clueride.domain.badge.features.BadgeFeatures;
import com.clueride.domain.badge.features.BadgeFeaturesService;
import com.clueride.domain.location.Location;
import com.clueride.domain.location.LocationService;
import com.clueride.domain.step.Step;
import com.clueride.domain.step.StepService;

/**
 * Implementation of {@link ArrivalStepsMapService}.
 */
public class ArrivalStepsMapServiceImpl implements ArrivalStepsMapService {
    @Inject
    private Logger LOGGER;

    private final LocationService locationService;
    private final BadgeFeaturesService badgeFeaturesService;
    private final StepService stepService;
    private final ArrivalStepStore arrivalStepStore;

    private static Map<Integer, List<Integer>> arrivalStepsMap = new HashMap<>();

    @Inject
    public ArrivalStepsMapServiceImpl(
            LocationService locationService,
            BadgeFeaturesService badgeFeaturesService,
            StepService stepService,
            ArrivalStepStore arrivalStepStore
    ) {
        this.locationService = locationService;
        this.badgeFeaturesService = badgeFeaturesService;
        this.stepService = stepService;
        this.arrivalStepStore = arrivalStepStore;
    }

    @Override
    public Map<Integer, List<Integer>> buildArrivalStepsMap() {
        LOGGER.debug("Re-Building Arrival - Steps Map");
        /* Flush out any existing copy, we're rebuilding. */
        arrivalStepsMap = new HashMap<>();

        List<Location> locations = locationService.getThemeLocations();
        Map<String, Integer> locationIdPerName = new HashMap<>();
        for (Location location : locations) {
            locationIdPerName.put(location.getName(), location.getId());
        }
        List<BadgeFeatures> badgeFeaturesList = badgeFeaturesService.getThemedBadgeFeatures();
        List<Step> steps = new ArrayList<>();
        for (BadgeFeatures badgeFeatures : badgeFeaturesList) {
            steps.addAll(stepService.getAllStepsForBadge(badgeFeatures.getId()));
        }

        for (Step step : steps) {
            if (locationIdPerName.containsKey(step.getName())) {
                LOGGER.info("Found Step {} for Location {}", step.getId(), step.getName());
                arrivalStepsMap.put(locationIdPerName.get(step.getName()), Collections.singletonList(step.getId()));
            } else {
                LOGGER.info("No match found for Step {}", step);
            }
        }
        return arrivalStepsMap;
    }

    @Override
    public Map<Integer, List<Integer>> loadArrivalStepsMap() {
        LOGGER.debug("Loading Arrival -> Steps Map");
        /* Flush out any existing copy, we're rebuilding. */
        arrivalStepsMap = new HashMap<>();

        List<ArrivalStepEntity> arrivalStepEntityList = arrivalStepStore.getAllRecords();
        for (ArrivalStepEntity arrivalStepEntity : arrivalStepEntityList) {
            int locationId = arrivalStepEntity.getLocationId();
            int stepId = arrivalStepEntity.getStepId();
            List<Integer> attractionStepIds;
            if (arrivalStepsMap.containsKey(arrivalStepEntity.getLocationId())) {
                attractionStepIds = arrivalStepsMap.get(locationId);
            } else {
                attractionStepIds = new ArrayList<>();
                arrivalStepsMap.put(locationId, attractionStepIds);
            }
            attractionStepIds.add(stepId);
        }
        return arrivalStepsMap;
    }

    @Override
    public int validate(List<Integer> attractionIds) {
        int missingLocations = 0;

        List<Location> locations = locationService.getThemeLocations();
        Map<Integer, String> locationPerId = new HashMap<>();
        for (Location location : locations) {
            locationPerId.put(location.getId(), location.getName());
        }

        List<BadgeFeatures> badgeFeaturesList = badgeFeaturesService.getThemedBadgeFeatures();
        Map<Integer, String> stepNamePerId = new HashMap<>();
        for (BadgeFeatures badgeFeatures : badgeFeaturesList) {
            List<Step> steps = stepService.getAllStepsForBadge(badgeFeatures.getId());
            for (Step step : steps) {
                stepNamePerId.put(step.getId(), step.getName());
            }
        }

        // TODO: Sort out if I really want to pass in the list of Attraction IDs
//        for (Integer attractionId : attractionIds) {
        for (Integer attractionId : locationPerId.keySet()) {
            if (arrivalStepsMap.containsKey(attractionId)) {
                LOGGER.info("Found Step(s) for Location ID {} ({}): {} ({})",
                        attractionId,
                        locationPerId.get(attractionId),
                        arrivalStepsMap.get(attractionId).get(0),
                        stepNamePerId.get(arrivalStepsMap.get(attractionId).get(0))
                );
            } else {
                missingLocations++;
                LOGGER.warn("No Step found for Location ID {} ({})",
                        attractionId,
                        locationPerId.get(attractionId)
                );
            }
        }

        return missingLocations;
    }

}
