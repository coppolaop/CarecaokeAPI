package br.com.darksun.controller;

import br.com.darksun.model.Song;
import br.com.darksun.service.SongService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path( "/songs" )
public class SongController {
	@Inject
	SongService service;

	@POST
	@Transactional
	@RolesAllowed( { "host", "guest" } )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Response create( Song song ) {
		return Response.status( Response.Status.CREATED.getStatusCode( ) )
					   .entity( service.create( song ) )
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
	@Path( "next" )
	@RolesAllowed( { "host", "guest" } )
	@Produces( MediaType.APPLICATION_JSON )
	public Response nextSongs( ) {
		return Response.ok( service.getNextSongs( ) ).build( );
	}

	@PUT
	@Transactional
	@RolesAllowed( "host" )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Response update( Song song ) {
		return Response.ok( service.update( song ) ).build( );
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
