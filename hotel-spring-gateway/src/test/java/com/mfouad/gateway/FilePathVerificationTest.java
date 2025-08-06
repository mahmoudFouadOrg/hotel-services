package com.mfouad.gateway;

import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilePathVerificationTest {
	
	
	@Test
	public void getResourcePath() {
        try {
        	
        	ClassPathResource protoFile = new ClassPathResource("proto/hotelSearch.proto");
            ClassPathResource descriptorFile = new ClassPathResource("proto/hotelSearch.pb");
            System.out.println("path is "+ protoFile.getPath() );
            
            System.out.println("path is "+protoFile.getFile().getAbsolutePath());
            
            System.out.println("Proto file exists: " + protoFile.exists());
            
            Resource resource = new ClassPathResource("application.yaml");
            Path path = Paths.get(resource.getURI());
            String absolutePath = path.toAbsolutePath().toString();
            System.out.println("Absolute path: " + absolutePath);
            
            // Verify that the file exists
            File file = new File(absolutePath);
            assertTrue(file.exists(), "File does not exist at the specified path: " + absolutePath);
        } catch (IOException e) {
            fail("Failed to get resource path: " + e.getMessage());
        }
    }

    
}
