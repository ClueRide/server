package com.clueride.domain.game;

import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.domain.course.Course;
import com.clueride.domain.course.CourseService;
import com.clueride.domain.outing.OutingView;
import org.jglue.cdiunit.NgCdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.enterprise.inject.Produces;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class GameStateServiceImplTest extends NgCdiRunner {
    private static final Integer TEST_COURSE_ID = 14;
    private static final List<Integer> FULL_SET_ATTRACTION_IDS = Arrays.asList(101, 102, 103, 104, 105);

    @InjectMocks
    private GameStateServiceImpl toTest;

    @Mock
    private OutingView outingView;

    @Mock
    private ClueRideSessionDto clueRideSessionDto;

    @Produces
    @Mock
    private CourseService courseService;

    @Mock
    private Course mockCourse;

    @Before
    public void setup() {
        initMocks(this);

        assertNotNull("Unable to instantiate OutingView", outingView) ;
        assertNotNull("Unable to instantiate class under test", toTest);
        assertNotNull("Expected DTO Provider to be instantiated", clueRideSessionDto);
    }


    @Test
    public void testIsEndOfCourse_notYet() {
        /* Setup */
        GameState.Builder gameStateBuilder = GameState.Builder.builder()
                .withLocationId(3);

        /* Train Mocks */
        when(outingView.getCourseId()).thenReturn(TEST_COURSE_ID);
        when(clueRideSessionDto.getOutingView()).thenReturn(outingView);
        when(courseService.getById(TEST_COURSE_ID)).thenReturn(mockCourse);
        when(mockCourse.getLocationIds()).thenReturn(FULL_SET_ATTRACTION_IDS);

        /* Make call */
        boolean actual = toTest.isEndOfCourse(gameStateBuilder);

        /* Verify */
        assertFalse(actual, "Should not be End of Course for 3rd attraction");
    }

    @Test
    public void testIsEndOfCourse_atEnd() {
        /* Setup */
        GameState.Builder gameStateBuilder = GameState.Builder.builder()
                .withLocationId(105);

        /* Train Mocks */
        when(outingView.getCourseId()).thenReturn(TEST_COURSE_ID);
        when(clueRideSessionDto.getOutingView()).thenReturn(outingView);
        when(courseService.getById(TEST_COURSE_ID)).thenReturn(mockCourse);
        when(mockCourse.getLocationIds()).thenReturn(FULL_SET_ATTRACTION_IDS);

        /* Make call */
        boolean actual = toTest.isEndOfCourse(gameStateBuilder);

        /* Verify */
        assertTrue(actual, "Should be End of Course for last attraction");
    }

}
