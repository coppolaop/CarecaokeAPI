package br.com.darksun.controller;

import br.com.darksun.model.Guest;
import br.com.darksun.service.GuestService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path( "/guests" )
public class GuestController {

	@Inject
	GuestService service;

	@POST
	@Transactional
	@RolesAllowed( "host" )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Guest create( Guest guest ) {
		return service.create( guest );
	}

	@POST
	@Path( "invite/{name}" )
	@Transactional
	@RolesAllowed( "host" )
	@Produces( MediaType.APPLICATION_JSON )
	public Guest invite( @PathParam( "name" ) String name ) {
		return service.invite( name );
	}

	@GET
	@RolesAllowed( "host" )
	@Produces( MediaType.APPLICATION_JSON )
	public List< Guest > readAll( ) {
		return service.readAll( );
	}

	@GET
	@Path( "{id}" )
	@RolesAllowed( "host" )
	@Produces( MediaType.APPLICATION_JSON )
	public Guest readById( @PathParam( "id" ) Long id ) {
		return service.readById( id );
	}

	@GET
	@Path( "invitations" )
	@RolesAllowed( "host" )
	@Produces( MediaType.APPLICATION_JSON )
	public List< String > getAllInvitations( ) {
		return service.getAllInvitations( );
	}

	@PUT
	@Transactional
	@RolesAllowed( "host" )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Guest update( Guest guest ) {
		return service.update( guest );
	}

	@DELETE
	@Path( "{id}" )
	@Transactional
	@RolesAllowed( "host" )
	public void delete( @PathParam( "id" ) Long id ) {
		service.delete( id );
	}
}
