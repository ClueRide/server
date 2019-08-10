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
 * Defines operations on Pages.
 */
public interface PageService {

    /**
     * Records a visit to the page with the given slug.
     *
     * This does not actually redirect the user to the page; it is used
     * to record achievements.
     *
     * @param slug String used to identify the page to be displayed.
     * @return Page instance matching the slug, or null if not found.
     */
    Page visitPage(String slug);

}
