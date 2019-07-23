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
 * Created by jett on 7/20/19.
 */
package com.clueride.badgeos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import com.clueride.config.ConfigService;

/**
 * Implementation of {@link BadgeOSSessionService}.
 *
 * Uses credentials to login to BadgeOS and from the response, picks up both
 * a set of Cookies for use in future sessions as well as a set of Nonces that
 * are required for future calls. These nonces are then available to be retrieved for use
 * within a session mediated by the cookies.
 */
public class BadgeOSSessionServiceImpl implements BadgeOSSessionService {
    @Inject
    private Logger LOGGER;

    private final BadgeOSCredentials badgeOSCredentials;
    private final String loginUrlAsString;

    /** Populated with session cookies which are "re-baked" when needed. */
    // TODO: This will need to be refreshed/re-baked periodically.
    private static HttpClientContext badgeOSContext = null;
    private static CookieStore cookieStore = null;
    private static Map<NonceType, Nonce> nonces = null;

    @Inject
    public BadgeOSSessionServiceImpl(
            BadgeOSCredentials badgeOSCredentials,
            ConfigService configService
    ) {
        this.loginUrlAsString = configService.getBadgeOSLoginUrlAsString();
        this.badgeOSCredentials = badgeOSCredentials;
    }

    @Override
    public void refreshSession() {
        /* Establish Context if needed. */
        badgeOSContext = establishContext();

        retrieveUserAchievementNonces();

    }

    /**
     * After getting a new set of cookies, we also make sure we have a new set
     * of Nonces.
     */
    private void retrieveUserAchievementNonces() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            /* Prepare for the next POST to pick up the Nonces. */
            HttpGet userGet = new HttpGet(
                    "https://clueride.com/wp-admin/user-edit.php?user_id=47"
            );
            LOGGER.info("User Page Request: \n{}", userGet.getRequestLine());

            CloseableHttpResponse userGetResponse = httpClient.execute(userGet, badgeOSContext);
            LOGGER.info("User Page Response: {}", userGetResponse.getStatusLine());
            String userGetResponseAsString = EntityUtils.toString(userGetResponse.getEntity());

            /* Pull out the Cookies. */
            cookieStore = badgeOSContext.getCookieStore();

            /* Pull out the Nonces. */
            nonces = retrieveNonces(userGetResponseAsString);

        } catch (IOException e) {
            LOGGER.error("Problem connecting to BadgeOS");
            e.printStackTrace();
        }

    }

    @Override
    public Nonce getAwardNonce() {
        if (nonces == null) {
            refreshSession();
        }

        assert nonces != null;
        return nonces.get(NonceType.AWARD_ACHIEVEMENT);
    }

    @Override
    public Nonce getRevokeNonce() {
        if (nonces == null) {
            refreshSession();
        }

        assert nonces != null;
        return nonces.get(NonceType.REVOKE_ACHIEVEMENT);
    }

    @Override
    public CookieStore getSessionCookies() {
        return cookieStore;
    }

    /**
     * Creates a new Context populated by logging into WordPress if we haven't
     * already done so.
     *
     * @return Either the existing context for WordPress, or establish a new one
     * if it doesn't exist.
     */
    private HttpClientContext establishContext() {
        /* Use pre-established context if it is available. */
        if (badgeOSContext != null) {
            return badgeOSContext;
        }

        HttpClientContext context = HttpClientContext.create();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            /* Assemble the Login Form's data entity to be posted. */
            List<NameValuePair> formElements = new ArrayList<>();
            formElements.add(new BasicNameValuePair("log", badgeOSCredentials.getLog()));
            formElements.add(new BasicNameValuePair("pwd", badgeOSCredentials.getPwd()));
            formElements.add(new BasicNameValuePair("wp-submit", badgeOSCredentials.getWpSubmit()));
            formElements.add(new BasicNameValuePair("testcookie", "1"));

            /* This endpoint may change as we evolve, but is suitable for proving out the concepts. */
            formElements.add(new BasicNameValuePair(
                    "redirect_to",
                    "https://clueride.com/wp-admin/user-edit.php?user_id=47"
            ));

            HttpEntity loginEntity = new UrlEncodedFormEntity(formElements, Consts.UTF_8);

            /* Create and Invoke the URL for logging in. */
            HttpPost loginPost = new HttpPost(loginUrlAsString);
            loginPost.setEntity(loginEntity);
            LOGGER.debug("Connecting to BadgeOS: \n{}", loginPost.getRequestLine());

            CloseableHttpResponse response = httpClient.execute(loginPost, context);
            LOGGER.debug("login.php Status: {}", response.getStatusLine());
            response.close();
        } catch (IOException e) {
            LOGGER.error("Problem connecting to BadgeOS");
            throw(new RuntimeException("Unable to connect to BadgeOS", e));
        }
        return context;
    }

    /**
     * Given the User Page, retrieve the Nonces needed to award an achievement.
     *
     * @param userGetResponseAsString Entire text of the User Edit page from WordPress / BadgeOS.
     * @return Map of the relevant (and desired) Nonces found on the page.
     */
    private Map<NonceType, Nonce> retrieveNonces(String userGetResponseAsString) {
        boolean haveAwardNonce = false;
        boolean haveRevokeNonce = false;

        Map<NonceType, Nonce> map = new HashMap<>();

        String[] lines = userGetResponseAsString.split("\n");
        for (String line : lines) {
            if (line.contains("action=award")) {
                LOGGER.debug("Found Award Nonce");
                map.put(NonceType.AWARD_ACHIEVEMENT, nonceFromLine(line, NonceType.AWARD_ACHIEVEMENT));
                haveAwardNonce = true;
            }
            if (line.contains("action=revoke")) {
                LOGGER.debug("Found Revoke Nonce");
                map.put(NonceType.REVOKE_ACHIEVEMENT, nonceFromLine(line, NonceType.REVOKE_ACHIEVEMENT));
                haveRevokeNonce = true;
            }
            if (haveAwardNonce && haveRevokeNonce) {
                return map;
            }
        }
        LOGGER.error("Unable to find nonces for both Award and Revoke");
        return map;
    }

    private Nonce nonceFromLine(String line, NonceType nonceType) {
        String actionPortion = line.substring(line.indexOf("action"));
        String field = actionPortion.substring(
                actionPortion.indexOf("_wpnonce"),
                actionPortion.indexOf("\">")
        );
        LOGGER.debug("Nonce field: {}", field);

        String nonceValue = field.substring(field.indexOf("=")+1);
        return new Nonce(nonceType, nonceValue);
    }

}
