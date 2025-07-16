package org.mfouad.exceptions;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CustomExceptionHandler implements ExceptionMapper<RuntimeException> {

	@Override
	public jakarta.ws.rs.core.Response toResponse(RuntimeException exception) {
		
		
		return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.NOT_FOUND)
				.entity("Country not found: " + exception.getMessage()).build();
	}

}
