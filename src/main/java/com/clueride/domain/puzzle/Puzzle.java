/*
 * Copyright 2015 Jett Marks
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
 * Created by jett on 11/23/15.
 */
package com.clueride.domain.puzzle;

import java.util.ArrayList;
import java.util.List;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.puzzle.answer.AnswerEntity;
import com.clueride.domain.puzzle.answer.AnswerKey;

@Immutable
public class Puzzle {
    private final Integer id;
    private final String name;
    private final Integer locationId;
    private final String locationName;
    private final String question;
    private List<AnswerEntity> answers = new ArrayList<>();
    private final AnswerKey correctAnswer;
    private final Integer points;

    Puzzle(PuzzleEntity builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.locationId = builder.getLocationEntity().getId();
        this.locationName = builder.getLocationEntity().getName();
        this.question = builder.getQuestion();
        this.answers = builder.getAnswerEntities();
        this.correctAnswer = builder.getCorrectAnswer();
        this.points = builder.getPoints();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getQuestion() {
        return question;
    }

    public AnswerKey getCorrectAnswer() {
        return correctAnswer;
    }

    public Integer getPoints() {
        return points;
    }

    public List<AnswerEntity> getAnswers() {
        return answers;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
