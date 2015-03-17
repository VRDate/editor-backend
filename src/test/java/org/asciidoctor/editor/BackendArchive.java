package org.asciidoctor.editor;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

/**
 * Created by mgreau on 10/02/15.
 */
public final class BackendArchive {

    public static final WebArchive getBackendArchive(){
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, StarterService.class.getPackage())
                .addAsResource("adoc/sample.adoc")
                .addAsResource("adoc/test.adoc")
                .addAsResource("config/editor-backend.properties", "editor-backend.properties")
                .addAsManifestResource("config/MANIFEST.MF", "MANIFEST.MF")
                .addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml").resolve("org.jsoup:jsoup").withTransitivity().asFile())
                        //Firebase
                .addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml").resolve("com.firebase:firebase-client-jvm").withTransitivity().asFile())
                        //DeltaSpike
                .addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml").resolve("org.apache.deltaspike.core:deltaspike-core-impl").withTransitivity().asFile())
                .addAsWebInfResource("config/beans.xml", "beans.xml");
    }
}
