package org.asciidoctor.editor.api;

import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;

import org.asciidoctor.editor.DeploymentAsClient;
import org.asciidoctor.editor.realtime.messages.OutputMessage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AsciidoctorEndpointTest extends DeploymentAsClient {

	private String asciidoc = "= Document Title\\n Maxime GREAU <greaumaxime@gmail.com>\\n\\nContent : {asciidoctor-version}.\\n[source,java]\\n----\\nSystem.out.println(Hello, World!);\\n----\"";

	@Test
	@InSequence(1)
	public void should_convert_adoc_to_pdf(@ArquillianResource URI uri) {

		Client client = ClientBuilder.newClient();
		WebTarget w = client.target(uri);

		Future<OutputMessage> om = w.path("/convert/pdf").request(asciidoc)
				.async().get(

				new InvocationCallback<OutputMessage>() {

					@Override
					public void completed(OutputMessage om) {
						System.out.println("ok");

					}

					@Override
					public void failed(Throwable arg0) {
						System.out.println("Error");

					}

				});

		assertTrue(om != null);
	}

}
