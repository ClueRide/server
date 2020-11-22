package com.clueride.domain.flag;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class FlagTest {

    @Inject
    private FlagEntity flagEntity;

    @Deployment
    public static JavaArchive createDeployment() {
        PomEquippedResolveStage pomEquippedResolveStage = Maven.resolver().loadPomFromFile("pom.xml");
        Archive<?>[] dependencies = pomEquippedResolveStage
                .resolve("org.apache.commons:commons-lang3").withTransitivity().as(JavaArchive.class);
        JavaArchive javaArchive = ShrinkWrap.create(JavaArchive.class)
                .addClass("com.clueride.domain.flag.Flag")
                .addClass("com.clueride.domain.flag.FlagEntity")
                .addClass("com.clueride.domain.flag.FlaggedAttribute")
                .addClass("com.clueride.domain.flag.reason.FlagReason")
                .addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("META-INF/persistence.xml");
        for (Archive<?> archive : dependencies) {
            javaArchive.merge(archive);
        }
        for (Archive<?> archive : pomEquippedResolveStage.resolve(
                "org.slf4j:slf4j-api"
        ).withTransitivity().as(JavaArchive.class)) {
            javaArchive.merge(archive);
        }
        return javaArchive;
    }

    @Test
    public void testEquals() {
        Flag expectedFlag = flagEntity.build();
        FlagEntity entityFromFlag = FlagEntity.from(expectedFlag);

        assertEquals(expectedFlag, entityFromFlag.build());
        assertEquals(flagEntity, entityFromFlag);
    }

}
