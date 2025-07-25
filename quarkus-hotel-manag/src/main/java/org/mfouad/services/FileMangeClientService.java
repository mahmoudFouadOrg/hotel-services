package org.mfouad.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.mfouad.dto.UploadFileReq;
import com.mfouad.dto.UploadFileRes;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/")
@RegisterRestClient(configKey="files-api")
public interface  FileMangeClientService {
	
	@POST
	@Path("")
	public UploadFileRes uploadFile(UploadFileReq req);

}
