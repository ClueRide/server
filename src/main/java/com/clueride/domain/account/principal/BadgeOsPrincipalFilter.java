/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 11/29/18.
 */
package com.clueride.domain.account.principal;

/**
 * Represents criteria by which to filter the Principals held within BadgeOS.
 */
public class BadgeOsPrincipalFilter {
    FilterType filterType = FilterType.NO_CRITERIA;

    /* TODO: expect this to expand as business needs coalesce. */

    public enum FilterType {
        NO_CRITERIA,
        SINGLE_CRITERIA,
        MULTIPLE_CRITERIA
    }

}
