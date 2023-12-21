package br.com.darksun.service;

import br.com.darksun.model.Guest;
import br.com.darksun.repository.GuestRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class GuestService {
	public static final String HOST_ROLE  = "host";
	public static final String GUEST_ROLE = "guest";

	@Inject
	GuestRepository repository;

	public Guest create( Guest guest ) {
		guest.setId( null );
		applyBusinessRules( guest );
		repository.persist( guest );
		return guest;
	}

	public Guest invite( String name ) {
		Guest guest = new Guest( null, name, "", "" );
		applyBusinessRules( guest );
		repository.persist( guest );
		return guest;
	}

	public List< Guest > readAll( ) {
		return repository.findAll( ).list( );
	}

	public Guest readById( Long id ) {
		return repository.findByIdOptional( id )
						 .orElseThrow( ( ) -> new EntityNotFoundException(
								 "Guest not found with ID: " + id ) );
	}

	protected Guest readByName( String name ) {
		return repository.findByNameOptional( name )
						 .orElseThrow( ( ) -> new EntityNotFoundException(
								 "Guest not found with name: " + name ) );
	}

	public List< String > getAllInvitations( ) {
		return readAll( ).stream( ).map( Guest::getName ).collect( Collectors.toList( ) );
	}

	public Guest update( Guest guest ) {
		if ( guest.getId( ) == null || guest.getId( ) == 0 ) {
			throw new IllegalArgumentException( "Guest with no Id" );
		}
		readById( guest.getId( ) );
		applyBusinessRules( guest );
		repository.getEntityManager( ).merge( guest );
		return guest;
	}

	public void delete( Long id ) {
		boolean wasDeleted = repository.deleteById( id );
		if ( !wasDeleted ) {
			throw new EntityNotFoundException( "Guest not found with ID: " + id );
		}
	}

	private void applyBusinessRules( Guest guest ) {
		Optional< Guest > similarGuest = repository.findByNameOptional( guest.getName( ) );
		if ( similarGuest.isPresent( ) && !similarGuest.get( ).getId( ).equals( guest.getId( ) ) ) {
			throw new IllegalArgumentException( "Not possible to have guests with same name" );
		}
		guest.setPassword( BcryptUtil.bcryptHash( guest.getPassword( ) ) );
		guest.setRole( HOST_ROLE.equalsIgnoreCase( guest.getRole( ) ) ? HOST_ROLE : GUEST_ROLE );
	}
}
