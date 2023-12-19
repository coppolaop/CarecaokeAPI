package br.com.darksun.service;

import br.com.darksun.TestUtils;
import br.com.darksun.model.Guest;
import br.com.darksun.repository.GuestRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.darksun.TestUtils.mockList;
import static br.com.darksun.service.GuestService.HOST_ROLE;
import static org.mockito.Mockito.*;

@QuarkusTest
class GuestServiceTest {

	@Inject
	GuestService    service;
	@InjectMock
	GuestRepository repository;

	private List< Guest > guestList;

	private static final Long   FIRST_GUEST_ID  = 1L;
	private static final String SIMPLE_PASSWORD = "123123";

	@BeforeEach
	void setUp( ) {
		TestUtils utils = new TestUtils( );
		guestList = utils.getGuests( );
	}

	@Test
	public void create_Success_Host( ) {
		Guest newGuest = new Guest( null, "Coppola", SIMPLE_PASSWORD, HOST_ROLE );
		when( repository.findByNameOptional( any( ) ) ).thenReturn( Optional.empty( ) );
		doNothing( ).when( repository ).persist( any( Guest.class ) );

		Guest response = service.create( newGuest );
		response.setId( 1L );

		Assertions.assertTrue( BcryptUtil.matches( SIMPLE_PASSWORD, newGuest.getPassword( ) ) );
		response.setPassword( guestList.getFirst( ).getPassword( ) );
		Assertions.assertEquals( guestList.getFirst( ), response );
		verify( repository, times( 1 ) ).findByNameOptional( any( ) );
		verify( repository, times( 1 ) ).persist( any( Guest.class ) );
	}

	@Test
	public void readAll_Success( ) {
		PanacheQuery query = mockList( guestList );
		when( repository.findAll( ) ).thenReturn( query );

		List< Guest > response = service.readAll( );
		Assertions.assertEquals( guestList, response );
		verify( repository, times( 1 ) ).findAll( );
	}

	@Test
	public void readAll_Success_EmptyList( ) {
		PanacheQuery query = mockList( Collections.emptyList( ) );
		when( repository.findAll( ) ).thenReturn( query );

		List< Guest > response = service.readAll( );
		Assertions.assertTrue( response.isEmpty( ) );
		verify( repository, times( 1 ) ).findAll( );
	}

	@Test
	public void readById_Success( ) {
		when( repository.findByIdOptional( any( ) ) ).thenReturn(
				Optional.of( guestList.getFirst( ) ) );

		Guest response = service.readById( FIRST_GUEST_ID );
		Assertions.assertEquals( guestList.getFirst( ), response );
		verify( repository, times( 1 ) ).findByIdOptional( any( ) );
	}

	@Test
	public void readById_Fail_NotFound( ) {
		when( repository.findByIdOptional( any( ) ) ).thenReturn( Optional.empty( ) );

		boolean wasThrown = false;
		try {
			service.readById( FIRST_GUEST_ID );
		} catch ( EntityNotFoundException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( repository, times( 1 ) ).findByIdOptional( any( ) );
	}

	@Test
	public void getAllInvitations_Success( ) {
		PanacheQuery query = mockList( guestList );
		when( repository.findAll( ) ).thenReturn( query );
		List< String > invitations = guestList.stream( )
											  .map( Guest::getName )
											  .collect( Collectors.toList( ) );

		List< String > response = service.getAllInvitations( );
		Assertions.assertEquals( invitations, response );
		verify( repository, times( 1 ) ).findAll( );
	}

	@Test
	public void update_Success_Host( ) {
		Guest newGuest = new Guest( 1L, "Coppola", SIMPLE_PASSWORD, HOST_ROLE );
		when( repository.findByIdOptional( any( ) ) ).thenReturn(
				Optional.of( guestList.getFirst( ) ) );
		when( repository.findByNameOptional( any( ) ) ).thenReturn(
				Optional.of( guestList.getFirst( ) ) );
		EntityManager entityManager = Mockito.mock( EntityManager.class );
		when( repository.getEntityManager( ) ).thenReturn( entityManager );
		when( repository.getEntityManager( ).merge( any( ) ) ).thenReturn(
				Optional.of( guestList.getFirst( ) ) );
		doNothing( ).when( repository ).persist( any( Guest.class ) );

		Guest response = service.update( newGuest );

		Assertions.assertTrue( BcryptUtil.matches( SIMPLE_PASSWORD, newGuest.getPassword( ) ) );
		response.setPassword( guestList.getFirst( ).getPassword( ) );
		Assertions.assertEquals( guestList.getFirst( ), response );
		verify( repository, times( 1 ) ).findByIdOptional( any( ) );
		verify( repository, times( 1 ) ).findByNameOptional( any( ) );
	}

	@Test
	public void update_Fail_IdNull( ) {
		Guest newGuest = new Guest( null, "Coppola", SIMPLE_PASSWORD, HOST_ROLE );

		boolean wasThrown = false;
		try {
			service.update( newGuest );
		} catch ( IllegalArgumentException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( repository, times( 0 ) ).findByIdOptional( any( ) );
		verify( repository, times( 0 ) ).findByNameOptional( any( ) );
	}

	@Test
	public void update_Fail_IdZero( ) {
		Guest newGuest = new Guest( 0L, "Coppola", SIMPLE_PASSWORD, HOST_ROLE );

		boolean wasThrown = false;
		try {
			service.update( newGuest );
		} catch ( IllegalArgumentException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( repository, times( 0 ) ).findByIdOptional( any( ) );
		verify( repository, times( 0 ) ).findByNameOptional( any( ) );
	}

	@Test
	public void delete_Sucess( ) {
		when( repository.deleteById( any( ) ) ).thenReturn( true );

		service.delete( FIRST_GUEST_ID );

		verify( repository, times( 1 ) ).deleteById( any( ) );
	}

	@Test
	public void delete_Fail( ) {
		when( repository.deleteById( any( ) ) ).thenReturn( false );

		boolean wasThrown = false;
		try {
			service.delete( FIRST_GUEST_ID );
		} catch ( EntityNotFoundException ex ) {
			wasThrown = true;
		}

		Assertions.assertTrue( wasThrown );
		verify( repository, times( 1 ) ).deleteById( any( ) );
	}
}