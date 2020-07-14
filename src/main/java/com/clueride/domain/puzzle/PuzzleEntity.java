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
 * Created by jett on 1/13/19.
 */
package com.clueride.domain.puzzle;

import com.clueride.domain.location.LocationEntity;
import com.clueride.domain.puzzle.answer.AnswerEntity;
import com.clueride.domain.puzzle.answer.AnswerKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistable Builder for {@link Puzzle} instances.
 */
@Entity
@Table(name = "puzzle")
public class PuzzleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "puzzle_pk_sequence")
    @SequenceGenerator(name = "puzzle_pk_sequence", sequenceName = "puzzle_id_seq", allocationSize = 1)
    private Integer id;

    private String name;
    private String question;
    private Integer points;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            mappedBy = "puzzleEntity"
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("answers")
    private List<AnswerEntity> answerEntities = new ArrayList<>();

    @Column(name = "correct_answer")
    private AnswerKey correctAnswer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private LocationEntity locationEntity;

    /* Supports REST API. */
    @Transient
    private Integer locationId;
    @Transient
    private String locationName;

    public static PuzzleEntity builder() {
        return new PuzzleEntity();
    }

    public static PuzzleEntity from(Puzzle instance) {
        return builder()
                .withId(instance.getId())
                .withName(instance.getName())
                .withQuestion(instance.getQuestion())
                .withCorrectAnswer(instance.getCorrectAnswer())
                .withAnswers(instance.getAnswers())
                .withPoints(instance.getPoints())
                ;
    }

    public Integer getLocationId() {
        return locationId;
    }

    /* Supports REST API. */
    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    /* Supports REST API. */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getQuestion() {
        return question;
    }

    public PuzzleEntity withQuestion(String question) {
        this.question = question;
        return this;
    }

    public List<AnswerEntity> getAnswerEntities() {
        return answerEntities;
    }

    public PuzzleEntity withAnswers(List<AnswerEntity> answerEntities) {
        this.answerEntities = answerEntities;
        return this;
    }

    public AnswerKey getCorrectAnswer() {
        return correctAnswer;
    }

    public PuzzleEntity withCorrectAnswer(AnswerKey correctAnswer) {
        this.correctAnswer = correctAnswer;
        return this;
    }

    public Integer getPoints() {
        return points;
    }

    public PuzzleEntity withPoints(Integer points) {
        this.points = points;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public PuzzleEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public PuzzleEntity withLocationBuilder(LocationEntity locationEntity) {
        this.locationEntity = locationEntity;
        return this;
    }

    public LocationEntity getLocationEntity() {
        return locationEntity;
    }

    public String getName() {
        return name;
    }

    public PuzzleEntity withName(String name) {
        this.name = name;
        return this;
    }

    public Puzzle build() {
        return new Puzzle(this);
    }

    public PuzzleEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public PuzzleEntity setName(String name) {
        this.name = name;
        return this;
    }

    public PuzzleEntity setQuestion(String question) {
        this.question = question;
        return this;
    }

    public PuzzleEntity setAnswerEntities(List<AnswerEntity> answerEntities) {
        this.answerEntities = answerEntities;
        return this;
    }

    public PuzzleEntity setCorrectAnswer(AnswerKey correctAnswer) {
        this.correctAnswer = correctAnswer;
        return this;
    }

    public PuzzleEntity setPoints(Integer points) {
        this.points = points;
        return this;
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
