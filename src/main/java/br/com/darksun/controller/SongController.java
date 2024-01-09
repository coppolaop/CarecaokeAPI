package br.com.darksun.controller;

import br.com.darksun.model.Song;
import br.com.darksun.service.SongService;
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

@Path( "/songs" )
public class SongController {
	@Inject
	SongService service;

	@POST
	@Transactional
	@RolesAllowed( { HOST_ROLE, GUEST_ROLE } )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Response create( Song song ) {
		return Response.status( Response.Status.CREATED.getStatusCode( ) )
					   .entity( service.create( song ) )
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
	@Path( "mine" )
	@RolesAllowed( { HOST_ROLE, GUEST_ROLE } )
	@Produces( MediaType.APPLICATION_JSON )
	public Response readMySongs( @Context SecurityContext securityContext ) {
		return Response.ok( service.readMySongs( securityContext.getUserPrincipal( ).getName( ) ) )
					   .build( );
	}

	@GET
	@Path( "list" )
	@RolesAllowed( { HOST_ROLE, GUEST_ROLE } )
	@Produces( MediaType.APPLICATION_JSON )
	public Response songList( ) {
		return Response.ok( service.readAllNextSongs( ) ).build( );
	}

	@PUT
	@Transactional
	@RolesAllowed( HOST_ROLE )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Response update( Song song ) {
		return Response.ok( service.update( song ) ).build( );
	}

	@PATCH
	@Path( "next" )
	@Transactional
	@RolesAllowed( { HOST_ROLE } )
	@Produces( MediaType.APPLICATION_JSON )
	public Response nextSong( ) {
		return Response.ok( service.nextSong( ) ).build( );
	}

	@DELETE
	@Path( "{id}" )
	@Transactional
	@RolesAllowed( HOST_ROLE )
	public Response delete( @PathParam( "id" ) Long id ) {
		service.delete( id );
		return Response.noContent( ).build( );
	}

	@DELETE
	@Path( "mine/{name}" )
	@Transactional
	@RolesAllowed( { HOST_ROLE, GUEST_ROLE } )
	public Response deleteMySong( @PathParam( "name" ) String name,
								  @Context SecurityContext securityContext ) {
		service.deleteMySong( name, securityContext.getUserPrincipal( ).getName( ) );
		return Response.noContent( ).build( );
	}
}
