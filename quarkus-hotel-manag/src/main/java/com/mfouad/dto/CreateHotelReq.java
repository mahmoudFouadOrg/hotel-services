package com.mfouad.dto;

import java.io.InputStream;
import java.util.List;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import jakarta.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateHotelReq {
	
	@FormParam("hotel")
    @PartType("application/json")
	HotelJsonReq hotelJsonReq;
	
	 @FormParam("files")
	    @PartType("application/octet-stream") // Accepts binary data
	    public List<FilePart> files;
	 
	 
		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		@Builder
		// This class represents a file part in the multipart request
		// It can be used to upload files along with the hotel data
	 public static class FilePart {
	        @PartType("application/octet-stream")
	        public InputStream file;

	        public String fileName;
	    }
	
	
}
