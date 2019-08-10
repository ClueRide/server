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
 * Created by jett on 11/17/17.
 */
package com.clueride.aop.badge;

import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.domain.badge.event.BadgeEventBuilder;
import com.clueride.domain.badge.event.BadgeEventService;

/**
 * Method Interceptor responsible for capturing events that count toward the awarding of badges.
 */
@BadgeCapture
@Interceptor
public class BadgeCaptureInterceptor {
    @Inject
    private Logger LOGGER;

    @Inject
    private BadgeEventService badgeEventService;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    @AroundInvoke
    public Object invoke(InvocationContext invocation) throws Exception {
        LOGGER.debug("invoke()");

        BadgeEventBuilder badgeEventBuilder =  BadgeEventBuilder.builder()
                .withTimestamp(new Date())
                .withMethodName(invocation.getMethod().getName())
                .withMethodClass(invocation.getMethod().getDeclaringClass())
                .withPrincipal(clueRideSessionDto.getBadgeOsPrincipal());

        Object returnValue = invocation.proceed();
        badgeEventBuilder.withReturnValue(returnValue);
        badgeEventService.send(badgeEventBuilder);
        return returnValue;
    }

}
