package br.com.darksun.controller;

import br.com.darksun.model.Guest;
import br.com.darksun.service.GuestService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path( "/guests" )
public class GuestController {
	@Inject
	GuestService service;

	@POST
	@Transactional
	@RolesAllowed( "host" )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Response create( Guest guest ) {
		return Response.status( Response.Status.CREATED.getStatusCode( ) )
					   .entity( service.create( guest ) )
					   .build( );
	}

	@POST
	@Path( "invite/{name}" )
	@Transactional
	@RolesAllowed( "host" )
	@Produces( MediaType.APPLICATION_JSON )
	public Response invite( @PathParam( "name" ) String name ) {
		return Response.status( Response.Status.CREATED.getStatusCode( ) )
					   .entity( service.invite( name ) )
					   .build( );
	}

	@GET
	@RolesAllowed( "host" )
	@Produces( MediaType.APPLICATION_JSON )
	public Response readAll( ) {
		return Response.ok( service.readAll( ) ).build( );
	}

	@GET
	@Path( "{id}" )
	@RolesAllowed( "host" )
	@Produces( MediaType.APPLICATION_JSON )
	public Response readById( @PathParam( "id" ) Long id ) {
		return Response.ok( service.readById( id ) ).build( );
	}

	@GET
	@Path( "invitations" )
	@RolesAllowed( "host" )
	@Produces( MediaType.APPLICATION_JSON )
	public Response getAllInvitations( ) {
		return Response.ok( service.getAllInvitations( ) ).build( );
	}

	@PUT
	@Transactional
	@RolesAllowed( "host" )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Response update( Guest guest ) {
		return Response.ok( service.update( guest ) ).build( );
	}

	@DELETE
	@Path( "{id}" )
	@Transactional
	@RolesAllowed( "host" )
	public Response delete( @PathParam( "id" ) Long id ) {
		service.delete( id );
		return Response.noContent( ).build( );
	}
}
