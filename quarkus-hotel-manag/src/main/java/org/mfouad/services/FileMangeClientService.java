package org.mfouad.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.mfouad.dto.UploadFileReq;
import com.mfouad.dto.UploadFileRes;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/")
@RegisterRestClient(configKey="filesMang-apis")
public interface FileMangeClientService {
	
	@POST
	UploadFileRes uploadFile(UploadFileReq upReq);

}
