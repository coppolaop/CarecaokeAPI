package br.com.darksun.repository;

import br.com.darksun.model.Guest;
import br.com.darksun.model.Song;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SongRepository implements PanacheRepository< Song > {
	public List< Song > findAllByHasNotBeenSung( ) {
		return this.find( "hasBeenSung = ?1 order by createdAt", false ).list( );
	}

	public List< Song > findAllBySinger( Guest singer ) {
		return this.find( "singer", singer ).list( );
	}

	public Song findByHasNotBeenSung( ) {
		return this.find( "hasBeenSung = ?1 order by createdAt", false ).firstResult( );
	}
}
