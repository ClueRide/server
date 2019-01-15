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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.persistence.NoResultException;

import org.slf4j.Logger;

import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.account.principal.PrincipalService;

//import com.clueride.domain.account.member.Member;

/**
 * Implementation of Badge Event service which dispatches events from a queue that is populated by clients.
 *
 * The dispatch involves persisting and logging of the events.
 */
@ApplicationScoped
public class BadgeEventServiceImpl implements BadgeEventService {
    @Inject
    private Logger LOGGER;

    private static BlockingQueue<BadgeEventBuilder> eventQueue = new LinkedTransferQueue<>();
    private static Thread workerThread = null;
    private boolean runnable = true;

    @Inject
    private BadgeEventStore badgeEventStore;
    @Inject
    private MemberService memberService;
    @Inject
    private PrincipalService principalService;

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
    public void send(BadgeEventBuilder badgeEvent) {
        try {
            synchronized(eventQueue) {
                eventQueue.put(badgeEvent);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BadgeEvent getBadgeEventById(Integer badgeEventId) {
        BadgeEventBuilder badgeEventBuilder = badgeEventStore.getById(badgeEventId);
//        fillDbBuilder(badgeEventBuilder);
        return badgeEventBuilder.build();
    }

    // TODO: Tie this into the database to create the event records
//    private void fillDbBuilder(BadgeEvent.Builder badgeEventBuilder) {
//        Member member = memberService.getMember(badgeEventBuilder.getMemberId());
//        badgeEventBuilder.withPrincipal(
//                principalService.getPrincipalForEmailAddress(
//                        member.getEmailAddress()
//                )
//        );
//    }

    private void fillClientBuilder(BadgeEventBuilder badgeEventBuilder) throws AddressException {
        BadgeOsPrincipal principal = (BadgeOsPrincipal) badgeEventBuilder.getPrincipal();
        Integer memberId = memberService.getMemberByEmail(
                principal.getEmailAddress().toString()
        ).getId();

        badgeEventBuilder.withMemberId(memberId);
    }

    public class Worker implements Runnable {

        @Override
        public void run() {
            while (runnable) {
                BadgeEventBuilder badgeEventBuilder = null;
                try {
                    badgeEventBuilder = eventQueue.take();
                    fillClientBuilder(badgeEventBuilder);
                    LOGGER.info("Captured Event: " + badgeEventBuilder.toString());
//                    badgeEventStore.add(badgeEventBuilder);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    runnable = false;
                } catch (NoResultException nre) {
                    LOGGER.error("Unable to find {} as a Member",
                            ((BadgeOsPrincipal)badgeEventBuilder.getPrincipal()).getEmailAddress()
                    );
                    throw nre;
                } catch (RuntimeException rte) {
                    LOGGER.error("Problem Storing Badge Event: " + badgeEventBuilder.getTimestamp(), rte);
                    throw(rte);
                } catch (AddressException e) {
                    LOGGER.error("Address Problem Storing Badge Event: " + badgeEventBuilder.getTimestamp(), e);
                    e.printStackTrace();
                }

            }
        }

    }


}
