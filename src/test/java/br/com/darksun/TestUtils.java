package br.com.darksun;

import br.com.darksun.model.Guest;
import br.com.darksun.model.Song;
import br.com.darksun.model.Vote;
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
	private List< Vote >  votes;
	private int           firstUnsong;

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
		Song song3 = new Song( 3L, "Deixa vida me levar", "Zeca Pagodinho",
							   "https://www.youtube.com/watch?v=4UcMQcVHnS4", true,
							   LocalDateTime.of( 2024, 3, 16, 14, 31, 1 ), guest3 );
		Song song4 = new Song( 4L, "Holiday In Cambodia", "Dead Kennedys",
							   "https://www.youtube.com/watch?v=cfVN4tTbB-w", true,
							   LocalDateTime.of( 2024, 3, 16, 14, 31, 2 ), guest2 );
		Song song5 = new Song( 5L, "Mina do Condomínio", "Seu Jorge",
							   "https://www.youtube.com/watch?v=u7M0mqzpPMM", true,
							   LocalDateTime.of( 2024, 3, 16, 14, 31, 3 ), guest5 );
		Song song6 = new Song( 6L, "Vasco da Gama", "Los Hermanos (?)",
							   "https://www.youtube.com/watch?v=r0IlmwctKOs&ab_channel=AerofoxKaraok%C3%AA",
							   true, LocalDateTime.of( 2024, 3, 16, 14, 31, 4 ), guest4 );

		songs.add( song1 );
		songs.add( song2 );
		songs.add( song3 );
		songs.add( song4 );
		songs.add( song5 );
		songs.add( song6 );

		firstUnsong = songs.size( );
		Song unsungSong1 = new Song( firstUnsong + 0L, "Wonderwall", "Oasis",
									 "https://www.youtube.com/watch?v=R_EXyGWI9rU", false,
									 LocalDateTime.of( 2024, 3, 16, 14, 33 ), guest1 );
		Song unsungSong2 = new Song( firstUnsong + 1L, "Vasco da Gama", "Los Hermanos (?)",
									 "https://www.youtube.com/watch?v=r0IlmwctKOs&ab_channel=AerofoxKaraok%C3%AA",
									 false, LocalDateTime.of( 2024, 3, 16, 14, 32 ), guest2 );
		Song unsungSong3 = new Song( firstUnsong + 2L, "I Really Want to Stay at Your House",
									 "Rosa Walton", "https://www.youtube.com/watch?v=xu4eioDS_OM",
									 false, LocalDateTime.of( 2024, 3, 16, 14, 34 ), guest1 );

		songs.add( unsungSong1 );
		songs.add( unsungSong2 );
		songs.add( unsungSong3 );

		votes = new ArrayList<>( );
		Vote vote1  = new Vote( 1L, Short.valueOf( "3" ), song1, guest2 );
		Vote vote2  = new Vote( 2L, Short.valueOf( "3" ), song1, guest3 );
		Vote vote3  = new Vote( 3L, Short.valueOf( "3" ), song1, guest4 );
		Vote vote4  = new Vote( 4L, Short.valueOf( "3" ), song1, guest5 );
		Vote vote5  = new Vote( 5L, Short.valueOf( "2" ), song2, guest1 );
		Vote vote6  = new Vote( 6L, Short.valueOf( "1" ), song3, guest1 );
		Vote vote7  = new Vote( 7L, Short.valueOf( "3" ), song3, guest2 );
		Vote vote8  = new Vote( 8L, Short.valueOf( "3" ), song4, guest1 );
		Vote vote9  = new Vote( 9L, Short.valueOf( "2" ), song4, guest5 );
		Vote vote10 = new Vote( 10L, Short.valueOf( "3" ), song5, guest1 );
		Vote vote11 = new Vote( 9L, Short.valueOf( "2" ), song5, guest2 );
		Vote vote12 = new Vote( 9L, Short.valueOf( "1" ), song5, guest3 );
		Vote vote13 = new Vote( 9L, Short.valueOf( "2" ), song5, guest4 );
		Vote vote14 = new Vote( 5L, Short.valueOf( "3" ), song6, guest4 );
		Vote vote15 = new Vote( 6L, Short.valueOf( "3" ), song6, guest5 );
		votes.add( vote1 );
		votes.add( vote2 );
		votes.add( vote3 );
		votes.add( vote4 );
		votes.add( vote5 );
		votes.add( vote6 );
		votes.add( vote7 );
		votes.add( vote8 );
		votes.add( vote9 );
		votes.add( vote10 );
		votes.add( vote11 );
		votes.add( vote12 );
		votes.add( vote13 );
		votes.add( vote14 );
		votes.add( vote15 );
	}

	public static PanacheQuery mockList( List list ) {
		PanacheQuery query = Mockito.mock( PanacheQuery.class );
		when( query.page( Mockito.any( ) ) ).thenReturn( query );
		when( query.list( ) ).thenReturn( list );
		return query;
	}
}
