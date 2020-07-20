package com.clueride.domain.path;

import javax.annotation.concurrent.Immutable;

@Immutable
public class LinkPath {
    private final Integer id;
    private final Integer startAttractionId;
    private final Integer endAttractionId;
    private final Boolean hasEdges;

    public LinkPath(LinkPathEntity linkPathEntity) {
        this.id = linkPathEntity.getId();
        this.startAttractionId = linkPathEntity.getStartAttractionId();
        this.endAttractionId = linkPathEntity.getEndAttractionId();
        this.hasEdges = linkPathEntity.hasEdges();
    }

    public Integer getId() {
        return id;
    }

    public Integer getStartAttractionId() {
        return startAttractionId;
    }

    public Integer getEndAttractionId() {
        return endAttractionId;
    }

    public Boolean getHasEdges() {
        return hasEdges;
    }
}
