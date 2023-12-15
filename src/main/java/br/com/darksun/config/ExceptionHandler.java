package br.com.darksun.config;

import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper< Exception > {
	@Override
	public Response toResponse( Exception exception ) {
		return mapExceptionToResponse( exception );
	}

	private Response mapExceptionToResponse( Exception exception ) {
		switch ( exception ) {
			case WebApplicationException webApplicationException -> {
				Response originalErrorResponse = webApplicationException.getResponse( );
				return Response.fromResponse( originalErrorResponse )
							   .entity( exception.getMessage( ) )
							   .build( );
			}
			case IllegalArgumentException illegalArgumentException -> {
				return Response.status( Response.Status.BAD_REQUEST )
							   .entity( exception.getMessage( ) )
							   .build( );
			}
			case EntityNotFoundException entityNotFoundException -> {
				return Response.status( Response.Status.NOT_FOUND )
							   .entity( exception.getMessage( ) )
							   .build( );
			}
			case null, default -> {
				return Response.serverError( ).entity( "Internal Server Error" ).build( );
			}
		}
	}
}
