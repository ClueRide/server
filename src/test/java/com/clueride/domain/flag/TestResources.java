package com.clueride.domain.flag;

import com.clueride.domain.flag.reason.FlagReason;
import com.clueride.util.TestOnly;

import javax.ws.rs.Produces;

public class TestResources {

    @Produces
    @TestOnly
    FlagEntity produceFlagEntity() {
        return FlagEntity.builder()
                .withId(123)
                .withAttractionId(100)
                .withReason(FlagReason.FUN_FACTOR)
                .withFlaggedAttribute(FlaggedAttribute.MAIN_LINK)
                .withDescription("Link is no longer reachable")
                .withOpenBadgeEventId(13)
                ;
    }

}
