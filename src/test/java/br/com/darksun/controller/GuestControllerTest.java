package br.com.darksun.controller;

import br.com.darksun.TestUtils;
import br.com.darksun.model.Guest;
import br.com.darksun.service.GuestService;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.ForbiddenException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.darksun.service.GuestService.HOST_ROLE;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;

@QuarkusTest
public class GuestControllerTest {
	@Inject
	GuestController controller;
	@InjectMock
	GuestService    service;

	private List< Guest > guestList;

	private static final Long FIRST_GUEST_ID = 1L;

	@BeforeEach
	void setUp( ) {
		TestUtils utils = new TestUtils( );
		guestList = utils.getGuests( );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "host" } )
	public void create_Success( ) {
		Guest newGuest = new Guest( null, "Coppola", "123123", HOST_ROLE );
		when( service.create( any( ) ) ).thenReturn( guestList.getFirst( ) );

		Guest response = ( ( Guest ) controller.create( newGuest ).getEntity( ) );

		Assertions.assertTrue(
				BcryptUtil.matches( newGuest.getPassword( ), response.getPassword( ) ) );
		Assertions.assertEquals( guestList.getFirst( ), response );
		verify( service, times( 1 ) ).create( any( Guest.class ) );
		given( ).when( )
				.body( newGuest )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.post( "/guests" )
				.then( )
				.statusCode( Response.Status.CREATED.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "guest" } )
	public void create_Fail_Credentials( ) {
		Guest newGuest = new Guest( null, "Coppola", "123123", HOST_ROLE );
		when( service.create( any( ) ) ).thenReturn( guestList.getFirst( ) );

		boolean wasThrown = false;
		try {
			controller.create( newGuest );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).create( any( Guest.class ) );
		given( ).when( )
				.body( newGuest )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.post( "/guests" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "host" } )
	public void readAll_Success( ) {
		when( service.readAll( ) ).thenReturn( guestList );

		List< Guest > response = ( ( List< Guest > ) controller.readAll( ).getEntity( ) );

		Assertions.assertEquals( guestList, response );
		verify( service, times( 1 ) ).readAll( );
		given( ).when( ).get( "/guests" ).then( ).statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "guest" } )
	public void readAll_Fail_Credentials( ) {
		when( service.readAll( ) ).thenReturn( guestList );

		boolean wasThrown = false;
		try {
			controller.readAll( );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).readAll( );
		given( ).when( )
				.get( "/guests" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "host" } )
	public void readById_Success( ) {
		when( service.readById( any( ) ) ).thenReturn( guestList.getFirst( ) );

		Guest response = ( ( Guest ) controller.readById( any( ) ).getEntity( ) );

		Assertions.assertEquals( guestList.getFirst( ), response );
		verify( service, times( 1 ) ).readById( any( ) );
		given( ).pathParam( "id", FIRST_GUEST_ID )
				.when( )
				.get( "/guests/{id}" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "guest" } )
	public void readById_Fail_Credentials( ) {
		when( service.readById( any( ) ) ).thenReturn( guestList.getFirst( ) );

		boolean wasThrown = false;
		try {
			controller.readById( FIRST_GUEST_ID );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).readById( any( ) );
		given( ).pathParam( "id", FIRST_GUEST_ID )
				.when( )
				.get( "/guests/{id}" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "host" } )
	public void getAllInvitations_Success( ) {
		List< String > invitations = guestList.stream( )
											  .map( Guest::getName )
											  .collect( Collectors.toList( ) );
		when( service.getAllInvitations( ) ).thenReturn( invitations );

		List< String > response = ( ( List< String > ) controller.getAllInvitations( )
																 .getEntity( ) );

		Assertions.assertEquals( invitations, response );
		verify( service, times( 1 ) ).getAllInvitations( );
		given( ).when( )
				.get( "/guests/invitations" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "guest" } )
	public void getAllInvitations_Fail_Credentials( ) {
		boolean wasThrown = false;
		try {
			controller.getAllInvitations( );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).getAllInvitations( );
		given( ).when( )
				.get( "/guests/invitations" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "host" } )
	public void update_Success( ) {
		Guest newGuest = new Guest( null, "Coppola", "123123", HOST_ROLE );
		when( service.update( any( ) ) ).thenReturn( guestList.getFirst( ) );

		Guest response = ( ( Guest ) controller.update( newGuest ).getEntity( ) );

		Assertions.assertTrue(
				BcryptUtil.matches( newGuest.getPassword( ), response.getPassword( ) ) );
		Assertions.assertEquals( guestList.getFirst( ), response );
		verify( service, times( 1 ) ).update( any( Guest.class ) );
		given( ).when( )
				.body( newGuest )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.put( "/guests" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "guest" } )
	public void update_Fail_Credentials( ) {
		Guest newGuest = new Guest( null, "Coppola", "123123", HOST_ROLE );
		when( service.update( any( ) ) ).thenReturn( guestList.getFirst( ) );

		boolean wasThrown = false;
		try {
			controller.update( newGuest );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).update( any( Guest.class ) );
		given( ).when( )
				.body( newGuest )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.put( "/guests" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "host" } )
	public void delete_Success( ) {
		doNothing( ).when( service ).delete( any( ) );

		controller.delete( FIRST_GUEST_ID );

		verify( service, times( 1 ) ).delete( any( ) );
		given( ).pathParam( "id", FIRST_GUEST_ID )
				.when( )
				.delete( "/guests/{id}" )
				.then( )
				.statusCode( Response.Status.NO_CONTENT.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "guest" } )
	public void delete_Fail_Credentials( ) {
		doNothing( ).when( service ).delete( any( ) );

		boolean wasThrown = false;
		try {
			controller.delete( FIRST_GUEST_ID );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).delete( any( ) );
		given( ).pathParam( "id", FIRST_GUEST_ID )
				.when( )
				.delete( "/guests/{id}" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}
}
