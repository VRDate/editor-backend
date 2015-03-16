package org.asciidoctor.editor;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;

/**
 * Created by mgreau on 06/02/15.
 */
@RunWith(Arquillian.class)
public class DeploymentInContainer {

    @org.jboss.arquillian.container.test.api.Deployment(testable = false)
    public static WebArchive createDeployment() {
        return BackendArchive.getBackendArchive();
    }
}
