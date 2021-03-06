/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 3/20/16.
 */
package com.clueride.domain.team;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberService;

/**
 * Implementation of the Team Interface for maintaining the list of Teams and their members.
 */
public class TeamServiceImpl implements TeamService {
    @Inject
    private Logger LOGGER;

    private final TeamStore teamStore;
    private final MemberService memberService;

    @Inject
    public TeamServiceImpl(
            TeamStore teamStore,
            MemberService memberService
    ) {
        this.teamStore = teamStore;
        this.memberService = memberService;
    }


    @Override
    public List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();
        List<TeamEntity> builders = teamStore.getTeams();
        for (TeamEntity builder : builders) {
            teams.add(builder.build());
        }
        return teams;
    }

    @Override
    public Team getTeam(Integer teamId) {
        return teamStore.getTeamById(teamId).build();
    }

    @Override
    public Team addMember(Integer teamId, Member newMember) {
        Member member = memberService.getMember(newMember.getId());
        LOGGER.info("Adding Member " + member.getDisplayName() + " to team " + teamId);
        TeamEntity teamEntity = teamStore.getTeamById(teamId);
        teamEntity.withNewMember(member);
        return teamStore.updateTeam(teamEntity).build();
    }

    @Override
    public Team newTeam(String name) {
        TeamEntity teamEntity = TeamEntity.builder()
                .withName(name);
        return teamStore.addNew(teamEntity).build();
    }

}
