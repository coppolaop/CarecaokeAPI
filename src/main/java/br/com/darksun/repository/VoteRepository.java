package br.com.darksun.repository;

import br.com.darksun.model.Guest;
import br.com.darksun.model.Song;
import br.com.darksun.model.Vote;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class VoteRepository implements PanacheRepository< Vote > {
	public Optional< Vote > findByItsForAndWhoVotesOptional( Song itsFor, Guest whoVotes ) {
		return this.find( "itsFor = ?1 AND whoVotes = ?2", itsFor, whoVotes )
				   .firstResultOptional( );
	}
}
