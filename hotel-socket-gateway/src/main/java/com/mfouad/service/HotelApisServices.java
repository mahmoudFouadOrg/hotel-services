package com.mfouad.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.mfouad.dto.UpdateHotelActivationDTO;
import com.mfouad.dto.UpdateHotelActivationResDTO;

import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@Path("/")
@RegisterRestClient(configKey="hotels-countries")
public interface HotelApisServices {
	
	
	
	@PUT
	@Path("/de-active/internal")
	public UpdateHotelActivationResDTO uploadFile(UpdateHotelActivationDTO req);

}
