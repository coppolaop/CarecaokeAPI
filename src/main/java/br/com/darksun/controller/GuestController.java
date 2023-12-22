package br.com.darksun.controller;

import br.com.darksun.model.Guest;
import br.com.darksun.service.GuestService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static br.com.darksun.service.GuestService.HOST_ROLE;

@Path( "/guests" )
public class GuestController {
	@Inject
	GuestService service;

	@POST
	@Transactional
	@RolesAllowed( HOST_ROLE )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Response create( Guest guest ) {
		return Response.status( Response.Status.CREATED.getStatusCode( ) )
					   .entity( service.create( guest ) )
					   .build( );
	}

	@POST
	@Path( "host/first" )
	@Transactional
	@PermitAll
	@Produces( MediaType.APPLICATION_JSON )
	public Response createFirstHost( ) {
		return Response.status( Response.Status.CREATED.getStatusCode( ) )
					   .entity( service.firstHost( ) )
					   .build( );
	}

	@POST
	@Path( "invite/{name}" )
	@Transactional
	@RolesAllowed( HOST_ROLE )
	@Produces( MediaType.APPLICATION_JSON )
	public Response invite( @PathParam( "name" ) String name ) {
		return Response.status( Response.Status.CREATED.getStatusCode( ) )
					   .entity( service.invite( name ) )
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

	@GET
	@Path( "invitations" )
	@RolesAllowed( HOST_ROLE )
	@Produces( MediaType.APPLICATION_JSON )
	public Response getAllInvitations( ) {
		return Response.ok( service.getAllInvitations( ) ).build( );
	}

	@PUT
	@Transactional
	@RolesAllowed( HOST_ROLE )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Response update( Guest guest ) {
		return Response.ok( service.update( guest ) ).build( );
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
