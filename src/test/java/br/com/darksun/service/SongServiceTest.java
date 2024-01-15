package br.com.darksun.service;

import br.com.darksun.TestUtils;
import br.com.darksun.model.Guest;
import br.com.darksun.model.Song;
import br.com.darksun.repository.SongRepository;
import br.com.darksun.util.Utils;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static br.com.darksun.TestUtils.mockList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class SongServiceTest {
	@Inject
	SongService    service;
	@InjectMock
	SongRepository repository;
	@InjectMock
	GuestService   guestService;

	private List< Song >  songList;
	private List< Guest > guestList;

	private static final Long    FIRST_SONG_ID = 1L;
	private static       Integer FIRST_UNSUNG_SONG_POSITION;

	@BeforeEach
	void setUp( ) {
		TestUtils utils = new TestUtils( );
		songList                   = utils.getSongs( );
		guestList                  = utils.getGuests( );
		FIRST_UNSUNG_SONG_POSITION = utils.getFirstUnsong( );
	}

	@Test
	void create_Success_Simple( ) {
		Song newSong = new Song( null, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 null, null, guestList.getFirst( ) );
		aux_Create_Success( newSong );
	}

	@Test
	void create_Success_WithId( ) {
		Song newSong = new Song( null, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 null, null, guestList.getFirst( ) );
		aux_Create_Success( newSong );
	}

	@Test
	void create_Success_WithHasBeenSungTrue( ) {
		Song newSong = new Song( null, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 true, null, guestList.getFirst( ) );
		aux_Create_Success( newSong );
	}

	@Test
	void create_Success_WithCreatedAt( ) {
		Song newSong = new Song( null, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 null, LocalDateTime.now( ), guestList.getFirst( ) );
		aux_Create_Success( newSong );
	}

	@Test
	void create_Success_GuestWithJustId( ) {
		Guest guest = new Guest( );
		guest.setId( 1L );
		Song newSong = new Song( null, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 null, null, guest );
		aux_Create_Success( newSong );
	}

	private void aux_Create_Success( Song newSong ) {
		when( guestService.readById( any( ) ) ).thenReturn( guestList.getFirst( ) );
		when( repository.findAllBySinger( any( ) ) ).thenReturn( new ArrayList<>( ) );
		doNothing( ).when( repository ).persist( any( Song.class ) );
		Song firstSong = songList.getFirst( );

		Song response = service.create( newSong );
		response.setId( 1L );

		Assertions.assertFalse( response.getHasBeenSung( ) );
		Assertions.assertTrue(
				response.getCreatedAt( ).isAfter( LocalDateTime.now( ).minusSeconds( 1 ) ) );
		Assertions.assertTrue(
				response.getCreatedAt( ).isBefore( LocalDateTime.now( ).plusSeconds( 1 ) ) );
		firstSong.setHasBeenSung( false );
		firstSong.setCreatedAt( response.getCreatedAt( ) );
		Assertions.assertEquals( firstSong, response );
		verify( guestService, times( 1 ) ).readById( any( ) );
		verify( repository, times( 1 ) ).findAllBySinger( any( ) );
		verify( repository, times( 1 ) ).persist( any( Song.class ) );
	}

	@Test
	void create_Fail_NullGuest( ) {
		Song newSong = new Song( null, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 null, LocalDateTime.now( ), null );
		boolean wasThrown = false;
		try {
			service.create( newSong );
		} catch ( IllegalArgumentException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( guestService, never( ) ).readById( any( ) );
		verify( repository, never( ) ).findAllBySinger( any( ) );
		verify( repository, never( ) ).persist( any( Song.class ) );
	}

	@Test
	void create_Fail_GuestWithoutId( ) {
		Guest fakeGuest = new Guest( );
		Song newSong = new Song( null, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 null, LocalDateTime.now( ), fakeGuest );
		boolean wasThrown = false;
		try {
			service.create( newSong );
		} catch ( IllegalArgumentException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( guestService, never( ) ).readById( any( ) );
		verify( repository, never( ) ).findAllBySinger( any( ) );
		verify( repository, never( ) ).persist( any( Song.class ) );
	}

	@Test
	void create_Fail_SongsWithSameName( ) {
		Song newSong = new Song( null, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 null, null, guestList.getFirst( ) );
		when( guestService.readById( any( ) ) ).thenReturn( guestList.getFirst( ) );
		when( repository.findAllBySinger( any( ) ) ).thenReturn( songList );

		boolean wasThrown = false;
		try {
			service.create( newSong );
		} catch ( IllegalArgumentException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( guestService, times( 1 ) ).readById( any( ) );
		verify( repository, times( 1 ) ).findAllBySinger( any( ) );
		verify( repository, never( ) ).persist( any( Song.class ) );
	}

	@Test
	void readAll_Success_Simple( ) {
		PanacheQuery query = mockList( songList );
		when( repository.findAll( ) ).thenReturn( query );

		List< Song > response = service.readAll( );

		Assertions.assertEquals( songList, response );
		verify( repository, times( 1 ) ).findAll( );
	}

	@Test
	void readAll_Success_Empty( ) {
		PanacheQuery query = mockList( new ArrayList( ) );
		when( repository.findAll( ) ).thenReturn( query );

		List< Song > response = service.readAll( );

		Assertions.assertTrue( response.isEmpty( ) );
		verify( repository, times( 1 ) ).findAll( );
	}

	@Test
	void readById_Success( ) {
		when( repository.findByIdOptional( any( ) ) ).thenReturn(
				Optional.of( songList.getFirst( ) ) );

		Song response = service.readById( FIRST_SONG_ID );

		Assertions.assertEquals( songList.getFirst( ), response );
		verify( repository, times( 1 ) ).findByIdOptional( any( ) );
	}

	@Test
	void readById_Fail_NotFound( ) {
		when( repository.findByIdOptional( any( ) ) ).thenReturn( Optional.empty( ) );

		boolean wasThrown = false;
		try {
			service.readById( FIRST_SONG_ID );
		} catch ( EntityNotFoundException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( repository, times( 1 ) ).findByIdOptional( any( ) );
	}

	@Test
	void readMySongs_Succes_Simple( ) {
		List< Song > songsOfFirstGuest = getSongsOfFirstGuest( );
		when( guestService.readByName( any( ) ) ).thenReturn( guestList.getFirst( ) );
		when( repository.findAllBySinger( any( ) ) ).thenReturn( songsOfFirstGuest );

		List< Song > response = service.readMySongs( guestList.getFirst( ).getName( ) );

		Assertions.assertEquals( songsOfFirstGuest, response );
		verify( guestService, times( 1 ) ).readByName( any( ) );
		verify( repository, times( 1 ) ).findAllBySinger( any( ) );
	}

	private List< Song > getSongsOfFirstGuest( ) {
		List< Song > songsOfFirstGuest = new ArrayList<>( );
		for ( Song song : songList ) {
			if ( song.getSinger( ).equals( guestList.getFirst( ) ) ) {
				songsOfFirstGuest.add( song );
			}
		}
		return songsOfFirstGuest;
	}

	@Test
	void readMySongs_Success_Empty( ) {
		List< Song > songsOfFirstGuest = new ArrayList<>( );
		when( guestService.readByName( any( ) ) ).thenReturn( guestList.getFirst( ) );
		when( repository.findAllBySinger( any( ) ) ).thenReturn( songsOfFirstGuest );

		List< Song > response = service.readMySongs( guestList.getFirst( ).getName( ) );

		Assertions.assertEquals( songsOfFirstGuest, response );
		verify( guestService, times( 1 ) ).readByName( any( ) );
		verify( repository, times( 1 ) ).findAllBySinger( any( ) );
	}

	@Test
	void readMySongs_Fail_NotFound( ) {
		when( guestService.readByName( any( ) ) ).thenThrow( new EntityNotFoundException(
				"Guest not found with name: " + guestList.getFirst( ) ) );

		boolean wasThrown = false;
		try {
			service.readMySongs( guestList.getFirst( ).getName( ) );
		} catch ( EntityNotFoundException e ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( guestService, times( 1 ) ).readByName( any( ) );
		verify( repository, never( ) ).findAllBySinger( any( ) );
	}

	@Test
	void readAllNextSongs_Success_Simple( ) {
		List< Song > unsungSongs = songList.subList( FIRST_UNSUNG_SONG_POSITION, songList.size( ) );
		List< String > formatedUnsungSongs = new ArrayList<>( );
		unsungSongs.stream( )
				   .forEach( song -> formatedUnsungSongs.add( Utils.formatSingerAndSong( song ) ) );
		when( repository.findAllByHasNotBeenSung( ) ).thenReturn( unsungSongs );

		List< String > response = service.readAllNextSongs( );

		Assertions.assertEquals( formatedUnsungSongs, response );
		verify( repository, times( 1 ) ).findAllByHasNotBeenSung( );
	}

	@Test
	void readAllNextSongs_Success_Empty( ) {
		List< Song >   unsungSongs         = new ArrayList<>( );
		List< String > formatedUnsungSongs = new ArrayList<>( );
		unsungSongs.stream( )
				   .forEach( song -> formatedUnsungSongs.add( Utils.formatSingerAndSong( song ) ) );
		when( repository.findAllByHasNotBeenSung( ) ).thenReturn( unsungSongs );

		List< String > response = service.readAllNextSongs( );

		Assertions.assertEquals( formatedUnsungSongs, response );
		verify( repository, times( 1 ) ).findAllByHasNotBeenSung( );
	}

	@Test
	void update_Success_Simple( ) {
		Song newSong = new Song( 1L, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 true, songList.getFirst( ).getCreatedAt( ),
								 guestList.getFirst( ) );
		when( repository.findByIdOptional( any( ) ) ).thenReturn(
				Optional.of( songList.getFirst( ) ) );
		when( guestService.readById( any( ) ) ).thenReturn( guestList.getFirst( ) );
		when( repository.findAllBySinger( any( ) ) ).thenReturn( new ArrayList<>( ) );
		EntityManager entityManager = Mockito.mock( EntityManager.class );
		when( repository.getEntityManager( ) ).thenReturn( entityManager );
		when( repository.getEntityManager( ).merge( any( ) ) ).thenReturn(
				Optional.of( guestList.getFirst( ) ) );

		Song response = service.update( newSong );

		Assertions.assertTrue( response.getHasBeenSung( ) );
		Assertions.assertEquals( songList.getFirst( ), response );
		verify( repository, times( 1 ) ).findByIdOptional( any( ) );
		verify( guestService, times( 1 ) ).readById( any( ) );
		verify( repository, times( 1 ) ).findAllBySinger( any( ) );
		verify( entityManager, times( 1 ) ).merge( any( Song.class ) );
	}

	@Test
	void update_Success_WithoutCreatedAt( ) {
		Song newSong = new Song( 1L, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 true, null, guestList.getFirst( ) );
		when( repository.findByIdOptional( any( ) ) ).thenReturn(
				Optional.of( songList.getFirst( ) ) );
		when( guestService.readById( any( ) ) ).thenReturn( guestList.getFirst( ) );
		when( repository.findAllBySinger( any( ) ) ).thenReturn( new ArrayList<>( ) );
		EntityManager entityManager = Mockito.mock( EntityManager.class );
		when( repository.getEntityManager( ) ).thenReturn( entityManager );
		when( repository.getEntityManager( ).merge( any( ) ) ).thenReturn(
				Optional.of( guestList.getFirst( ) ) );

		Song response = service.update( newSong );

		Assertions.assertTrue( response.getHasBeenSung( ) );
		Assertions.assertTrue(
				response.getCreatedAt( ).isAfter( LocalDateTime.now( ).minusSeconds( 1 ) ) );
		Assertions.assertTrue(
				response.getCreatedAt( ).isBefore( LocalDateTime.now( ).plusSeconds( 1 ) ) );
		verify( repository, times( 1 ) ).findByIdOptional( any( ) );
		verify( guestService, times( 1 ) ).readById( any( ) );
		verify( repository, times( 1 ) ).findAllBySinger( any( ) );
		verify( entityManager, times( 1 ) ).merge( any( Song.class ) );
	}

	@Test
	void update_Fail_IdNull( ) {
		Song newSong = new Song( null, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 true, songList.getFirst( ).getCreatedAt( ),
								 guestList.getFirst( ) );

		boolean wasThrown = false;
		try {
			service.update( newSong );
		} catch ( IllegalArgumentException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( repository, times( 0 ) ).findByIdOptional( any( ) );
		verify( repository, times( 0 ) ).findByIdOptional( any( ) );
	}

	@Test
	void update_Fail_IdZero( ) {
		Song newSong = new Song( 0L, "All Star", "Smash Mouth",
								 "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
								 true, songList.getFirst( ).getCreatedAt( ),
								 guestList.getFirst( ) );

		boolean wasThrown = false;
		try {
			service.update( newSong );
		} catch ( IllegalArgumentException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( repository, times( 0 ) ).findByIdOptional( any( ) );
		verify( repository, times( 0 ) ).findByIdOptional( any( ) );
	}

	@Test
	void nextSong_Success_Simple( ) {
		when( repository.findByHasNotBeenSung( ) ).thenReturn(
														  songList.get( FIRST_UNSUNG_SONG_POSITION ) )
												  .thenReturn( songList.get(
														  FIRST_UNSUNG_SONG_POSITION + 1 ) );
		EntityManager entityManager = Mockito.mock( EntityManager.class );
		when( repository.getEntityManager( ) ).thenReturn( entityManager );
		when( repository.getEntityManager( ).merge( any( ) ) ).thenReturn(
				Optional.of( guestList.getFirst( ) ) );

		Song response = service.nextSong( );

		Assertions.assertFalse( response.getHasBeenSung( ) );
		Assertions.assertTrue( songList.get( FIRST_UNSUNG_SONG_POSITION ).getHasBeenSung( ) );
		Assertions.assertEquals( songList.get( FIRST_UNSUNG_SONG_POSITION + 1 ), response );
		verify( repository, times( 2 ) ).findByHasNotBeenSung( );
		verify( entityManager, times( 1 ) ).merge( any( Song.class ) );
	}

	@Test
	void nextSong_Success_LastSong( ) {
		when( repository.findByHasNotBeenSung( ) ).thenReturn(
				songList.get( songList.size( ) - 1 ) ).thenReturn( null );
		EntityManager entityManager = Mockito.mock( EntityManager.class );
		when( repository.getEntityManager( ) ).thenReturn( entityManager );
		when( repository.getEntityManager( ).merge( any( ) ) ).thenReturn(
				Optional.of( guestList.getFirst( ) ) );

		Song response = service.nextSong( );

		Assertions.assertTrue( songList.get( songList.size( ) - 1 ).getHasBeenSung( ) );
		Assertions.assertNull( response );
		verify( repository, times( 2 ) ).findByHasNotBeenSung( );
		verify( entityManager, times( 1 ) ).merge( any( Song.class ) );
	}

	@Test
	void nextSong_Fail_Empty( ) {
		when( repository.findByHasNotBeenSung( ) ).thenReturn( null );
		EntityManager entityManager = Mockito.mock( EntityManager.class );

		boolean wasThrown = false;
		try {
			service.nextSong( );
		} catch ( IllegalArgumentException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( repository, times( 1 ) ).findByHasNotBeenSung( );
		verify( entityManager, never( ) ).merge( any( Song.class ) );
	}

	@Test
	void delete_Success( ) {
		when( repository.deleteById( any( ) ) ).thenReturn( true );

		service.delete( FIRST_SONG_ID );

		verify( repository, times( 1 ) ).deleteById( any( ) );
	}

	@Test
	void delete_Fail_NotFound( ) {
		when( repository.deleteById( any( ) ) ).thenReturn( false );

		boolean wasThrown = false;
		try {
			service.delete( FIRST_SONG_ID );
		} catch ( EntityNotFoundException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( repository, times( 1 ) ).deleteById( any( ) );
	}

	@Test
	void deleteMySong_Success( ) {
		when( guestService.readByName( any( ) ) ).thenReturn( guestList.getFirst( ) );
		when( repository.findAllBySinger( any( ) ) ).thenReturn( getSongsOfFirstGuest( ) );
		when( repository.deleteById( any( ) ) ).thenReturn( true );

		service.deleteMySong( songList.get( FIRST_UNSUNG_SONG_POSITION ).getName( ),
							  songList.get( FIRST_UNSUNG_SONG_POSITION ).getSinger( ).getName( ) );

		verify( guestService, times( 1 ) ).readByName( any( ) );
		verify( repository, times( 1 ) ).findAllBySinger( any( ) );
		verify( repository, times( 1 ) ).deleteById( any( ) );
	}

	@Test
	void deleteMySong_Fail_SingerNotFound( ) {
		when( guestService.readByName( any( ) ) ).thenThrow(
				new EntityNotFoundException( "Guest not found with name: " ) );

		boolean wasThrown = false;
		try {
			service.deleteMySong( songList.getFirst( ).getName( ), "" );
		} catch ( EntityNotFoundException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( guestService, times( 1 ) ).readByName( any( ) );
		verify( repository, never( ) ).findAllBySinger( any( ) );
		verify( repository, never( ) ).deleteById( any( ) );
	}

	@Test
	void deleteMySong_Fail_SongNotFound( ) {
		when( guestService.readByName( any( ) ) ).thenReturn( guestList.getFirst( ) );
		when( repository.findAllBySinger( any( ) ) ).thenReturn( new ArrayList<>( ) );

		boolean wasThrown = false;
		try {
			service.deleteMySong( songList.getFirst( ).getName( ),
								  songList.getFirst( ).getSinger( ).getName( ) );
		} catch ( EntityNotFoundException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( guestService, times( 1 ) ).readByName( any( ) );
		verify( repository, times( 1 ) ).findAllBySinger( any( ) );
		verify( repository, never( ) ).deleteById( any( ) );
	}

	@Test
	void deleteMySong_Fail_SongAlreadySung( ) {
		when( guestService.readByName( any( ) ) ).thenReturn( guestList.getFirst( ) );
		when( repository.findAllBySinger( any( ) ) ).thenReturn( getSongsOfFirstGuest( ) );

		boolean wasThrown = false;
		try {
			service.deleteMySong( songList.getFirst( ).getName( ),
								  songList.getFirst( ).getSinger( ).getName( ) );
		} catch ( IllegalArgumentException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( guestService, times( 1 ) ).readByName( any( ) );
		verify( repository, times( 1 ) ).findAllBySinger( any( ) );
		verify( repository, never( ) ).deleteById( any( ) );
	}
}