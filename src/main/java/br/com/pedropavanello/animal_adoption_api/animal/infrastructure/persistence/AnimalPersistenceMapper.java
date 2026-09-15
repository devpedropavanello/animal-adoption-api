package br.com.pedropavanello.animal_adoption_api.animal.infrastructure.persistence;

import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AnimalId;
import org.springframework.stereotype.Component;

@Component
public final class AnimalPersistenceMapper {

    public AnimalJpaEntity toEntity(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException(
                    "O animal não pode ser nulo"
            );
        }

        return new AnimalJpaEntity(
                animal.getId().value(),
                animal.getName(),
                animal.getSpecies(),
                animal.getBreed(),
                animal.getAge(),
                animal.getStatus()
        );
    }

    public Animal toDomain(AnimalJpaEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException(
                    "A entidade JPA do animal não pode ser nula"
            );
        }

        return Animal.restore(
                new AnimalId(entity.getId()),
                entity.getName(),
                entity.getSpecies(),
                entity.getBreed(),
                entity.getAge(),
                entity.getStatus()
        );
    }
}