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
 * Created by jett on 2/26/19.
 */
package com.clueride.domain.puzzle.answer;

import java.util.Map;

/**
 * Summary of the answers given during a certain outing for the given puzzle.
 */
public class AnswerSummary {
    private Integer puzzleId;
    private AnswerKey correctAnswer;
    private AnswerKey myAnswer;
    private Map<AnswerKey,Integer> answerMap;

    public Integer getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(Integer puzzleId) {
        this.puzzleId = puzzleId;
    }

    public AnswerKey getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(AnswerKey correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public AnswerKey getMyAnswer() {
        return myAnswer;
    }

    public void setMyAnswer(AnswerKey myAnswer) {
        this.myAnswer = myAnswer;
    }

    public Map<AnswerKey, Integer> getAnswerMap() {
        return answerMap;
    }

    public void setAnswerMap(Map<AnswerKey, Integer> answerMap) {
        this.answerMap = answerMap;
    }
}
