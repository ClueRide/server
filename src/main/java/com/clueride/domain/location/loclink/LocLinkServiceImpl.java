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
 * Created by jett on 5/6/19.
 */
package com.clueride.domain.location.loclink;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import com.google.common.base.Strings;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link LocLinkService}.
 */
public class LocLinkServiceImpl implements LocLinkService {

    @Inject
    private LocLinkStore locLinkStore;

    @Override
    public LocLinkEntity createNewLocationLink(LocLinkEntity locLinkEntity) throws MalformedURLException {
        String link = requireNonNull(locLinkEntity.getLink());
        /* Try creating a URL from the link to make sure it is valid. */
        new URL(link);
        locLinkStore.addNew(locLinkEntity);
        return locLinkEntity;
    }

    @Override
    public LocLinkEntity getLocLinkByUrl(String locLinkText) throws MalformedURLException {
        requireNonNull(locLinkText);
        /* Try creating a URL from the link to make sure it is valid. */
        new URL(locLinkText);
        LocLinkEntity locLinkEntity = locLinkStore.findByUrl(locLinkText);
        if (locLinkEntity != null) {
            return locLinkEntity;
        } else {
            return createNewLocationLink(
                    new LocLinkEntity().withLink(locLinkText)
            );
        }

    }

    @Override
    public LocLinkEntity validateAndPrepareFromUserInput(LocLinkEntity locLinkEntity) throws MalformedURLException {
        if (Strings.isNullOrEmpty(locLinkEntity.getLink())) {
            /* No attempt to provide a valid link. */
            return null;
        }
        String link = locLinkEntity.getLink();

        /* Check the value. */
        new URL(link);

        return getLocLinkByUrl(link);
    }

}
