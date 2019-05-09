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

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link LocLinkService}.
 */
public class LocLinkServiceImpl implements LocLinkService {

    @Inject
    private LocLinkStore locLinkStore;

    @Override
    public LocLink createNewLocationLink(LocLinkEntity locLinkEntity) throws MalformedURLException {
        String link = requireNonNull(locLinkEntity.getLink());
        URL url = new URL(link);
        locLinkStore.addNew(locLinkEntity);
        return locLinkEntity.build();
    }

    @Override
    public LocLink getLocLinkByUrl(String locLinkText) throws MalformedURLException {
        requireNonNull(locLinkText);
        URL url = new URL(locLinkText);
        LocLinkEntity locLinkEntity = locLinkStore.findByUrl(locLinkText);
        if (locLinkEntity != null) {
            return locLinkEntity.build();
        } else {
            return createNewLocationLink(
                    new LocLinkEntity().withLink(locLinkText)
            );
        }

    }
}
