package br.com.pedropavanello.animal_adoption_api.animal.domain.repository;

import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AnimalId;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository {

    Animal save(Animal animal);

    Optional<Animal> findById(AnimalId id);

    List<Animal> findAll();

    void delete(Animal animal);
}
