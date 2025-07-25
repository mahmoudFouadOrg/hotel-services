package org.mfouad;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mfouad.services.FileMangeClientService;

import com.mfouad.dto.UploadFileReq;
import com.mfouad.dto.UploadFileRes;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

@QuarkusTest
public class FileManageClientServiceTest {
	
	@Inject
	@RestClient
	FileMangeClientService service;
	
	 @ConfigProperty(name = "hotels.filesUpload.url")
     String hotelsFilesUploadUrl;
	
	@BeforeEach
	void init() {
		// Initialize any required data or state before each test
	}
	
	@Test
	void uploadFileTest() throws IOException {
		
		assertNotNull(hotelsFilesUploadUrl);
		
		
		UploadFileRes res = service.uploadFile(
				UploadFileReq.builder().fileContent(getClass().getResourceAsStream("/test.txt").readAllBytes())
				.name("test.txt").build());
		
		assertNotNull(res);
		assertNotNull(res.getFileName());
		assertNotNull(res.getUrl());
		
		
	}

}
