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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Inject;

import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;

import com.clueride.config.ConfigService;
import com.clueride.domain.location.LocationBuilder;
import com.clueride.domain.location.LocationStore;
import static java.util.Objects.requireNonNull;

/**
 * Default implementation of {@link ImageService}.
 */
public class ImageServiceImpl implements ImageService {
    @Inject
    private Logger LOGGER;

    @Inject
    private LocationStore locationStore;
    @Inject
    private ImageStore imageStore;
    @Inject
    private ConfigService config;

    @Override
    public List<ImageLinkEntity> getImagesForLocation(Integer locationId) {
        requireNonNull(locationId, "Must specify location");
        return imageStore.getImagesForLocation(locationId);
    }

    @Override
    public ImageLinkEntity saveLocationImage(ImageUploadRequest imageUploadRequest) {
        Integer locationId = imageUploadRequest.getLocationId();
        requireNonNull(locationId, "Must specify location to associate with image");
        LOGGER.info("Requesting Save of image for Location ID {}", locationId);

        Integer newSeqId = persistImageContent(locationId, imageUploadRequest.getFileData());
        ImageLinkEntity image = persistImageMetadata(locationId, newSeqId);
        LocationBuilder locationBuilder = locationStore.getLocationBuilderById(locationId);
        /* Save this image as the (default) featured image because there is no other featured image. */
        if (locationBuilder.hasNoFeaturedImage()) {
            locationBuilder.withFeaturedImage(image);
            locationStore.update(locationBuilder);
        }
        return image;
    }

    private Integer persistImageContent(Integer locationId, InputStream fileData) {
        InputStream convertedFileData = decodeBase64(fileData);
        return imageStore.addNew(locationId, convertedFileData);
    }

    /* This should kick in when we are converting from an actual device. */
    InputStream decodeBase64(InputStream fileData) {
        InputStreamReader reader = new InputStreamReader(fileData);
        try {
            //noinspection StatementWithEmptyBody
            while (reader.read() != ',') {
                // skip the header
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BaseEncoding.base64().decodingStream(reader);
    }

    private ImageLinkEntity persistImageMetadata(Integer locationId, Integer imageOnFileSystemId) {
        /* Build and assign URL. */
        ImageLinkEntity imageLinkEntity = new ImageLinkEntity(
                buildImageUrlString(
                        locationId,
                        imageOnFileSystemId
                )
        );

        imageStore.addNewToLocation(imageLinkEntity, locationId);
        return imageLinkEntity;
    }

    private String buildImageUrlString(Integer locationId, Integer imageOnFileSystemId) {
        return String.format( "%s%d/%d.jpg",
                config.getBaseImageUrl(),
                locationId,
                imageOnFileSystemId
        );
    }

}
