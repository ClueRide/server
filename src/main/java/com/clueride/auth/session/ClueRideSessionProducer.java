/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 12/1/18.
 */
package com.clueride.auth.session;

import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Responds to the Event that requests placing Principal instances into user's session.
 *
 * Design on the Wiki: http://bikehighways.wikidot.com/principal-into-session.
 */
@RequestScoped
public class ClueRideSessionProducer implements Serializable {
    @Inject
    private Logger LOGGER;

    @Inject
    private ClueRideSessionService clueRideSessionService;


    private String accessToken;

    /* Records the key info each time a request is made. */
    public void handleUserRegisteredEvent(@Observes @ClueRideSession String accessToken) {
        this.accessToken = accessToken;
    }

    @Produces
    @SessionScoped
    @ClueRideSession
    /* Invoked once per session, but maintaining session is tricky across platforms. */
    private ClueRideSessionDto produceClueRideSessionDto() {
        return clueRideSessionService.loadSessionForExistingAccount(accessToken);
    }
}
