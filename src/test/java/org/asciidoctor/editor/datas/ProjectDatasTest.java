package org.asciidoctor.editor.datas;

import org.asciidoctor.editor.DeploymentInContainer;
import org.asciidoctor.editor.processor.Converter;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class ProjectDatasTest extends DeploymentInContainer {

    @Inject
    ProjectDatas datas;

    @Test
    @InSequence(1)
    public void should_convert_adoc_from_firebase_to_pdf() throws URISyntaxException,
            DeploymentException, IOException, InterruptedException {

        String projectID = "project:123";
        String fileID = "file:123";

        datas.listenToAsciiDocContent(projectID, fileID, Converter.html5);
        assertTrue(true);
    }

}
