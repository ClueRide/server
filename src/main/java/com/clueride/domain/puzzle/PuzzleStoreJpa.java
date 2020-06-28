/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 10/19/17.
 */
package com.clueride.domain.puzzle;

import com.clueride.domain.location.LocationEntity;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.util.List;

/**
 * Implementation of the Puzzle Store.
 */
public class PuzzleStoreJpa implements PuzzleStore {

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public Integer addNew(PuzzleEntity puzzleEntity) {
        try {
            userTransaction.begin();
            entityManager.persist(puzzleEntity);
            userTransaction.commit();
        } catch (NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
            e.printStackTrace();
        }
        return puzzleEntity.getId();
    }

    @Override
    public PuzzleEntity getPuzzleById(Integer id) {
        PuzzleEntity puzzleEntity;
        puzzleEntity = entityManager.find(PuzzleEntity.class, id);
        return puzzleEntity;
    }

    @Override
    public List<PuzzleEntity> getPuzzlesForLocation(LocationEntity locationEntity) {
        List<PuzzleEntity> puzzleEntities;
        puzzleEntities = entityManager.createQuery(
                "SELECT p FROM PuzzleEntity p where p.locationEntity = :locationEntity",
                PuzzleEntity.class
        ).setParameter("locationEntity", locationEntity)
                .getResultList();
        return puzzleEntities;
    }

    @Override
    public void update(PuzzleEntity puzzleEntity) {
        try {
            userTransaction.begin();
            entityManager.merge(puzzleEntity);
            userTransaction.commit();
        } catch (NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removePuzzle(PuzzleEntity puzzleEntity) {
        try {
            userTransaction.begin();
            entityManager.remove(
                    entityManager.contains(puzzleEntity)
                            ? puzzleEntity
                            : entityManager.merge(puzzleEntity)
            );
            userTransaction.commit();
        } catch (NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
            e.printStackTrace();
        }
    }

}
