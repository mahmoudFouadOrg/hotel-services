package com.mfouad;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class FileResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response UploadFile(UploadFileReq req) {
    	
    	return Response.ok(UploadFileRes.builder()
    			.fileName(req.getName())
    			.url("randomurl")
    			.build())
    			.build();
    	
    }
    
}
