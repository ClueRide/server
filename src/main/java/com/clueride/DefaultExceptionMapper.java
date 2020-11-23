/*
 * Copyright 2020 Jett Marks
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
 * Created by jett on 1/1/20.
 */
package com.clueride;

import org.jboss.resteasy.spi.DefaultOptionsMethodException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Handles uncaught exceptions by wrapping them in a 500 Response.
 */
@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Throwable> {

//    @Inject
//    private Logger LOGGER;

    public Response toResponse(Throwable e) {
        /* TODO: SVR-84 improve response to this exception. */
        if (e instanceof DefaultOptionsMethodException) {
            return null;
        }

//        LOGGER.error(e.getMessage(), e);

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.toString())
                .build();
    }

}
