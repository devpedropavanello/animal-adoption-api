package br.com.pedropavanello.animal_adoption_api.animal.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataAnimalRepository
        extends JpaRepository<AnimalJpaEntity, UUID> {
}