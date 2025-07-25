package com.mfouad.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.mfouad.services.HotelService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfouad.dto.CreateHotelReq;
import com.mfouad.dto.HotelJsonReq;
import com.mfouad.dto.CreateHotelReq.HotelFilePart;

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
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class HotelResource {
	
	 ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
     Validator validator = factory.getValidator();
     
     @Inject
     HotelService hotelService;
     
     private ObjectMapper mapper = new ObjectMapper();
     
     private static final Logger log =  Logger.getLogger(HotelResource.class);
	
	
	@POST
	@Path("")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA})
	public Response create(MultipartFormDataInput input ) throws IOException {
		
		InputPart hotelPart = input.getFormDataMap().get("hotel").get(0);
        String json = hotelPart.getBodyAsString();
        log.info("Received hotel JSON: " + json);
        HotelJsonReq hotel = mapper.readValue(json, HotelJsonReq.class);
        log.info("Received hotel rooms : " + hotel.getRooms());
		
	                Set<ConstraintViolation<HotelJsonReq>> violations = validator.validate(hotel);
					if (!violations.isEmpty()) {
						StringBuilder errorMessage = new StringBuilder("Validation errors: ");
						for (ConstraintViolation<HotelJsonReq> violation : violations) {
							errorMessage.append(violation.getMessage()).append("; ");
						}
						return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage.toString()).build();
					}
					
					CreateHotelReq createRequest = CreateHotelReq.builder().hotelJsonReq(hotel).files(new ArrayList<HotelFilePart>()).build();
					
					if(input.getFormDataMap().get("files") != null) {
					
					 List<InputPart> fileParts = input.getFormDataMap().get("files");

			            for (InputPart filePart : fileParts) {
			                // Get uploaded file name
			                String fileName = filePart.getHeaders()
			                    .getFirst("Content-Disposition")
			                    .replaceAll("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");

			                InputStream fileStream = filePart.getBody(InputStream.class, null);
			                createRequest.getFiles().add(new HotelFilePart(fileStream, fileName));
			                // Process fileStream & fileName (save, etc.)
			            }
					}
					hotelService.createNewHotel(createRequest);	
	            
	          

	            
	    return Response.ok().build();
	}
	
	@GET
	@Path("")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public Response searchHotel(@QueryParam ("countryid") Long id, @QueryParam ("name") String name) {
		
        return Response.ok(hotelService.findHotels(id , name)).build();
	}
	
	
	@GET
	@Path("{id}/images")
	@Produces({MediaType.APPLICATION_JSON})
	
public Response getHotelImages(@PathParam ("id") String id) {
		
        return Response.ok(hotelService.getHotelImages(id)).build();
	}
	
}
