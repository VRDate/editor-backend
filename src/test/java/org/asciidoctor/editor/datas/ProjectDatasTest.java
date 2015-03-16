package org.asciidoctor.editor.datas;

import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.editor.DeploymentInContainer;
import org.asciidoctor.editor.core.AsciidoctorProcessor;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.websocket.DeploymentException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import static org.asciidoctor.OptionsBuilder.options;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class ProjectDatasTest extends DeploymentInContainer {

    @Inject
    ProjectDatas datas;

    @Test
    @InSequence(1)
    public void should_convert_adoc_from_firebase_to_pdf() throws URISyntaxException,
            DeploymentException, IOException, InterruptedException {

        String projectID = "adoc/test.adoc";
        String fileID = "sample.pdf";

        datas.listenToAsciiDocContent(projectID, fileID);
        assertTrue(true);
    }


    /**
     * Gets a resourse in a similar way as {@link java.io.File#File(String)}
     */
    public File getResource(String pathname) {
        try {
            URL resource = Thread.currentThread().getContextClassLoader().getResource(pathname);
            if (resource != null) {
                return new File(Thread.currentThread().getContextClassLoader().getResource(pathname).toURI());
            }
            else {
                throw new RuntimeException(new FileNotFoundException(pathname));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String getResourceAsString(String path){
        try {
            URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
            if (resource != null) {
                return readFromStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
            }
            else {
                throw new RuntimeException(new FileNotFoundException(path));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String readFromStream(final InputStream is) {
        if (is == null) {
            return "";
        }
        final char[] buffer = new char[1024];
        final StringBuilder out = new StringBuilder();
        try {
            final Reader in = new InputStreamReader(is, "UTF-8");
            try {
                for (;;) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            } finally {
                in.close();
            }
        } catch (UnsupportedEncodingException ex) {
			/* ... */
        } catch (IOException ex) {
			/* ... */
        }
        return out.toString();
    }

}
