package br.com.pedropavanello.animal_adoption_api.animal.infrastructure.persistence;

import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AdoptionStatus;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AnimalId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;

class AnimalPersistenceMapperTest {

    private final AnimalPersistenceMapper mapper =
            new AnimalPersistenceMapper();

    @Test
    void shouldMapDomainAnimalToJpaEntity() {
        UUID id = UUID.randomUUID();
        Animal animal = Animal.restore(
                new AnimalId(id),
                "Luna",
                "Cachorro",
                "Vira-lata",
                3,
                AdoptionStatus.ADOPTED
        );

        AnimalJpaEntity entity = mapper.toEntity(animal);

        assertAll(
                () -> assertEquals(id, entity.getId()),
                () -> assertEquals("Luna", entity.getName()),
                () -> assertEquals("Cachorro", entity.getSpecies()),
                () -> assertEquals("Vira-lata", entity.getBreed()),
                () -> assertEquals(3, entity.getAge()),
                () -> assertEquals(
                        AdoptionStatus.ADOPTED,
                        entity.getStatus()
                )
        );
    }

    @Test
    void shouldMapJpaEntityToDomainAnimal() {
        UUID id = UUID.randomUUID();
        AnimalJpaEntity entity = new AnimalJpaEntity(
                id,
                "Milo",
                "Gato",
                null,
                2,
                AdoptionStatus.AVAILABLE
        );

        Animal animal = mapper.toDomain(entity);

        assertAll(
                () -> assertEquals(id, animal.getId().value()),
                () -> assertEquals("Milo", animal.getName()),
                () -> assertEquals("Gato", animal.getSpecies()),
                () -> assertNull(animal.getBreed()),
                () -> assertEquals(2, animal.getAge()),
                () -> assertEquals(
                        AdoptionStatus.AVAILABLE,
                        animal.getStatus()
                )
        );
    }

    @Test
    void shouldRejectNullValues() {
        assertAll(
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> mapper.toEntity(null)
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> mapper.toDomain(null)
                )
        );
    }
}