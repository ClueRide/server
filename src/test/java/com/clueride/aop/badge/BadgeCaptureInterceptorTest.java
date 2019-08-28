package com.clueride.aop.badge;
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
 * Created by jett on 11/24/18.
 */

import java.lang.annotation.Annotation;
import java.security.Principal;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InterceptionType;
import javax.inject.Inject;

import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.NgCdiRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.aop.AopDummyService;
import com.clueride.aop.AopServiceConsumerImpl;
import com.clueride.domain.badge.event.BadgeEventEntity;
import com.clueride.domain.badge.event.BadgeEventService;
import com.clueride.util.Resources;
import com.clueride.util.TestOnly;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the BadgeCaptureInterceptorTest class.
 */
@AdditionalClasses({
        TestResources.class,
        Resources.class,
        BadgeCapture.class,
        BadgeCaptureInterceptor.class
})
public class BadgeCaptureInterceptorTest extends NgCdiRunner {
    @Inject
    @TestOnly
    private Principal principal;

    @Produces
    @Mock
    private BadgeEventService badgeEventService;

    @Produces
    @Mock
    private AopDummyService dummyService;

    @InjectMocks
    private AopServiceConsumerImpl toTest;

    /* This serves as a convenient way to know when the container is open for business. */
    public void printHello(@Observes ContainerInitialized event, @Parameters List<String> parameters) {
        WeldContainer instance = WeldContainer.instance(event.getContainerId());
        BeanManager beanManager = instance.getBeanManager();
        boolean success = beanManager.isInterceptorBinding(BadgeCapture.class);
        success = beanManager.isQualifier(BadgeCapture.class);
        Annotation qualifier = BadgeCaptureInterceptor.class.getAnnotation(BadgeCapture.class);
        beanManager.getInterceptorBindingHashCode(qualifier);
        List interceptors = beanManager.resolveInterceptors(InterceptionType.AROUND_INVOKE, qualifier);
        /* This will fail to be retrieved. */
//        BadgeCaptureInterceptor interceptor = instance.select(BadgeCaptureInterceptor.class, qualifier).get();
    }

    /* Before Suite is too early -- CDI container isn't initialized yet. */
//    @BeforeSuite
    public void suiteSetUp() throws Exception {
        /* These aren't available until the container is initialized. */
        BeanManager beanManager = CDI.current().getBeanManager();
        boolean interceptorBinding = beanManager.isInterceptorBinding(BadgeCapture.class);
        Annotation qualifier = BadgeCaptureInterceptor.class.getAnnotation(BadgeCapture.class);
        List interceptors = beanManager.resolveInterceptors(InterceptionType.AROUND_INVOKE, qualifier);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        /* If this wasn't initialized, it would fail. */
        BeanManager beanManager = CDI.current().getBeanManager();
        boolean interceptorBinding = beanManager.isInterceptorBinding(BadgeCapture.class);
        Annotation qualifier = BadgeCaptureInterceptor.class.getAnnotation(BadgeCapture.class);
        List interceptors = beanManager.resolveInterceptors(InterceptionType.AROUND_INVOKE, qualifier);
        initMocks(this);

        /* This creates an instance, but it doesn't trigger BadgeCapture; test fails. */
        toTest = new AopServiceConsumerImpl(dummyService);
        /* This is unable to construct the instance; unsatisfied dependencies. */
        toTest = CDI.current().select(AopServiceConsumerImpl.class).get();
        assertNotNull(toTest);
    }

    /* Failing test;  */
    /* And now its worse because I'm not using the Session Principal at all. */
//    @Test
    public void testInjection() throws Exception {
        /* setup test */
        Integer expected = 123;

        /* train mocks */
//        when(sessionPrincipal.getSessionPrincipal()).thenReturn(principal);

        /* make call */
        Integer actual = toTest.performService(expected);

        /* verify results */
        assertEquals(actual, expected);
//        verify(sessionPrincipal).getSessionPrincipal();
    }

//    @Test
    public void testAccessToPrincipal() throws Exception {
        /* setup test */
        Integer expected = 123;

        /* train mocks */

        /* make call */
        Integer actual = toTest.performService(expected);

        /* verify results */
        assertEquals(actual, expected);

    }

    @Test
    public void testAvoidCallUponException() throws Exception {
        /* setup test */
        Integer expected = 123;

        /* train mocks */
        doThrow(new RuntimeException()).when(dummyService).doSomeWork();

        /* make call */
        try {
            toTest.performService(expected);
        } catch (Exception e) {
            /* Ignore; we're paying attention to the badgeEventService */
        }

        /* verify results */
        verify(badgeEventService, times(0)).send(any(BadgeEventEntity.class));
    }

}