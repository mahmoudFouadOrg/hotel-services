package com.mfouad;

import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class CountryRestController {
	
	@Inject
	EntityManager entityManager;
	
	@Inject
	CountryService countryService;
	

	// Example endpoint
	 @GET
	 @Path("")
	 @Produces(MediaType.APPLICATION_JSON)
	 public List<Country> getAllCountries() {
		 
		 return entityManager.createQuery("SELECT c FROM Country c", Country.class).getResultList();
	 }
	 
	 @GET
	 @Path("/active")
	 @Produces(MediaType.APPLICATION_JSON)
	 public List<Country> getAllActiveCountries() {
		 
		return countryService.getAcitveCountries();
	 }
	 
	 @POST
	 @Path("")
	 @Produces(MediaType.APPLICATION_JSON)
		public Response createNewCountry(CountryReq countryReq) {

		 countryService.createCountry(countryReq);
			return Response.ok().build();
		 
		
	 }
	 
	 @GET
	 @Path("/{countryid}")
	 @Produces(MediaType.APPLICATION_JSON)
	 public Response getCountryName(@PathParam ("countryid") Long id) {
		 
         return Response.ok(countryService.getCountryNameById(id)).build();
	 }
	 
	 @PUT
	 @Path("/{countryid}/de-active")
	 @Produces(MediaType.APPLICATION_JSON)
	 public Response active(@PathParam ("countryid") Long id) {
		 countryService.activateDeactive(id);
		 
		 return Response.ok().build();
	 }

}
