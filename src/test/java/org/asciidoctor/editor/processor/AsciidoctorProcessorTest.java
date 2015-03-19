package org.asciidoctor.editor.processor;

import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.SafeMode;
import org.asciidoctor.editor.DeploymentInContainer;
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
public class AsciidoctorProcessorTest extends DeploymentInContainer {

    private String data = "{\"type\":\"adoc-for-html5\",\"source\":\"= Real-time collaborative editor for AsciiDoc\\nMaxime GREAU <greaumaxime@gmail.com>\\nv0.1.0, March 28, 2014\\n:backend: html5\\n:toc:\\n:toclevels: 3\\n:imagesdir: ../images\\n\\n== Introduction\\n\\nThis project gives you the possibility to *work on the same AsciiDoc file with a team and see the rendering in realtime*.\",\"writer\":\"@mgreau\"}";
    private String dataConvertToPDF = "= Document Title\\n Maxime GREAU <greaumaxime@gmail.com>\\n\\nContent : {asciidoctor-version}.\\n[source,java]\\n----\\nSystem.out.println(Hello, World!);\\n----\"";


    @Inject
    AsciidoctorProcessor asciidoctor;




    //@Test
    //@InSequence(1)
    public void should_convert_adoc_to_html() throws URISyntaxException,
            DeploymentException, IOException, InterruptedException {

        Map<String, Object> parameters;
        parameters = options()
                .backend("html5")
                .safe(SafeMode.UNSAFE).headerFooter(true)
                .eruby("erubis")
                .attributes(
                        AttributesBuilder.attributes()
                                .attribute("icons!", "")
                                .attribute("allow-uri-read")
                                .attribute("copycss!", "").asMap())
                .asMap();

        AsciidoctorProcessor asciidoctor = new AsciidoctorProcessor();
        asciidoctor.init();

        final String result = asciidoctor.getDelegate().render(dataConvertToPDF, parameters);
        assertNotNull(result);
        assertEquals(result.contains("<h1>Document Title"), true);

        long start = System.currentTimeMillis();
        final String result2 = asciidoctor.getDelegate().convert(getResourceAsString("adoc/websocket.adoc"), parameters);
        assertNotNull(result2);
        System.out.println("time : " + (System.currentTimeMillis() - start));
    }

    @Test
    @InSequence(2)
    public void should_convert_adoc_to_pdf() throws URISyntaxException,
            DeploymentException, IOException, InterruptedException {

        String inputFilename = "adoc/test.adoc";
        String outputFilename = "sample.pdf";
        byte[] res ;
        res = asciidoctor.convertToBinaryDocument(getResourceAsString(inputFilename), Converter.pdf, null, "all", outputFilename);
        assertTrue(res != null);
    }



    /**
     * Gets a resourse in a similar way as {@link File#File(String)}
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
