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
 * Created by jett on 12/30/18.
 */
package com.clueride.domain.invite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import com.clueride.auth.ClueRideSession;
import com.clueride.auth.ClueRideSessionDto;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.course.Course;
import com.clueride.domain.course.CourseService;
import com.clueride.domain.outing.Outing;
import com.clueride.domain.outing.OutingService;
import com.clueride.domain.team.Team;
import com.clueride.domain.team.TeamService;

/**
 * Implementation of the {@link InviteService}.
 */
public class InviteServiceImpl implements InviteService {
    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    @Inject private InviteStore inviteStore;
    @Inject private OutingService outingService;
    @Inject private MemberService memberService;
    @Inject private TeamService teamService;
    @Inject private CourseService courseService;

    @Override
    public List<Invite> getInvitationsForOuting(Integer outingId) {
        return null;
    }

    @Override
    public Invite createNew(Integer outingId, Integer memberId) throws IOException {
        return null;
    }

    @Override
    public List<Invite> send(Integer outingId) {
        return null;
    }

    @Override
    public List<Invite> getSessionInvites() {
        List<Invite> invites = new ArrayList<>();

        Integer memberId = clueRideSessionDto.getMember().getId();
        List<Invite.Builder> inviteBuilders = inviteStore.getUpcomingInvitationsByMemberId(memberId);
        for (Invite.Builder builder : inviteBuilders) {

            // TODO: CA-376 Consider plopping this down in the Session DTO instead of creating this here.
            Outing outing = outingService.getById(builder.getOutingId());
            builder.withScheduledTime(outing.getScheduledTime());

            Member guide = memberService.getMember(outing.getGuideMemberId());
            builder.withGuideName(guide.getDisplayName());

            Team team = teamService.getTeam(outing.getTeamId());
            builder.withTeamName(team.getName());

            Course course = courseService.getById(outing.getCourseId());
            builder.withCourseName(course.getName());
            builder.withCourseUrl(course.getUrl());

            invites.add(builder.build());
        }

        return invites;
    }

    @Override
    public Invite accept(Integer inviteId) {
        return null;
    }

    @Override
    public Invite decline(Integer inviteId) {
        return null;
    }

    @Override
    public Invite expire(Integer teamId) {
        return null;
    }

    @Override
    public SessionInviteState getSessionInviteState() {
        Integer memberId = clueRideSessionDto.getMember().getId();
        List<Invite.Builder> sessionInvites = inviteStore.getUpcomingInvitationsByMemberId(memberId);
        if (sessionInvites.size() == 0) {
            return SessionInviteState.NO_INVITES;
        }
        if (sessionInvites.size() > 1) {
            return SessionInviteState.MULTIPLE_INVITES;
        }
        Invite.Builder soleInvite = sessionInvites.get(0);
        switch (soleInvite.getState()) {
            case ACCEPTED:
                return SessionInviteState.ACCEPTED_INVITE;
            case DECLINED:
                return SessionInviteState.DECLINED_INVITES;
            case SENT:
                return SessionInviteState.OPEN_INVITE;
            case EXPIRED:
                return SessionInviteState.NO_INVITES;
        }
        return SessionInviteState.NO_INVITES;
    }

}