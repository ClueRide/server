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
 * Created by jett on 1/1/19.
 */
package com.clueride.domain.outing;

import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import org.slf4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 * Implementation of OutingService.
 */
public class OutingServiceImpl implements OutingService {
    @Inject
    private Logger LOGGER;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    @Inject
    private OutingStore outingStore;

    @Override
    public Outing getById(Integer outingId) {
        LOGGER.debug("Retrieving outing for ID: {}", outingId);
        return new Outing(outingStore.getOutingById(outingId));
    }

    @Override
    public OutingView getViewById(Integer outingId) {
        LOGGER.debug("Retrieving outing view for ID: {}", outingId);
        return outingStore.getOutingViewById(outingId).build();
    }

    @Override
    public OutingView getActiveOutingView() {
        if (clueRideSessionDto.hasNoOuting()) {
            throw new NoSessionOutingException();
        }
        return outingStore.getOutingViewById(clueRideSessionDto.getOutingId()).build();
    }

    @Override
    public OutingView setCourseForEternalOuting(Integer courseId) {
        return outingStore.setCourseForEternalOuting(courseId).build();
    }

}
