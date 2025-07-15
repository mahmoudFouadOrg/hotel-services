package com.mfouad.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CustomExceptionHandler implements ExceptionMapper<CountryNotFoundException> {

	@Override
	public Response toResponse(CountryNotFoundException exception) {
			return Response.status(Response.Status.NOT_FOUND).entity("Country not found").build();
	}

}
