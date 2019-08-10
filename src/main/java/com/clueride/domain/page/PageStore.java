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

/**
 * Defines persistence operations on Pages.
 */
public interface PageStore {

    /**
     * Returns the Post ID for the given slug.
     *
     * This will also tell us if we have an entry for the page
     * in our local database.
     *
     * @param slug String used in the URL for the Clue Ride website.
     * @return matching Post ID or null if not found.
     */
    PageEntity getPageBySlug(String slug);

}
