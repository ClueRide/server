package com.clueride.domain.path;

import javax.persistence.*;

@Entity
@Table(name="path")
public class LinkPathEntity {
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

    public static LinkPathEntity builder() {
        return new LinkPathEntity();
    }

    public LinkPath build() {
        return new LinkPath(this);
    }

    public Integer getId() {
        return id;
    }

    public LinkPathEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getStartNodeId() {
        return startNodeId;
    }

    public Integer getEndNodeId() {
        return endNodeId;
    }

    public LinkPathEntity withStartNodeId(Integer startNodeId) {
        this.startNodeId = startNodeId;
        return this;
    }

    public LinkPathEntity withEndNodeId(Integer endNodeId) {
        this.endNodeId = endNodeId;
        return this;
    }

    public Boolean hasEdges() {
        return hasEdges;
    }

    public LinkPathEntity withHasEdges(Boolean hasEdges) {
        this.hasEdges = hasEdges;
        return this;
    }

    public Integer getStartAttractionId() {
        return startAttractionId;
    }

    public LinkPathEntity withStartAttractionId(Integer startAttractionId) {
        this.startAttractionId = startAttractionId;
        return this;
    }

    public Integer getEndAttractionId() {
        return endAttractionId;
    }

    public LinkPathEntity withEndAttractionId(Integer endAttractionId) {
        this.endAttractionId = endAttractionId;
        return this;
    }
}
