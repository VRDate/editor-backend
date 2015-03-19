package org.asciidoctor.editor.api;

import org.asciidoctor.editor.processor.AsciidoctorProcessor;
import org.asciidoctor.editor.processor.Converter;
import org.asciidoctor.editor.realtime.messages.OutputMessage;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@Path("/asciidoctor")
@Stateless
public class AsciidoctorEndpoint {

	@Inject
    AsciidoctorProcessor processor;
	
	@POST
	@Path("/convert/{backend}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	@Asynchronous
	public void convert(@PathParam("backend") String backend, 
			String asciidocContent, @Suspended AsyncResponse response) {
		Converter converter;
		switch (backend) {
			case "pdf":
				converter = Converter.pdf;
				break;
			case "slide":
				converter = Converter.slide;
				break;
			default:
				converter = Converter.html5;
		}

		OutputMessage out = new OutputMessage(converter);
		out.setContent(processor.convertToDocument(asciidocContent, converter));
		response.resume(out);
	}
	
	@GET
	@Path("/version")
	@Produces(MediaType.TEXT_PLAIN)
	public String version(){
		return processor.getDelegate().asciidoctorVersion();
	}

	
	
}