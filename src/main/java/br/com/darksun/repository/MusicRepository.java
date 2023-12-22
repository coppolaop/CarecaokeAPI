package br.com.darksun.repository;

import br.com.darksun.model.Music;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class MusicRepository implements PanacheRepository< Music > {
	public List< Music > findAllByHasNotBeenSung( ) {
		return this.find( "hasBeenSung = ?1 order by createdAt", false ).list( );
	}
}
