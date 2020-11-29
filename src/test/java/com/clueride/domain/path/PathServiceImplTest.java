package com.clueride.domain.path;

import com.clueride.domain.attraction.AttractionEntity;
import com.clueride.domain.attraction.AttractionStore;
import com.clueride.domain.course.CourseEntity;
import com.clueride.domain.course.CourseStore;
import com.clueride.domain.course.link.CourseToPathLinkStore;
import com.clueride.domain.path.attractions.CoursePathAttractionsEntity;
import com.clueride.domain.path.attractions.CoursePathAttractionsStore;
import com.clueride.domain.path.meta.PathMeta;
import com.clueride.domain.path.meta.PathMetaEntity;
import com.clueride.domain.path.meta.PathMetaStore;
import com.clueride.network.path.PathStore;
import org.jglue.cdiunit.NgCdiRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class PathServiceImplTest extends NgCdiRunner {
    private final static Integer COURSE_ID = 6;
    private final static Integer NODE_ID_1 = 901;
    private final static Integer NODE_ID_2 = 902;
    private final static Integer NODE_ID_3 = 903;
    private final static Integer ATTR_ID_1 = 1;
    private final static Integer ATTR_ID_2 = 2;
    private final static Integer ATTR_ID_3 = 3;
    private final static Integer ATTR_ID_4 = 4;
    private final List<Integer> attractionIds = Arrays.asList(
            ATTR_ID_1,
            ATTR_ID_2,
            ATTR_ID_3
    );
    private final static Integer PATH_ID_1 = 101;
    private final static Integer PATH_ID_2 = 102;
    private final static Integer PATH_ID_EXTRA = 1001;
    private final static CoursePathAttractionsEntity PATH_1 =
            CoursePathAttractionsEntity.builder()
                    .withPathId(PATH_ID_1)
                    .withCourseId(COURSE_ID)
                    .withStartAttractionId(ATTR_ID_1)
                    .withEndAttractionId(ATTR_ID_2);
    private final static CoursePathAttractionsEntity PATH_2 =
            CoursePathAttractionsEntity.builder()
                    .withPathId(PATH_ID_2)
                    .withCourseId(COURSE_ID)
                    .withStartAttractionId(ATTR_ID_2)
                    .withEndAttractionId(ATTR_ID_3);
    private final static CoursePathAttractionsEntity PATH_EXTRA =
            CoursePathAttractionsEntity.builder()
                    .withPathId(PATH_ID_EXTRA)
                    .withCourseId(COURSE_ID)
                    .withStartAttractionId(ATTR_ID_3)
                    .withEndAttractionId(ATTR_ID_4);

    @Mock
    private Logger LOGGER;

    @Mock
    private CourseStore courseStore;

    @Mock
    private CoursePathAttractionsStore coursePathAttractionsStore;

    @Mock
    private PathStore pathStore;

    @Mock
    private PathMetaStore pathMetaStore;

    @Mock
    private AttractionStore attractionStore;

    @Mock
    private CourseToPathLinkStore courseToPathLinkStore;

    @InjectMocks
    private PathServiceImpl toTest;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        assertNotNull(toTest);
    }

    @Test
    public void testGetPathMetaForAttractions_removeExtra() {
        /* Train Mocks */
        for (int attractionId : attractionIds) {
            when(attractionStore.getById(attractionId)).thenReturn(
                    AttractionEntity.builder()
                            .withId(attractionId)
                            .withNodeId(attractionId + 900)
            );
        }
        when(courseStore.getCourseById(COURSE_ID)).thenReturn(CourseEntity.builder().withId(COURSE_ID));
        when(coursePathAttractionsStore.getPathAttractionsForCourse(COURSE_ID)).thenReturn(
                Arrays.asList(PATH_1, PATH_2, PATH_EXTRA)
        );
        when(pathMetaStore.findSuitablePath(NODE_ID_1, NODE_ID_2)).thenReturn(
                PathMetaEntity.builder()
        );
        when(pathMetaStore.findSuitablePath(NODE_ID_2, NODE_ID_3)).thenReturn(
                PathMetaEntity.builder()
        );

        /* Make Call */
        List<PathMeta> actual = toTest.getPathMetaForAttractions(
                COURSE_ID,
                attractionIds
        );

        /* Verify */
        assertNotNull(actual);
        assertEquals(actual.size(), attractionIds.size() - 1);
        verify(pathMetaStore, times(0)).createNew(any());
        verify(courseToPathLinkStore, times(0)).remove(any());
    }

}
