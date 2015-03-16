package org.asciidoctor.editor.realtime.encoders;

import java.io.StringWriter;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.asciidoctor.editor.realtime.messages.OutputMessage;

public class OutputMessageEncoder implements Encoder.Text<OutputMessage> {

	private static final Logger logger = Logger
			.getLogger("OutputMessageEncoder");

	@Override
	public void init(EndpointConfig ec) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public String encode(OutputMessage m) {
		StringWriter swriter = new StringWriter();
		try {
			try (JsonWriter jsonWrite = Json.createWriter(swriter)) {
				JsonObjectBuilder builder = Json.createObjectBuilder();
				
				JsonObjectBuilder dataBuilder = Json.createObjectBuilder();
				dataBuilder.add("format", m.getFormat().toString()).add("currentWriter",
						m.getCurrentWriter()).add("source", m.getAdocSource()).add("timeToRender",
						m.getTimeToRender()).add("output", m.getContent());
				//addHeader
				if (m.getDocHeader() != null && m.getDocHeader()
						.getDocumentTitle() != null && m.getDocHeader().getAuthor() != null
						&& m.getDocHeader().getRevisionInfo().getNumber() != null){
					dataBuilder.add("docHeader",
							Json.createObjectBuilder()
									.add("title",
											m.getDocHeader()
													.getDocumentTitle().getMain())
									.add("author", m.getDocHeader().getAuthor().getFullName())
									.add("revisioninfo",
											m.getDocHeader()
													.getRevisionInfo()
													.getNumber()));
				}
				
				builder.add("type", m.getType().toString())
						.add("adocId", m.getAdocId())
						.add("data", dataBuilder.build());
				
				jsonWrite.writeObject(builder.build());
			}
		} catch (Exception e) {
			logger.severe("Output message decode error.");
			e.printStackTrace();
			Json.createWriter(swriter).writeObject(getDefault(m).build());
		}
		return swriter.toString();
	}

	private JsonObjectBuilder getDefault(OutputMessage m) {

		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("type", m.getType().toString())
				.add("adocId", m.getAdocId())
				.add("data",
						Json.createObjectBuilder()
								.add("format", "")
								.add("currentWriter", "")
								.add("docHeader", Json.createObjectBuilder())
								.add("source", m.getAdocSource())
								.add("timeToRender", "-1")
								.add("output",
										"<b>Error in Asciidoc source !</b> <br/>Check the following and re-try : "
												+ "<ul><li>Headers informations are mandatory (title, author name, revision, date)</li></lu>"));
		return builder;

	}
}
