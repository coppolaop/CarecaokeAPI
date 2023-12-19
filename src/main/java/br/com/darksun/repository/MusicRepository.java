package br.com.darksun.repository;

import br.com.darksun.model.Music;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MusicRepository implements PanacheRepository< Music > {
}
