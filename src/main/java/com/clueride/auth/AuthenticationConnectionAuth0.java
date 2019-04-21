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
 * Created by jett on 10/18/18.
 */
package com.clueride.auth;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * Implementation of {@link AuthenticationConnection}.
 */
public class AuthenticationConnectionAuth0 implements AuthenticationConnection {
    @Inject
    private Logger LOGGER;

    private String jsonResponse = null;
    private int responseCode = 0;
    private String responseMessage = null;

    @Override
    public int makeRequest(URL auth0Url, String accessToken) throws IOException {

        /* Open Connection */
        HttpsURLConnection connection = (HttpsURLConnection) auth0Url.openConnection();

        /* Provide 'credentials' */
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        /* Retrieve response */
        responseCode = connection.getResponseCode();
        responseMessage = connection.getResponseMessage();
        switch (responseCode) {
            case 200:
                InputStream inputStr = connection.getInputStream();
                String encoding = connection.getContentEncoding() == null ? "UTF-8"
                        : connection.getContentEncoding();
                jsonResponse = IOUtils.toString(inputStr, encoding);
                LOGGER.debug(String.format("Raw JSON Response:\n%s", jsonResponse));
                break;
            case 401:
                LOGGER.debug("Unauthorized against " + auth0Url);
                break;
            default:
                LOGGER.error(String.format(
                        "Unable to read response: %d %s",
                        responseCode,
                        responseMessage)
                );
                break;
        }
        return responseCode;
    }

    @Override
    public String getJsonResponse() {
        return jsonResponse;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String getResponseMessage() {
        return responseMessage;
    }

}
