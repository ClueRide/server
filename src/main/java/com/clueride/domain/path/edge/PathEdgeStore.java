package com.clueride.domain.path.edge;

public interface PathEdgeStore {
    /**
     * Creates a new record for PathEdges to link a Path with an Edge.
     *
     * @param pathEdgeEntity instance to persist.
     * @return same PathEdgeEntity with an ID assigned by the DB.
     */
    PathEdgeEntity createNew(PathEdgeEntity pathEdgeEntity);
}
