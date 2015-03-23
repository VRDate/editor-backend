package org.asciidoctor.editor.api;

import org.asciidoctor.editor.datas.ProjectDatas;
import org.asciidoctor.editor.processor.Converter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/project")
@Stateless
public class ProjectEndpoint {

    @Inject
    ProjectDatas datas;

    @GET
    @Path("/{projectId}/file/{fileId}/listener/{backend}")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response listenToAsciiDoc(@PathParam("projectId") String projectId,
                                     @PathParam("fileId") String fileId, @PathParam("backend") String backend) {
        switch (backend) {
            case "pdf":
                datas.listenToAsciiDocContent(projectId, fileId, Converter.pdf);
                break;
            case "dzslides":
                datas.listenToAsciiDocContent(projectId, fileId, Converter.dzslides);
                break;
            case "html5":
                datas.listenToAsciiDocContent(projectId, fileId, Converter.html5);
                break;
            default:
                datas.listenToAsciiDocContent(projectId, fileId, Converter.html5);
        }


       return Response.ok().build();
	}
	


	
	
}