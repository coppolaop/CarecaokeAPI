package br.com.darksun.controller;

import br.com.darksun.TestUtils;
import br.com.darksun.model.Guest;
import br.com.darksun.model.Song;
import br.com.darksun.service.SongService;
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
import java.util.stream.Collectors;

import static br.com.darksun.service.GuestService.GUEST_ROLE;
import static br.com.darksun.service.GuestService.HOST_ROLE;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;

@QuarkusTest
public class SongControllerTest {
	@Inject
	SongController controller;
	@InjectMock
	SongService    service;

	private List< Song >  songList;
	private List< Guest > guestList;

	private static final Long    FIRST_SONG_ID              = 1L;
	private static final Integer FIRST_UNSUNG_SONG_POSITION = 2;

	@BeforeEach
	void setUp( ) {
		TestUtils utils = new TestUtils( );
		songList  = utils.getSongs( );
		guestList = utils.getGuests( );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void create_Success_Host( ) {
		Song newSong = new Song( null, "Wonderwall", "Oasis",
								 "https://www.youtube.com/watch?v=R_EXyGWI9rU", false, null,
								 guestList.getFirst( ) );
		when( service.create( any( ) ) ).thenReturn( songList.get( FIRST_UNSUNG_SONG_POSITION ) );

		Song response = ( ( Song ) controller.create( newSong ).getEntity( ) );

		Assertions.assertEquals( songList.get( FIRST_UNSUNG_SONG_POSITION ), response );
		verify( service, times( 1 ) ).create( any( Song.class ) );
		given( ).when( )
				.body( newSong )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.post( "/songs" )
				.then( )
				.statusCode( Response.Status.CREATED.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = GUEST_ROLE )
	public void create_Success_Guest( ) {
		Song newSong = new Song( null, "Wonderwall", "Oasis",
								 "https://www.youtube.com/watch?v=R_EXyGWI9rU", false, null,
								 guestList.getFirst( ) );
		when( service.create( any( ) ) ).thenReturn( songList.get( FIRST_UNSUNG_SONG_POSITION ) );

		Song response = ( ( Song ) controller.create( newSong ).getEntity( ) );

		Assertions.assertEquals( songList.get( FIRST_UNSUNG_SONG_POSITION ), response );
		verify( service, times( 1 ) ).create( any( Song.class ) );
		given( ).when( )
				.body( newSong )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.post( "/songs" )
				.then( )
				.statusCode( Response.Status.CREATED.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = "" )
	public void create_Fail_Credentials( ) {
		Song newSong = new Song( null, "Wonderwall", "Oasis",
								 "https://www.youtube.com/watch?v=R_EXyGWI9rU", false, null,
								 guestList.getFirst( ) );
		when( service.create( any( ) ) ).thenReturn( songList.getFirst( ) );

		boolean wasThrown = false;
		try {
			controller.create( newSong );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).create( any( Song.class ) );
		given( ).when( )
				.body( newSong )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.post( "/songs" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void readAll_Success( ) {
		when( service.readAll( ) ).thenReturn( songList );

		List< Song > response = ( ( List< Song > ) controller.readAll( ).getEntity( ) );

		Assertions.assertEquals( songList, response );
		verify( service, times( 1 ) ).readAll( );
		given( ).when( ).get( "/songs" ).then( ).statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = GUEST_ROLE )
	public void readAll_Fail_Credentials( ) {
		when( service.readAll( ) ).thenReturn( songList );

		boolean wasThrown = false;
		try {
			controller.readAll( );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).readAll( );
		given( ).when( )
				.get( "/songs" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void readById_Success( ) {
		when( service.readById( any( ) ) ).thenReturn( songList.getFirst( ) );

		Song response = ( ( Song ) controller.readById( any( ) ).getEntity( ) );

		Assertions.assertEquals( songList.getFirst( ), response );
		verify( service, times( 1 ) ).readById( any( ) );
		given( ).pathParam( "id", FIRST_SONG_ID )
				.when( )
				.get( "/songs/{id}" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = GUEST_ROLE )
	public void readById_Fail_Credentials( ) {
		when( service.readById( any( ) ) ).thenReturn( songList.getFirst( ) );

		boolean wasThrown = false;
		try {
			controller.readById( FIRST_SONG_ID );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).readById( any( ) );
		given( ).pathParam( "id", FIRST_SONG_ID )
				.when( )
				.get( "/songs/{id}" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void readById_Fail_NotFound( ) {
		when( service.readById( any( ) ) ).thenThrow(
				new EntityNotFoundException( "Song not found with ID: " + FIRST_SONG_ID ) );

		boolean wasThrown = false;
		try {
			controller.readById( FIRST_SONG_ID );
		} catch ( EntityNotFoundException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, times( 1 ) ).readById( any( ) );
		given( ).pathParam( "id", FIRST_SONG_ID )
				.when( )
				.get( "/songs/{id}" )
				.then( )
				.statusCode( Response.Status.NOT_FOUND.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void readMySongs_Success_Host( ) {
		SecurityContext securityContext = mock( SecurityContext.class );
		Principal       principal       = mock( Principal.class );
		when( securityContext.getUserPrincipal( ) ).thenReturn( principal );
		when( principal.getName( ) ).thenReturn( "tester" );
		when( service.readMySongs( any( ) ) ).thenReturn( songList );

		List< Song > response = ( ( List< Song > ) controller.readMySongs( securityContext )
															 .getEntity( ) );

		Assertions.assertEquals( songList, response );
		verify( service, times( 1 ) ).readMySongs( any( ) );
		given( ).when( )
				.get( "/songs/mine" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = GUEST_ROLE )
	public void readMySongs_Success_Guest( ) {
		SecurityContext securityContext = mock( SecurityContext.class );
		Principal       principal       = mock( Principal.class );
		when( securityContext.getUserPrincipal( ) ).thenReturn( principal );
		when( principal.getName( ) ).thenReturn( "tester" );
		when( service.readMySongs( any( ) ) ).thenReturn( songList );

		List< Song > response = ( ( List< Song > ) controller.readMySongs( securityContext )
															 .getEntity( ) );

		Assertions.assertEquals( songList, response );
		verify( service, times( 1 ) ).readMySongs( any( ) );
		given( ).when( )
				.get( "/songs/mine" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void readMySongs_Success_Empty( ) {
		SecurityContext securityContext = mock( SecurityContext.class );
		Principal       principal       = mock( Principal.class );
		when( securityContext.getUserPrincipal( ) ).thenReturn( principal );
		when( principal.getName( ) ).thenReturn( "tester" );
		List< Song > emptySongList = new ArrayList<>( );
		when( service.readMySongs( any( ) ) ).thenReturn( emptySongList );

		List< Song > response = ( ( List< Song > ) controller.readMySongs( securityContext )
															 .getEntity( ) );

		Assertions.assertEquals( emptySongList, response );
		Assertions.assertTrue( response.isEmpty( ) );
		verify( service, times( 1 ) ).readMySongs( any( ) );
		given( ).when( )
				.get( "/songs/mine" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = "" )
	public void readMySongs_Fail_Credentials( ) {
		SecurityContext securityContext = mock( SecurityContext.class );
		Principal       principal       = mock( Principal.class );
		when( securityContext.getUserPrincipal( ) ).thenReturn( principal );
		when( principal.getName( ) ).thenReturn( "tester" );

		boolean wasThrown = false;
		try {
			controller.readMySongs( securityContext );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).readById( any( ) );
		given( ).when( )
				.get( "/songs/mine" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void songList_Success_Host( ) {
		List< String > invitations = songList.stream( )
											 .map( Song::getName )
											 .collect( Collectors.toList( ) );
		when( service.readAllNextSongs( ) ).thenReturn( invitations );

		List< String > response = ( ( List< String > ) controller.songList( ).getEntity( ) );

		Assertions.assertEquals( invitations, response );
		verify( service, times( 1 ) ).readAllNextSongs( );
		given( ).when( )
				.get( "/songs/list" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = GUEST_ROLE )
	public void songList_Success_Guest( ) {
		List< String > invitations = songList.stream( )
											 .map( Song::getName )
											 .collect( Collectors.toList( ) );
		when( service.readAllNextSongs( ) ).thenReturn( invitations );

		List< String > response = ( ( List< String > ) controller.songList( ).getEntity( ) );

		Assertions.assertEquals( invitations, response );
		verify( service, times( 1 ) ).readAllNextSongs( );
		given( ).when( )
				.get( "/songs/list" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = { "" } )
	public void songList_Fail_Credentials( ) {
		boolean wasThrown = false;
		try {
			controller.songList( );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).readAllNextSongs( );
		given( ).when( )
				.get( "/songs/list" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void update_Success( ) {
		Song newSong = new Song( 1L, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 true, null, guestList.getFirst( ) );
		when( service.update( any( ) ) ).thenReturn( songList.getFirst( ) );

		Song response = ( ( Song ) controller.update( newSong ).getEntity( ) );

		Assertions.assertEquals( songList.getFirst( ), response );
		verify( service, times( 1 ) ).update( any( Song.class ) );
		given( ).when( )
				.body( newSong )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.put( "/songs" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = GUEST_ROLE )
	public void update_Fail_Credentials( ) {
		Song newSong = new Song( 1L, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 true, null, guestList.getFirst( ) );
		when( service.update( any( ) ) ).thenReturn( songList.getFirst( ) );

		boolean wasThrown = false;
		try {
			controller.update( newSong );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).update( any( Song.class ) );
		given( ).when( )
				.body( newSong )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.put( "/songs" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void update_Fail_BadRequest( ) {
		Song newSong = new Song( 1L, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 true, null, guestList.getFirst( ) );
		when( service.update( any( ) ) ).thenThrow(
				new IllegalArgumentException( "Song with no Id" ) );

		boolean wasThrown = false;
		try {
			controller.update( newSong );
		} catch ( IllegalArgumentException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, times( 1 ) ).update( any( Song.class ) );
		given( ).when( )
				.body( newSong )
				.contentType( MediaType.APPLICATION_JSON )
				.accept( MediaType.APPLICATION_JSON )
				.put( "/songs" )
				.then( )
				.statusCode( Response.Status.BAD_REQUEST.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void nextSong_Sucess_Simple( ) {
		when( service.nextSong( ) ).thenReturn( songList.get( 1 ) );

		Song response = ( ( Song ) controller.nextSong( ).getEntity( ) );

		Assertions.assertEquals( songList.get( 1 ), response );
		verify( service, times( 1 ) ).nextSong( );
		given( ).when( )
				.put( "/songs/next" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void nextSong_Sucess_Empty( ) {
		when( service.nextSong( ) ).thenReturn( null );

		Song response = ( ( Song ) controller.nextSong( ).getEntity( ) );

		Assertions.assertEquals( null, response );
		verify( service, times( 1 ) ).nextSong( );
		given( ).when( )
				.put( "/songs/next" )
				.then( )
				.statusCode( Response.Status.OK.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = GUEST_ROLE )
	public void nextSong_Fail_Credentials( ) {
		when( service.nextSong( ) ).thenReturn( songList.get( 1 ) );

		boolean wasThrown = false;
		try {
			controller.nextSong( );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).update( any( Song.class ) );
		given( ).when( )
				.contentType( MediaType.APPLICATION_JSON )
				.put( "/songs/next" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void delete_Success( ) {
		doNothing( ).when( service ).delete( any( ) );

		controller.delete( FIRST_SONG_ID );

		verify( service, times( 1 ) ).delete( any( ) );
		given( ).pathParam( "id", FIRST_SONG_ID )
				.when( )
				.delete( "/songs/{id}" )
				.then( )
				.statusCode( Response.Status.NO_CONTENT.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = "" )
	public void delete_Fail_Credentials( ) {
		doNothing( ).when( service ).delete( any( ) );

		boolean wasThrown = false;
		try {
			controller.delete( FIRST_SONG_ID );
		} catch ( ForbiddenException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, never( ) ).delete( any( ) );
		given( ).pathParam( "id", FIRST_SONG_ID )
				.when( )
				.delete( "/songs/{id}" )
				.then( )
				.statusCode( Response.Status.FORBIDDEN.getStatusCode( ) );
	}

	@Test
	@TestSecurity( user = "tester", roles = HOST_ROLE )
	public void delete_Fail_NotFound( ) {
		doThrow( new EntityNotFoundException( ) ).when( service ).delete( any( ) );

		boolean wasThrown = false;
		try {
			controller.delete( FIRST_SONG_ID );
		} catch ( EntityNotFoundException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( service, times( 1 ) ).delete( any( ) );
		given( ).pathParam( "id", FIRST_SONG_ID )
				.when( )
				.delete( "/songs/{id}" )
				.then( )
				.statusCode( Response.Status.NOT_FOUND.getStatusCode( ) );
	}

	//TODO deleteMySong_Success_Host
	//TODO deleteMySong_Success_Guest
	//TODO deleteMySong_Success_Empty
	//TODO deleteMySong_Fail_Credentials
}
