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
 * Created by jett on 7/28/19.
 */
package com.clueride.domain.badge.theme;

import java.util.List;

/**
 * Persistence operations for {@link ThemeEntity} instances.
 */
public interface ThemeStore {
    /**
     * Retrieve the list of Themes -- open or closed.
     * @return List of all ThemeEntity instances.
     */
    List<ThemeEntity> getThemes();

    /**
     * Retrieve the list of Closed Themes.
     * @return List of all closed ThemeEntity instances.
     */
    List<ThemeEntity> getClosedThemes();

    /**
     * Retrieve the list of Open Themes.
     * @return List of all open ThemeEntity instances.
     */
    List<ThemeEntity> getOpenThemes();

}
