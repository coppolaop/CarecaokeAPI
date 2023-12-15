package br.com.darksun;

import br.com.darksun.model.Guest;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import lombok.Getter;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static br.com.darksun.service.GuestService.GUEST_ROLE;
import static br.com.darksun.service.GuestService.HOST_ROLE;
import static org.mockito.Mockito.when;

@Getter
public class TestUtils {

	private List< Guest > guests;

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
	}

	public static PanacheQuery mockList( List list ) {
		PanacheQuery query = Mockito.mock( PanacheQuery.class );
		when( query.page( Mockito.any( ) ) ).thenReturn( query );
		when( query.list( ) ).thenReturn( list );
		return query;
	}
}
