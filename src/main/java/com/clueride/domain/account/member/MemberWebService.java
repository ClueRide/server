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
 * Created by jett on 11/12/18.
 */
package com.clueride.domain.account.member;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.clueride.auth.Secured;

/**
 * REST API for Members.
 */
@Path("/member")
@RequestScoped
public class MemberWebService {
    @Inject
    private MemberService memberService;

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GET
    @Secured
    @Path("active")
    @Produces(MediaType.APPLICATION_JSON)
    public Member getActiveMember() {
        return memberService.getActiveMember();
    }

    @GET@Secured
    @Path("matching")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Member> getMatchingMembers(@QueryParam("pattern") String pattern) {
        return memberService.getMatchingMembers(pattern);
    }

}
