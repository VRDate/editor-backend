package org.asciidoctor.editor.realtime;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.to;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.asciidoctor.editor.DeploymentAsClient;
import org.asciidoctor.editor.StarterService;
import org.asciidoctor.editor.realtime.client.MyBasicEndpointClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BackendServerEndpointTest extends DeploymentAsClient {

	final String ADOC_URL = "adoc/";
	final String ADOC_ID = "121213";

	@ArquillianResource
	URI base;

	@Test
	@InSequence(1)
	public void testNotificationOnOpenConnection() throws URISyntaxException,
			DeploymentException, IOException, InterruptedException {
		// onOpen - notifOnOpen
		MyBasicEndpointClient.latch = new CountDownLatch(2);

		Session session1 = connectToServer(MyBasicEndpointClient.class, ADOC_URL
				+ ADOC_ID);
		assertNotNull(session1);
		Session session2 = null;

		try {
			assertTrue(MyBasicEndpointClient.latch.await(2, TimeUnit.SECONDS));
			//One user connected
			assertEquals(getNotificationOnOpenConnection("1"),
					MyBasicEndpointClient.notificationMessage);
			
			MyBasicEndpointClient.latch = new CountDownLatch(2);
			session2 = connectToServer(MyBasicEndpointClient.class, ADOC_URL
					+ ADOC_ID);
			assertNotNull(session2);
			assertTrue(MyBasicEndpointClient.latch.await(2, TimeUnit.SECONDS));
			//Two users connected
			assertEquals(getNotificationOnOpenConnection("2"),
					MyBasicEndpointClient.notificationMessage);
			
		} finally {
			session1.close();
			session2.close();
		}
	}

	@Test
	@InSequence(2)
	public void testNotificationWhenBecameAWriter() throws URISyntaxException,
			DeploymentException, IOException, InterruptedException {
		final String writer = "@mgreau";
		final String JSONNotificationWhenBecameAWriter = "{\"type\":\"notification\",\"adocId\":\""
				+ ADOC_ID
				+ "\",\"data\":{\"nbConnected\":0,\"nbWriters\":1,\"writers\":{\""
				+ writer + "\":\"" + writer + "\"}}}";

		// notifOnOpen - notifWhenSend Adoc - output
		MyBasicEndpointClient endpoint = new MyBasicEndpointClient();
		Session session = connectToServer(MyBasicEndpointClient.class, ADOC_URL
				+ ADOC_ID);
		assertNotNull(session);

		session.getBasicRemote().sendText(dataConvertToPDF);
		await().timeout(50000, TimeUnit.MILLISECONDS).untilCall(to(endpoint).getNotificationMessage(), is(equalTo(JSONNotificationWhenBecameAWriter)));
	}

	/**
	 * WebSocket is not yet supported by default by Arquillian, so we need to
	 * change the schema manually.
	 * 
	 * @param endpoint
	 * @param uriPart
	 * @return
	 * @throws DeploymentException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public Session connectToServer(Class<?> endpoint, String uriPart)
			throws DeploymentException, IOException, URISyntaxException {
		WebSocketContainer container = ContainerProvider
				.getWebSocketContainer();
		assertNotNull(container);
		assertNotNull(base);
		URI uri = new URI("ws://" + base.getHost() + ":" + base.getPort()
				+ base.getPath() + uriPart);
		System.out.println("Connecting to: " + uri);
		return container.connectToServer(endpoint, uri);
	}

	private String data = "{\"type\":\"adoc-for-html5\",\"source\":\"= Real-time collaborative editor for AsciiDoc\\nMaxime GREAU <greaumaxime@gmail.com>\\nv0.1.0, March 28, 2014\\n:backend: html5\\n:toc:\\n:toclevels: 3\\n:imagesdir: ../images\\n\\n== Introduction\\n\\nThis project gives you the possibility to *work on the same AsciiDoc file with a team and see the rendering in realtime*.\",\"writer\":\"@mgreau\"}";

	private String dataConvertToPDF = "{\"type\":\"adoc-for-pdf\",\"source\":\"= Document Title\\nMaxime GREAU <greaumaxime@gmail.com>\\n\\nContent.\\n[source,java]\\n----\\nSystem.out.println(Hello, World!);\\n----\",\"writer\":\"@mgreau\"}";


	private String getNotificationOnOpenConnection(String nb){
		return "{\"type\":\"notification\",\"adocId\":\""
				+ ADOC_ID
				+ "\",\"data\":{\"nbConnected\":"+nb+",\"nbWriters\":0,\"writers\":{}}}";
	}
}
