package br.com.darksun.controller;

import br.com.darksun.TestUtils;
import br.com.darksun.model.Guest;
import br.com.darksun.model.Song;
import br.com.darksun.model.Vote;
import br.com.darksun.service.VoteService;
import io.quarkus.security.ForbiddenException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static br.com.darksun.service.GuestService.GUEST_ROLE;
import static br.com.darksun.service.GuestService.HOST_ROLE;
import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class VoteControllerTest {
	@Inject
	VoteController controller;
	@InjectMock
	VoteService    service;

	private List< Vote >  voteList;
	private List< Guest > guestList;
	private List< Song >  songList;

	private static final Long FIRST_VOTE_ID = 1L;

	@BeforeEach
	void setUp( ) {
		TestUtils utils = new TestUtils( );
		voteList  = utils.getVotes( );
		guestList = utils.getGuests( );
		songList  = utils.getSongs( );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	void create_Success_Host( ) {
		SecurityContext securityContext = mock( SecurityContext.class );
		Principal       principal       = mock( Principal.class );
		songList.getFirst( ).setCreatedAt( null );
		when( securityContext.getUserPrincipal( ) ).thenReturn( principal );
		when( principal.getName( ) ).thenReturn( voteList.getFirst( ).getWhoVotes( ).getName( ) );
		Vote newVote = new Vote( null, Short.valueOf( "3" ), songList.getFirst( ),
								 guestList.get( 0 ) );
		when( service.create( any( ), any( ) ) ).thenReturn( voteList.getFirst( ) );

		Vote response = ( ( Vote ) controller.create( newVote, securityContext ).getEntity( ) );

		Assertions.assertEquals( voteList.getFirst( ), response );
		verify( service, times( 1 ) ).create( any( Vote.class ), any( ) );
		given( ).when( )
				.body( newVote )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.post( "/votes" )
				.then( )
				.statusCode( Response.Status.CREATED.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	void create_Success_Guest( ) {
		SecurityContext securityContext = mock( SecurityContext.class );
		Principal       principal       = mock( Principal.class );
		songList.getFirst( ).setCreatedAt( null );
		when( securityContext.getUserPrincipal( ) ).thenReturn( principal );
		when( principal.getName( ) ).thenReturn( voteList.getFirst( ).getWhoVotes( ).getName( ) );
		Vote newVote = new Vote( null, Short.valueOf( "3" ), songList.getFirst( ),
								 guestList.get( 0 ) );
		when( service.create( any( ), any( ) ) ).thenReturn( voteList.getFirst( ) );

		Vote response = ( ( Vote ) controller.create( newVote, securityContext ).getEntity( ) );

		Assertions.assertEquals( voteList.getFirst( ), response );
		verify( service, times( 1 ) ).create( any( Vote.class ), any( ) );
		given( ).when( )
				.body( newVote )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.post( "/votes" )
				.then( )
				.statusCode( Response.Status.CREATED.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = "" )
	void create_Fail_Credential( ) {
		SecurityContext securityContext = mock( SecurityContext.class );
		Principal       principal       = mock( Principal.class );
		songList.getFirst( ).setCreatedAt( null );
		when( securityContext.getUserPrincipal( ) ).thenReturn( principal );
		when( principal.getName( ) ).thenReturn( voteList.getFirst( ).getWhoVotes( ).getName( ) );
		Vote newVote = new Vote( null, Short.valueOf( "3" ), songList.getFirst( ),
								 guestList.get( 0 ) );
		when( service.create( any( ), any( ) ) ).thenReturn( voteList.getFirst( ) );

		boolean wasThrown = false;
		try {
			controller.create( newVote, securityContext );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).create( any( Vote.class ), any( ) );
		given( ).when( )
				.body( newVote )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.post( "/votes" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	void readAll_Success_Simple( ) {
		when( service.readAll( ) ).thenReturn( voteList );

		List< Vote > response = ( ( List< Vote > ) controller.readAll( ).getEntity( ) );

		Assertions.assertEquals( voteList, response );
		verify( service, times( 1 ) ).readAll( );
		given( ).when( ).get( "/votes" ).then( ).statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	void readAll_Success_Empty( ) {
		List< Vote > newVoteList = new ArrayList<>( );
		when( service.readAll( ) ).thenReturn( newVoteList );

		List< Vote > response = ( ( List< Vote > ) controller.readAll( ).getEntity( ) );

		Assertions.assertEquals( newVoteList, response );
		Assertions.assertTrue( response.isEmpty( ) );
		verify( service, times( 1 ) ).readAll( );
		given( ).when( ).get( "/votes" ).then( ).statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = "" )
	void readAll_Fail_Credential( ) {
		when( service.readAll( ) ).thenReturn( voteList );

		boolean wasThrown = false;
		try {
			controller.readAll( );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).readAll( );
		given( ).when( )
				.get( "/votes" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	void readById_Success( ) {
		when( service.readById( any( ) ) ).thenReturn( voteList.getFirst( ) );

		Vote response = ( ( Vote ) controller.readById( any( ) ).getEntity( ) );

		Assertions.assertEquals( voteList.getFirst( ), response );
		verify( service, times( 1 ) ).readById( any( ) );
		given( ).pathParam( "id", FIRST_VOTE_ID )
				.when( )
				.get( "/votes/{id}" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = "" )
	void readById_Fail_Credential( ) {
		when( service.readById( any( ) ) ).thenReturn( voteList.getFirst( ) );

		boolean wasThrown = false;
		try {
			controller.readById( FIRST_VOTE_ID );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).readById( any( ) );
		given( ).pathParam( "id", FIRST_VOTE_ID )
				.when( )
				.get( "/votes/{id}" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	void readById_Fail_NotFound( ) {
		when( service.readById( any( ) ) ).thenThrow(
				new EntityNotFoundException( "Song not found with ID: " + FIRST_VOTE_ID ) );

		boolean wasThrown = false;
		try {
			controller.readById( FIRST_VOTE_ID );
		} catch ( EntityNotFoundException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, times( 1 ) ).readById( any( ) );
		given( ).pathParam( "id", FIRST_VOTE_ID )
				.when( )
				.get( "/votes/{id}" )
				.then( )
				.statusCode( Response.Status.NOT_FOUND.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	void delete_Success( ) {
		doNothing( ).when( service ).delete( any( ) );

		controller.delete( FIRST_VOTE_ID );

		verify( service, times( 1 ) ).delete( any( ) );
		given( ).pathParam( "id", FIRST_VOTE_ID )
				.when( )
				.delete( "/votes/{id}" )
				.then( )
				.statusCode( Response.Status.NO_CONTENT.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = GUEST_ROLE )
	void delete_Fail_Credential( ) {
		doNothing( ).when( service ).delete( any( ) );

		boolean wasThrown = false;
		try {
			controller.delete( FIRST_VOTE_ID );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).delete( any( ) );
		given( ).pathParam( "id", FIRST_VOTE_ID )
				.when( )
				.delete( "/votes/{id}" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	void delete_Fail_NotFound( ) {
		doThrow( new EntityNotFoundException( ) ).when( service ).delete( any( ) );

		boolean wasThrown = false;
		try {
			controller.delete( FIRST_VOTE_ID );
		} catch ( EntityNotFoundException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, times( 1 ) ).delete( any( ) );
		given( ).pathParam( "id", FIRST_VOTE_ID )
				.when( )
				.delete( "/votes/{id}" )
				.then( )
				.statusCode( Response.Status.NOT_FOUND.getStatusCode( ) );
	}
}