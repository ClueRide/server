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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import com.clueride.aop.badge.BadgeCapture;
import com.clueride.auth.Secured;

/**
 * REST API for images.
 */
@Path("image")
@RequestScoped
public class ImageWebService {

    @Inject
    private ImageService imageService;

    @GET
    @Secured
    @Path("{locationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<ImageEntity> getImagesForLocation(@PathParam("locationId") Integer locationId) {
        return imageService.getImagesForLocation(locationId);
    }

    /**
     * Handles upload request for a new image at the given location.
     * @param imageUploadRequest Object containing lat/lon, Location ID and
     *                           InputStream of the image.
     * @return Identifying record for this image, including its URL on the system.
     */
    @POST
    @Secured
    @BadgeCapture
    @Path("upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public ImageEntity uploadImage(@MultipartForm ImageUploadRequest imageUploadRequest) {
        return imageService.saveLocationImage(imageUploadRequest);
    }

}
