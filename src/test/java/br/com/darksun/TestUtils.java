package br.com.darksun;

import br.com.darksun.model.Guest;
import br.com.darksun.model.Song;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import lombok.Getter;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static br.com.darksun.service.GuestService.GUEST_ROLE;
import static br.com.darksun.service.GuestService.HOST_ROLE;
import static org.mockito.Mockito.when;

@Getter
public class TestUtils {

	private List< Guest > guests;
	private List< Song >  songs;

	public TestUtils( ) {
		guests = new ArrayList<>( );
		Guest guest1 = new Guest( 1L, "Coppola", BcryptUtil.bcryptHash( "123123" ), HOST_ROLE );
		Guest guest2 = new Guest( 2L, "Abel", BcryptUtil.bcryptHash( "" ), GUEST_ROLE );
		Guest guest3 = new Guest( 3L, "Igor", BcryptUtil.bcryptHash( "" ), GUEST_ROLE );
		Guest guest4 = new Guest( 4L, "Phillipe", BcryptUtil.bcryptHash( "" ), GUEST_ROLE );
		Guest guest5 = new Guest( 5L, "Narrita", BcryptUtil.bcryptHash( "" ), GUEST_ROLE );
		guests.add( guest1 );
		guests.add( guest2 );
		guests.add( guest3 );
		guests.add( guest4 );
		guests.add( guest5 );

		songs = new ArrayList<>( );
		Song song1 = new Song( 1L, "All Star", "Smash Mouth",
							   "https://www.youtube.com/watch?v=ABOYo7ioQJo&ab_channel=SingKing",
							   true, LocalDateTime.of( 2024, 3, 16, 14, 30 ), guest1 );
		Song song2 = new Song( 2L, "Song 2", "Blur", "https://www.youtube.com/watch?v=9GkcLEH7clA",
							   true, LocalDateTime.of( 2024, 3, 16, 14, 31 ), guest2 );
		Song song3 = new Song( 3L, "Wonderwall", "Oasis",
							   "https://www.youtube.com/watch?v=R_EXyGWI9rU", false,
							   LocalDateTime.of( 2024, 3, 16, 14, 33 ), guest1 );
		Song song4 = new Song( 4L, "Vasco da Gama", "Los Hermanos (?)",
							   "https://www.youtube.com/watch?v=r0IlmwctKOs&ab_channel=AerofoxKaraok%C3%AA",
							   false, LocalDateTime.of( 2024, 3, 16, 14, 32 ), guest4 );
		Song song5 = new Song( 5L, "I Really Want to Stay at Your House", "Rosa Walton",
							   "https://www.youtube.com/watch?v=xu4eioDS_OM", false,
							   LocalDateTime.of( 2024, 3, 16, 14, 34 ), guest1 );
		songs.add( song1 );
		songs.add( song2 );
		songs.add( song3 );
		songs.add( song4 );
		songs.add( song5 );
	}

	public static PanacheQuery mockList( List list ) {
		PanacheQuery query = Mockito.mock( PanacheQuery.class );
		when( query.page( Mockito.any( ) ) ).thenReturn( query );
		when( query.list( ) ).thenReturn( list );
		return query;
	}
}
