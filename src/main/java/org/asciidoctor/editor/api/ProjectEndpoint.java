package org.asciidoctor.editor.api;

import org.asciidoctor.editor.datas.ProjectDatas;

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
	@Path("/{projectId}/file/{fileId}/listener")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response listenToAsciiDoc(@PathParam("projectId") String projectId,
                                 @PathParam("fileId") String fileId) {
       datas.listenToAsciiDocContent(projectId, fileId);
       return Response.ok().build();
	}
	


	
	
}