package br.com.darksun.controller;

import br.com.darksun.model.Vote;
import br.com.darksun.service.VoteService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import static br.com.darksun.service.GuestService.GUEST_ROLE;
import static br.com.darksun.service.GuestService.HOST_ROLE;

@Path( "/votes" )
public class VoteController {
	@Inject
	VoteService service;

	@POST
	@Transactional
	@RolesAllowed( { HOST_ROLE, GUEST_ROLE } )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Response create( Vote vote, @Context SecurityContext securityContext ) {
		return Response.status( Response.Status.CREATED.getStatusCode( ) )
					   .entity( service.create( vote,
												securityContext.getUserPrincipal( ).getName( ) ) )
					   .build( );
	}

	@GET
	@RolesAllowed( HOST_ROLE )
	@Produces( MediaType.APPLICATION_JSON )
	public Response readAll( ) {
		return Response.ok( service.readAll( ) ).build( );
	}

	@GET
	@Path( "{id}" )
	@RolesAllowed( HOST_ROLE )
	@Produces( MediaType.APPLICATION_JSON )
	public Response readById( @PathParam( "id" ) Long id ) {
		return Response.ok( service.readById( id ) ).build( );
	}

	@DELETE
	@Path( "{id}" )
	@Transactional
	@RolesAllowed( HOST_ROLE )
	public Response delete( @PathParam( "id" ) Long id ) {
		service.delete( id );
		return Response.noContent( ).build( );
	}
}
