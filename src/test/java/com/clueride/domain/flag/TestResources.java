package com.clueride.domain.flag;

import com.clueride.config.ConfigService;
import com.clueride.domain.flag.reason.FlagReason;
import com.clueride.util.TestOnly;
import org.mockito.Mockito;

import javax.ws.rs.Produces;

public class TestResources {

    @Produces
    ConfigService produceConfigService() {
        return Mockito.mock(ConfigService.class);
    }

    @Produces
    FlagService produceFlagService() {
        return Mockito.mock(FlagService.class);
    }

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
