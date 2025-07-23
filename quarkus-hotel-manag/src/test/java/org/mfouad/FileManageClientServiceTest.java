package org.mfouad;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mfouad.services.FileMangeClientService;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class FileManageClientServiceTest {
	
	@Inject
	FileMangeClientService service;
	
	@BeforeEach
	void init() {
		// Initialize any required data or state before each test
	}
	
	@Test
	void uploadFileTest() {
		
		assertNotNull(service);
		
	}

}
