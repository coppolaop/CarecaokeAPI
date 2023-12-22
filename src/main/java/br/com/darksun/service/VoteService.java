package br.com.darksun.service;

import br.com.darksun.model.Vote;
import br.com.darksun.repository.VoteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

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
