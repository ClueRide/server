package com.clueride.domain.path.edge;

import javax.persistence.*;

@Entity
@Table(name="path_to_edge")
public class PathEdgeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "path_to_edge_pk_sequence")
    @SequenceGenerator(name="path_to_edge_pk_sequence", sequenceName = "path_to_edge_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name="edge_order")
    private Integer edgeOrder;

    @Column(name="path_id")
    private Integer pathId;

    @Column(name="edge_id")
    private Integer edgeId;

    public static PathEdgeEntity builder() {
        return new PathEdgeEntity();
    }

    public Integer getId() {
        return id;
    }

    public PathEdgeEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getEdgeOrder() {
        return edgeOrder;
    }

    public PathEdgeEntity withEdgeOrder(Integer edgeOrder) {
        this.edgeOrder = edgeOrder;
        return this;
    }

    public Integer getPathId() {
        return pathId;
    }

    public PathEdgeEntity withPathId(Integer pathId) {
        this.pathId = pathId;
        return this;
    }

    public Integer getEdgeId() {
        return edgeId;
    }

    public PathEdgeEntity withEdgeId(Integer edgeId) {
        this.edgeId = edgeId;
        return this;
    }

}
