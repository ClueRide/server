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

import com.clueride.auth.Secured;
import org.apache.http.HttpHeaders;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST API for Members.
 */
@Path("/member")
@RequestScoped
public class MemberWebService {
    @Inject
    private MemberService memberService;

    @Inject
    HttpServletRequest httpServletRequest;

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

    @GET
    @Secured
    @Path("matching")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Member> getMatchingMembers(@QueryParam("pattern") String pattern) {
        return memberService.getMatchingMembers(pattern);
    }

    @POST
    @Path("cross-check")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Member performCrossCheck(Member member) {
        String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return memberService.crossCheck(member, authHeader);
    }

}
