package com.clueride.domain.puzzle.answer;

import com.clueride.domain.puzzle.PuzzleEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created by jett on 11/23/15.
 */
@Entity
@Table(name = "answer")
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="answer_pk_sequence")
    @SequenceGenerator(name="answer_pk_sequence",sequenceName="answer_id_seq", allocationSize=1)
    private Integer id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "puzzle_id")
    private PuzzleEntity puzzleEntity;

    @Column(name = "answer_key") private AnswerKey answerKey;
    @Column(name = "answer") private String answer;

    public AnswerEntity() {}

    public AnswerEntity(AnswerKey answerKey, String answer) {
        this.answerKey = answerKey;
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public AnswerEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public AnswerKey getKey() {
        return answerKey;
    }

    public AnswerEntity setKey(AnswerKey answerKey) {
        this.answerKey = answerKey;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public AnswerEntity setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public AnswerEntity withPuzzleBuilder(PuzzleEntity puzzleEntity) {
        this.puzzleEntity = puzzleEntity;
        return this;
    }

    public AnswerEntity withAnswerKey(AnswerKey answerKey) {
        this.answerKey = answerKey;
        return this;
    }

    public AnswerEntity withAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
