package br.com.darksun.service;

import br.com.darksun.model.Music;
import br.com.darksun.repository.MusicRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@ApplicationScoped
public class MusicService {

	@Inject
	MusicRepository repository;

	@Inject
	GuestService guestService;

	public Music create( Music music ) {
		music.setId( null );
		music.setHasBeenSung( false );
		applyBusinessRules( music );
		repository.persist( music );
		return music;
	}

	public List< Music > readAll( ) {
		return repository.findAll( ).list( );
	}

	public Music readById( Long id ) {
		return repository.findByIdOptional( id )
						 .orElseThrow( ( ) -> new EntityNotFoundException(
								 "Music not found with ID: " + id ) );
	}

	public Music update( Music music ) {
		if ( music.getId( ) == null || music.getId( ) == 0 ) {
			throw new IllegalArgumentException( "Music with no Id" );
		}
		readById( music.getId( ) );
		applyBusinessRules( music );
		repository.getEntityManager( ).merge( music );
		return music;
	}

	public void delete( Long id ) {
		boolean wasDeleted = repository.deleteById( id );
		if ( !wasDeleted ) {
			throw new EntityNotFoundException( "Music not found with ID: " + id );
		}
	}

	private void applyBusinessRules( Music music ) {
		if ( music.getSinger( ) == null || music.getSinger( ).getId( ) == null ) {
			throw new IllegalArgumentException( "This music has no singer" );
		}
		guestService.readById( music.getSinger( ).getId( ) );
	}
}
