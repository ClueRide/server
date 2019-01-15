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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.location.LocationBuilder;
import com.clueride.domain.puzzle.answer.Answer;
import com.clueride.domain.puzzle.answer.AnswerKey;

/**
 * TODO: Description.
 */
@Entity(name = "PuzzleBuilder")
@Table(name = "puzzle")
public final class PuzzleBuilder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "puzzle_pk_sequence")
    @SequenceGenerator(name = "puzzle_pk_sequence", sequenceName = "puzzle_id_seq", allocationSize = 1)
    private Integer id;

    private String name;
    private String question;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "puzzleBuilder"
    )
    private List<Answer> answers = new ArrayList<>();

    @Column(name = "correct_answer")
    private AnswerKey correctAnswer;
    private Integer points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationBuilder locationBuilder;

    public static PuzzleBuilder builder() {
        return new PuzzleBuilder();
    }

    public static PuzzleBuilder from(Puzzle instance) {
        return builder()
                .withId(instance.getId())
                .withName(instance.getName())
                .withQuestion(instance.getQuestion())
                .withCorrectAnswer(instance.getCorrectAnswer())
                .withAnswers(instance.getAnswers())
                .withPoints(instance.getPoints())
                ;
    }

    public String getQuestion() {
        return question;
    }

    public PuzzleBuilder withQuestion(String question) {
        this.question = question;
        return this;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public PuzzleBuilder withAnswers(List<Answer> answers) {
        this.answers = answers;
        return this;
    }

    public AnswerKey getCorrectAnswer() {
        return correctAnswer;
    }

    public PuzzleBuilder withCorrectAnswer(AnswerKey correctAnswer) {
        this.correctAnswer = correctAnswer;
        return this;
    }

    public Integer getPoints() {
        return points;
    }

    public PuzzleBuilder withPoints(Integer points) {
        this.points = points;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public PuzzleBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public PuzzleBuilder withLocationBuilder(LocationBuilder locationBuilder) {
        this.locationBuilder = locationBuilder;
        return this;
    }

    public LocationBuilder getLocationBuilder() {
        return locationBuilder;
    }

    public String getName() {
        return name;
    }

    public PuzzleBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Puzzle build() {
        return new Puzzle(this);
    }

    public PuzzleBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public PuzzleBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PuzzleBuilder setQuestion(String question) {
        this.question = question;
        return this;
    }

    public PuzzleBuilder setAnswers(List<Answer> answers) {
        this.answers = answers;
        return this;
    }

    public PuzzleBuilder setCorrectAnswer(AnswerKey correctAnswer) {
        this.correctAnswer = correctAnswer;
        return this;
    }

    public PuzzleBuilder setPoints(Integer points) {
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
