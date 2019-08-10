/*
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
 * Created by jett on 8/9/19.
 */
package com.clueride.domain.page;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.slf4j.Logger;

import com.clueride.aop.badge.BadgeCapture;

/**
 * Implements the {@link PageService} interface.
 */
public class PageServiceImpl implements PageService {
    @Inject
    private Logger LOGGER;

    @Inject
    private PageStore pageStore;

    @Override
    @BadgeCapture
    public Page visitPage(String slug) {
        LOGGER.debug("Visiting page at slug {}", slug);
        try {
            return pageStore.getPageBySlug(slug).build();
        } catch (NoResultException nre) {
            LOGGER.error("No page found matching slug {}", slug);
            return null;
        }
    }

}
