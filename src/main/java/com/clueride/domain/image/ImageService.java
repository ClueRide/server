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

import com.clueride.domain.location.Location;

/**
 * Defines operations on {@link ImageEntity} instances.
 */
public interface ImageService {

    /**
     * Uploads and persists a new Image.
     * By providing a latlon, we can show nearby images for other
     * {@link Location} instances.
     * @param imageUploadRequest Object containing lat/lon, Location ID and
     *                           InputStream of the image.
     * @return Identifying record for this image, including its URL on the system.
     */
    ImageEntity saveLocationImage(ImageUploadRequest imageUploadRequest);

}
