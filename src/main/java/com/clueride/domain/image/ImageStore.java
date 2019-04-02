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

import java.io.InputStream;
import java.util.List;

import com.clueride.domain.location.Location;

/**
 * Defines JPA and File System operations on {@link ImageEntity} instances.
 *
 * In the case of Image data, we would expect to write the data out to
 * the web server's Document directory where URLs can be used to retrieve
 * the images by the application.
 *
 * This Store is different in that it is intended to straddle both JPA and
 * file system implementations.
 */
public interface ImageStore {
    /**
     * Retrieves the list of {@link ImageEntity} instances for the given location.
     * @param locationId unique identifier for the location.
     * @return List of matching {@link ImageEntity}.
     */
    List<ImageEntity> getImagesForLocation(Integer locationId);

    /**
     * Accepts InputStream of image data to persist along with
     * a Location ID to tie this image to.
     * @param locationId Unique identifier for the Location this
     *                   image is associated with.
     * @param convertedFileData InputStream of data converted from
     *                          the wire into something we put on the
     *                          file system for serving up by the Image
     *                          server.
     * @return Unique identifier for this record in the DB.
     * the file records cannot be opened.
     */
    Integer addNew(Integer locationId, InputStream convertedFileData);

    /**
     * Accepts the ImageEntity which contains the URL of an image which
     * has been placed on the Image Web Server's file system along with
     * the Location ID of the Location to association with the image, and
     * persists this link in the JPA-based database.
     * @param imageEntity Image's URL record.
     * @param locationId Unique identifier for the {@link Location} instance.
     * @return unique identifier for the link record.
     */
    Integer addNewToLocation(ImageEntity imageEntity, Integer locationId);
}
