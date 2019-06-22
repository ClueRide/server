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
import java.util.List;

import javax.inject.Inject;

/**
 * Default implementation of {@link StepService}.
 */
public class StepServiceImpl implements StepService {

    @Inject
    private StepStore stepStore;

    @Override
    public List<Step> getAllSteps() {
        List<Step> steps = new ArrayList<>();
        for (StepEntity entity : stepStore.getAllSteps()) {
            steps.add(entity.build());
        }
        return steps;
    }

}
