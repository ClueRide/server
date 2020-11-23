package com.clueride.domain.game;

import com.clueride.domain.outing.OutingView;
import com.clueride.domain.outing.OutingViewEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class TestResources {

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

//    @Produces
//    ConfigService produceConfigService() {
//        return Mockito.mock(ConfigService.class);
//    }
//

    @Produces
    public OutingView produceOutingView() {
        System.out.println("Instantiating an OutingView");
        return OutingViewEntity.builder()
                .build();
    }

}
