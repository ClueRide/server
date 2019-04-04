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
 * Created by jett on 2/10/19.
 */
package com.clueride.domain.image;

import java.util.List;

import com.clueride.domain.location.Location;

/**
 * Defines operations on {@link ImageLinkEntity} instances.
 */
public interface ImageService {

    /**
     * For the given Location ID, return all the associated images.
     * @param locationId unique identifier for the Location.
     * @return List of {@link ImageLinkEntity} matching the given location.
     */
    List<ImageLinkEntity> getImagesForLocation(Integer locationId);

    /**
     * Uploads and persists a new Image.
     * By providing a latlon, we can show nearby images for other
     * {@link Location} instances.
     * @param imageUploadRequest Object containing lat/lon, Location ID and
     *                           InputStream of the image.
     * @return Identifying record for this image, including its URL on the system.
     */
    ImageLinkEntity saveLocationImage(ImageUploadRequest imageUploadRequest);

}
