package com.clueride.domain.path.meta;

import javax.annotation.concurrent.Immutable;

@Immutable
public class PathMeta {
    private final Integer id;
    private final Integer courseToPathId;
    private final Integer startAttractionId;
    private final Integer endAttractionId;
    private final Boolean hasEdges;

    public PathMeta(PathMetaEntity pathMetaEntity) {
        this.id = pathMetaEntity.getId();
        this.courseToPathId = pathMetaEntity.getCourseToPathId();
        this.startAttractionId = pathMetaEntity.getStartAttractionId();
        this.endAttractionId = pathMetaEntity.getEndAttractionId();
        this.hasEdges = pathMetaEntity.hasEdges();
    }

    public Integer getId() {
        return id;
    }

    public Integer getCourseToPathId() {
        return courseToPathId;
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
