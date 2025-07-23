package com.mfouad.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.mfouad.services.HotelService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfouad.dto.CreateHotelReq;
import com.mfouad.dto.HotelJsonReq;
import com.mfouad.dto.CreateHotelReq.FilePart;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Path("/")
public class HotelResource {
	
	 ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
     Validator validator = factory.getValidator();
     
     @Inject
     HotelService hotelService;
	
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
	public Response create(@MultipartForm CreateHotelReq input ) {
		
	                Set<ConstraintViolation<HotelJsonReq>> violations = validator.validate(input.getHotelJsonReq());
					if (!violations.isEmpty()) {
						StringBuilder errorMessage = new StringBuilder("Validation errors: ");
						for (ConstraintViolation<HotelJsonReq> violation : violations) {
							errorMessage.append(violation.getMessage()).append("; ");
						}
						return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage.toString()).build();
					}
	               
	            
	            if (input.getFiles() != null && !input.getFiles().isEmpty()) {
	                for (FilePart filePart : input.getFiles()  ) {
	                    filePart.getFile();
	                    // Process each file, e.g., save to disk or DB
	                    System.out.println("Received file: " + filePart.getFileName());
	                }
	            }

	            
	    return Response.ok().build();
	}
	
	@GET
	@Consumes({MediaType.APPLICATION_JSON})
	public Response searchHotel(@QueryParam ("countryid") Long id, @QueryParam ("name") String name) {
		
        return Response.ok(hotelService.findHotels(id , name)).build();
	}
}
