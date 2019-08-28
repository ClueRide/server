package com.clueride.domain.badge.event;/*
 * Copyright 2019 Jett Marks
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
 * Created by jett on 3/31/19.
 */

import java.util.Date;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.NgCdiRunner;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.aop.badge.TestResources;
import com.clueride.domain.account.principal.EmailPrincipal;
import static org.testng.Assert.assertEquals;

/**
 * Exercises the BadgeEventEntityTest class.
 */
@AdditionalClasses({TestResources.class})
public class BadgeEventEntityTest extends NgCdiRunner {
    private BadgeEventEntity toTest;

    private static final Date NOW_TIMESTAMP = new Date();

    @BeforeMethod
    public void setUp() {
        toTest = BadgeEventEntity.builder()
                .withPrincipal(new EmailPrincipal("dummy@clueride.com"))
                .withMemberId(-1)
                .withMethodClass(BadgeEvent.class)
                .withMethodName("aHa")
                .withTimestamp(NOW_TIMESTAMP)
                ;
        /* See if JSON can handle this circular reference. */
        toTest.withReturnValue(toTest);
    }

    @Test
    public void testFrom() {
        /* setup test */
        BadgeEvent expected = toTest.build();

        /* make call */
        BadgeEvent actual = BadgeEventEntity.from(expected).build();

        /* verify results */
        assertEquals(actual, expected);
    }

}