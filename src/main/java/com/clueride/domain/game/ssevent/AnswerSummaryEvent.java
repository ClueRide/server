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
 * Created by jett on 3/26/19.
 */
package com.clueride.domain.game.ssevent;

import javax.annotation.concurrent.Immutable;

import com.clueride.domain.puzzle.answer.AnswerSummary;

/**
 * Bundles up an {@link AnswerSummary} for turning into an SSEvent.
 */
@Immutable
public class AnswerSummaryEvent {
    private final String event;
    private final Integer outingId;
    private final AnswerSummary answerSummary;


    public AnswerSummaryEvent(
            String event,
            Integer outingId,
            AnswerSummary answerSummary
    ) {
        this.event = event;
        this.outingId = outingId;
        this.answerSummary = answerSummary;
    }

    public String getEvent() {
        return event;
    }

    public Integer getOutingId() {
        return outingId;
    }

    public AnswerSummary getAnswerSummary() {
        return answerSummary;
    }
}
