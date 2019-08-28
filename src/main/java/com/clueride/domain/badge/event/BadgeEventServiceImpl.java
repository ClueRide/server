/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 11/25/17.
 */
package com.clueride.domain.badge.event;

import java.security.Principal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.persistence.NoResultException;

import org.slf4j.Logger;

import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.account.principal.PrincipalService;
import com.clueride.domain.achievement.award.AwardAchievementService;
import com.clueride.domain.team.Team;
import com.clueride.domain.team.TeamService;

/**
 * Implementation of Badge Event service which dispatches events from a queue that is populated by clients.
 *
 * The dispatch involves persisting and logging of the events.
 */
@ApplicationScoped
public class BadgeEventServiceImpl implements BadgeEventService {
    @Inject
    private Logger LOGGER;

    private static BlockingQueue<BadgeEventEntity> eventQueue = new LinkedTransferQueue<>();
    private static Thread workerThread = null;
    private boolean runnable = true;

    @Inject
    private BadgeEventStore badgeEventStore;
    @Inject
    private MemberService memberService;
    @Inject
    private PrincipalService principalService;
    @Inject
    private TeamService teamService;
    @Inject
    private AwardAchievementService awardAchievementService;

    @PostConstruct
    public void postConstruction() {
        if (workerThread == null) {
            workerThread = new Thread(
                    new BadgeEventServiceImpl.Worker()
            );
            workerThread.start();
        }
    }

    @Override
    public void send(BadgeEventEntity badgeEventEntity) {
        try {
            synchronized(eventQueue) {
                eventQueue.put(badgeEventEntity);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BadgeEvent getBadgeEventById(Integer badgeEventId) {
        BadgeEventEntity badgeEventEntity = badgeEventStore.getById(badgeEventId);
        fillDbBuilder(badgeEventEntity);
        return badgeEventEntity.build();
    }

    @Override
    public void sendToTeam(BadgeEventEntity badgeEventEntityCommon, int teamId) {
        BadgeEvent badgeEventSharedValues = badgeEventEntityCommon.build();
        Team team = teamService.getTeam(teamId);
        for (Member member : team.getMembers()) {
            BadgeEventEntity builder = BadgeEventEntity.from(badgeEventSharedValues);
            Principal principal = principalService.getPrincipalForEmailAddress(member.getEmailAddress());
            builder.withPrincipal(principal);
            send(builder);
        }
    }

    private void fillDbBuilder(BadgeEventEntity badgeEventEntity) {
        Member member = memberService.getMember(badgeEventEntity.getMemberId());
        badgeEventEntity.withPrincipal(
                principalService.getPrincipalForEmailAddress(
                        member.getEmailAddress()
                )
        );
    }

    private void fillClientBuilder(BadgeEventEntity badgeEventEntity) throws AddressException {
        BadgeOsPrincipal principal = (BadgeOsPrincipal) badgeEventEntity.getPrincipal();
        Member member = memberService.getMemberByEmail(
                principal.getEmailAddress().toString()
        );

        badgeEventEntity.withMemberId(member.getId());
        badgeEventEntity.withBadgeOSId(member.getBadgeOSId());
    }

    public class Worker implements Runnable {

        @Override
        public void run() {
            while (runnable) {
                BadgeEventEntity badgeEventEntity = null;
                try {
                    badgeEventEntity = eventQueue.take();
                    fillClientBuilder(badgeEventEntity);
                    LOGGER.info("Captured Event: " + badgeEventEntity.toString());
                    badgeEventStore.add(badgeEventEntity);
                    awardAchievementService.awardPotentialAchievement(badgeEventEntity.build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    runnable = false;
                } catch (NoResultException nre) {
                    LOGGER.error("Unable to find {} as a Member",
                            ((BadgeOsPrincipal) badgeEventEntity.getPrincipal()).getEmailAddress()
                    );
                    throw nre;
                } catch (RuntimeException rte) {
                    LOGGER.error("Problem Storing Badge Event: " + badgeEventEntity.getTimestamp(), rte);
                    throw(rte);
                } catch (AddressException e) {
                    LOGGER.error("Address Problem Storing Badge Event: " + badgeEventEntity.getTimestamp(), e);
                    e.printStackTrace();
                }

            }
        }

    }

}
