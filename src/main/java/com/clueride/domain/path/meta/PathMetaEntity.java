package com.clueride.domain.path.meta;

import javax.persistence.*;

@Entity
@Table(name="path")
public class PathMetaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "path_pk_sequence")
    @SequenceGenerator(name = "path_pk_sequence", sequenceName = "path_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name="start_node_id")
    private Integer startNodeId;

    @Column(name="end_node_id")
    private Integer endNodeId;

    @Transient
    private Boolean hasEdges;

    @Transient
    private Integer startAttractionId;

    @Transient
    private Integer endAttractionId;

    public static PathMetaEntity builder() {
        return new PathMetaEntity();
    }

    public PathMeta build() {
        return new PathMeta(this);
    }

    public Integer getId() {
        return id;
    }

    public PathMetaEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getStartNodeId() {
        return startNodeId;
    }

    public Integer getEndNodeId() {
        return endNodeId;
    }

    public PathMetaEntity withStartNodeId(Integer startNodeId) {
        this.startNodeId = startNodeId;
        return this;
    }

    public PathMetaEntity withEndNodeId(Integer endNodeId) {
        this.endNodeId = endNodeId;
        return this;
    }

    public Boolean hasEdges() {
        return hasEdges;
    }

    public PathMetaEntity withHasEdges(Boolean hasEdges) {
        this.hasEdges = hasEdges;
        return this;
    }

    public Integer getStartAttractionId() {
        return startAttractionId;
    }

    public PathMetaEntity withStartAttractionId(Integer startAttractionId) {
        this.startAttractionId = startAttractionId;
        return this;
    }

    public Integer getEndAttractionId() {
        return endAttractionId;
    }

    public PathMetaEntity withEndAttractionId(Integer endAttractionId) {
        this.endAttractionId = endAttractionId;
        return this;
    }
}
