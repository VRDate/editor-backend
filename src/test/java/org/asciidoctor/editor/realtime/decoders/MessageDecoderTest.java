package org.asciidoctor.editor.realtime.decoders;

import javax.inject.Inject;

import org.asciidoctor.editor.StarterService;
import org.asciidoctor.editor.core.AsciidoctorProcessor;
import org.asciidoctor.editor.realtime.messages.AsciidocMessage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class MessageDecoderTest {


	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackages(true, StarterService.class.getPackage())
				.addAsManifestResource("config/MANIFEST.MF")
				.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml").resolve("org.jsoup:jsoup").withTransitivity().asFile())
				.addAsWebInfResource("config/beans.xml");
	}


	@Inject
	MessageDecoder msgDecoder;

	@Inject
	AsciidoctorProcessor processor;

	private final String data = "= Real-time collaborative editor for AsciiDoc\\nMaxime GREAU <greaumaxime@gmail.com>\\nv0.1.0, March 28, 2014\\n:backend: html5\\n:toc:\\n:toclevels: 3\\n:imagesdir: ../images\\n\\n== Introduction\\n\\nThis project gives you the possibility to *work on the same AsciiDoc file with a team and see the rendering in realtime*.";


	@Test
	@InSequence(1)
	public void shouldNotDecodeJSONMessage() {
		assertNotNull(msgDecoder);

		final String JSON = "{\"type\" : \"aadoc\", \"sourrrce\" : \"texttest\", \"writer\": \"max\"}";
		AsciidocMessage adoc = null;

		try {
			adoc = msgDecoder.decode(JSON);
		} catch (Exception e) {
			assertEquals("[Message] Can't decode.", e.getMessage());
		}
		assertNull(adoc);
	}

	@Test
	@InSequence(2)
	public void shouldProcessMessage() {
		assertNotNull(msgDecoder);

		String out = null;

		try {
			out = processor.convertToDocument(data);
		} catch (Exception e) {
			System.out.println("error  : " + e.getMessage());
			fail("[Message] Can't decode." + e.getMessage());
		}
		assertNotNull(out);
		System.out.println("output html : " + out);
	}
}
