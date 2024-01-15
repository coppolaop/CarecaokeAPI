package br.com.darksun.service;

import br.com.darksun.model.Vote;
import br.com.darksun.repository.VoteRepository;
import br.com.darksun.util.Utils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

import java.util.*;

@ApplicationScoped
public class VoteService {

	@Inject
	VoteRepository repository;

	@Inject
	GuestService guestService;

	@Inject
	SongService songService;

	public Vote create( Vote vote, String whoVote ) {
		vote.setId( null );
		applyBusinessRules( vote, whoVote );
		repository.persist( vote );
		return vote;
	}

	public List< Vote > readAll( ) {
		return repository.findAll( ).list( );
	}

	public Vote readById( Long id ) {
		return repository.findByIdOptional( id )
						 .orElseThrow( ( ) -> new EntityNotFoundException(
								 "Vote not found with ID: " + id ) );
	}

	public List< String > results( ) {
		List< String >         results         = new ArrayList<>( );
		Map< String, Integer > musicAndItVotes = new HashMap<>( );

		readAll( ).forEach( vote -> {
			String  singerAndSong = Utils.formatSingerAndSong( vote );
			Integer score         = musicAndItVotes.getOrDefault( singerAndSong, 0 );
			score += vote.getScore( );
			musicAndItVotes.put( singerAndSong, score );
		} );

		musicAndItVotes.entrySet( )
					   .stream( )
					   .sorted( Map.Entry.comparingByValue( Collections.reverseOrder( ) ) )
					   .forEach( result -> {
						   results.add( new StringBuilder( result.getKey( ) ).append( " = " )
																			 .append(
																					 result.getValue( ) )
																			 .append( " points" )
																			 .toString( ) );
					   } );

		return results;
	}

	public void delete( Long id ) {
		boolean wasDeleted = repository.deleteById( id );
		if ( !wasDeleted ) {
			throw new EntityNotFoundException( "Vote not found with ID: " + id );
		}
	}

	private void applyBusinessRules( Vote vote, String whoVotesName ) {
		vote.setWhoVotes( guestService.readByName( whoVotesName ) );
		vote.setItsFor( songService.readById( vote.getItsFor( ).getId( ) ) );
		if ( vote.getItsFor( ).getSinger( ).getName( ).equalsIgnoreCase( whoVotesName ) ) {
			throw new IllegalArgumentException( whoVotesName + ", you cannot vote on your song" );
		}
		if ( vote.getScore( ) == null || vote.getScore( ) > 3 || vote.getScore( ) < 1 ) {
			throw new IllegalArgumentException( "Invalid score" );
		}
		Optional< Vote > similarVote = repository.findByItsForAndWhoVotesOptional(
				vote.getItsFor( ), vote.getWhoVotes( ) );
		if ( similarVote.isPresent( ) && !similarVote.get( ).getId( ).equals( vote.getId( ) ) ) {
			throw new IllegalArgumentException( "You can vote in a song just once" );
		}
	}
}
