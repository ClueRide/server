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
 * Created by jett on 2/28/19.
 */
package com.clueride.domain.puzzle.state;

import java.util.HashMap;
import java.util.Map;

import com.clueride.domain.puzzle.Puzzle;
import com.clueride.domain.puzzle.answer.Answer;
import com.clueride.domain.puzzle.answer.AnswerKey;
import static java.util.Objects.requireNonNull;

/**
 * Tracks the progress on the team's answers for a given Puzzle.
 */
public class PuzzleState {
    private Puzzle puzzle;
    private Map<AnswerKey, Integer> answerMap = new HashMap<>();

    public PuzzleState (Puzzle puzzle) {
        requireNonNull(puzzle, "Can't create Puzzle State for null puzzle");
        requireNonNull(puzzle.getAnswers(), "Can't create Puzzle State for puzzle without answers");

        this.puzzle = puzzle;
        for (Answer answer : puzzle.getAnswers()) {
            answerMap.put(answer.getKey(), 0);
        }
    }

    public Integer getPuzzleId() {
        return this.puzzle.getId();
    }

    public Map<AnswerKey, Integer> postAnswer(AnswerKey answerKey) {
        answerMap.merge(answerKey, 1, Integer::sum);
        return answerMap;
    }

    public AnswerKey getCorrectAnswer() {
        return this.puzzle.getCorrectAnswer();
    }

}
