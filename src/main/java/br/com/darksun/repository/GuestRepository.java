package br.com.darksun.repository;

import br.com.darksun.model.Guest;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class GuestRepository implements PanacheRepository< Guest > {
	public Optional< Guest > findByNameOptional( String name ) {
		return this.find( "name", name ).firstResultOptional( );
	}
}
