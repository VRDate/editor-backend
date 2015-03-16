package org.asciidoctor.editor.api;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.asciidoctor.editor.core.AsciidoctorProcessor;
import org.asciidoctor.editor.realtime.messages.Message;
import org.asciidoctor.editor.realtime.messages.OutputMessage;
import org.asciidoctor.editor.realtime.messages.TypeFormat;

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
		OutputMessage out = new OutputMessage(TypeFormat.pdf);
		out.setContent(processor.convertToDocument(asciidocContent, backend));	
		response.resume(out);
	}
	
	@GET
	@Path("/version")
	@Produces(MediaType.TEXT_PLAIN)
	public String version(){
		return processor.getDelegate().asciidoctorVersion();
	}

	
	
}