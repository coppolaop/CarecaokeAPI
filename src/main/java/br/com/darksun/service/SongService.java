package br.com.darksun.service;

import br.com.darksun.model.Guest;
import br.com.darksun.model.Song;
import br.com.darksun.repository.SongRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SongService {

	@Inject
	SongRepository repository;

	@Inject
	GuestService guestService;

	public Song create( Song song ) {
		song.setId( null );
		song.setHasBeenSung( false );
		song.setCreatedAt( LocalDateTime.now( ) );
		applyBusinessRules( song );
		repository.persist( song );
		return song;
	}

	public List< Song > readAll( ) {
		return repository.findAll( ).list( );
	}

	public Song readById( Long id ) {
		return repository.findByIdOptional( id )
						 .orElseThrow( ( ) -> new EntityNotFoundException(
								 "Song not found with ID: " + id ) );
	}

	public List< Song > readMySongs( String singerName ) {
		Guest singer = guestService.readByName( singerName );
		return repository.findAllBySinger( singer );
	}

	public List< String > getNextSongs( ) {
		List< Song >   nextSongs = repository.findAllByHasNotBeenSung( );
		List< String > response  = new ArrayList<>( );
		nextSongs.forEach( song -> response.add(
				new StringBuilder( ).append( song.getSinger( ).getName( ) )
									.append( " - " )
									.append( song.getName( ) )
									.toString( ) ) );
		return response;
	}

	public Song update( Song song ) {
		if ( song.getId( ) == null || song.getId( ) == 0 ) {
			throw new IllegalArgumentException( "Song with no Id" );
		}
		readById( song.getId( ) );
		if ( song.getCreatedAt( ) == null ) {
			song.setCreatedAt( LocalDateTime.now( ) );
		}
		applyBusinessRules( song );
		repository.getEntityManager( ).merge( song );
		return song;
	}

	public void delete( Long id ) {
		boolean wasDeleted = repository.deleteById( id );
		if ( !wasDeleted ) {
			throw new EntityNotFoundException( "Song not found with ID: " + id );
		}
	}

	private void applyBusinessRules( Song song ) {
		if ( song.getSinger( ) == null || song.getSinger( ).getId( ) == null ) {
			throw new IllegalArgumentException( "This song has no singer" );
		}
		guestService.readById( song.getSinger( ).getId( ) );
	}
}
