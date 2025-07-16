package com.mfouad.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfouad.dto.CreateHotelReq;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class HotelResource {
	
	 ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
     Validator validator = factory.getValidator();
	
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
	public Response create(MultipartFormDataInput input) {
		
		 try {
	            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
	            if (uploadForm.containsKey("data")) {
	                String jsonData = uploadForm.get("data").get(0).getBodyAsString();
	                // Deserialize JSON if needed
	                System.out.println("Received JSON: " + jsonData);
	                ObjectMapper objectMapper = new ObjectMapper();
	                CreateHotelReq req=  objectMapper.readValue(jsonData, CreateHotelReq.class);
	                Set<ConstraintViolation<CreateHotelReq>> violations = validator.validate(req);
					if (!violations.isEmpty()) {
						StringBuilder errorMessage = new StringBuilder("Validation errors: ");
						for (ConstraintViolation<CreateHotelReq> violation : violations) {
							errorMessage.append(violation.getMessage()).append("; ");
						}
						return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage.toString()).build();
					}
	               
	            }
	            
	            if (uploadForm.containsKey("files")) {
	                for (InputPart filePart : uploadForm.get("files")) {
	                    InputStream fileInputStream = filePart.getBody(InputStream.class, null);
	                    // Process each file, e.g., save to disk or DB
	                    System.out.println("Received file: " + filePart.getFileName());
	                }
	            }

	            
		 }
			catch (Exception e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Error processing the request: " + e.getMessage()).build();
			}
	    return Response.ok().build();
	}

}
