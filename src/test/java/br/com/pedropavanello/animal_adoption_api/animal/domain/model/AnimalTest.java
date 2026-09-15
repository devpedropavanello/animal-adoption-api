package br.com.pedropavanello.animal_adoption_api.animal.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnimalTest {

    @Test
    void shouldCreateAnimalWithGeneratedIdAndAvailableStatus() {
        Animal animal = Animal.create(
                " Luna ",
                " Cachorro ",
                " Vira-lata ",
                3
        );

        assertAll(
                () -> assertNotNull(animal.getId()),
                () -> assertNotNull(animal.getId().value()),
                () -> assertEquals("Luna", animal.getName()),
                () -> assertEquals("Cachorro", animal.getSpecies()),
                () -> assertEquals("Vira-lata", animal.getBreed()),
                () -> assertEquals(3, animal.getAge()),
                () -> assertEquals(
                        AdoptionStatus.AVAILABLE,
                        animal.getStatus()
                )
        );
    }

    @Test
    void shouldNormalizeBlankBreedToNull() {
        Animal animal = Animal.create(
                "Luna",
                "Cachorro",
                " ",
                3
        );

        assertNull(animal.getBreed());
    }

    @Test
    void shouldRejectNullOrBlankName() {
        assertAll(
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Animal.create(null, "Cachorro", null, 3)
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Animal.create("", "Cachorro", null, 3)
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Animal.create(" ", "Cachorro", null, 3)
                )
        );
    }

    @Test
    void shouldRejectNullOrBlankSpecies() {
        assertAll(
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Animal.create("Luna", null, null, 3)
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Animal.create("Luna", "", null, 3)
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Animal.create("Luna", " ", null, 3)
                )
        );
    }

    @Test
    void shouldRejectNullOrNegativeAge() {
        assertAll(
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Animal.create(
                                "Luna",
                                "Cachorro",
                                null,
                                null
                        )
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Animal.create(
                                "Luna",
                                "Cachorro",
                                null,
                                -1
                        )
                )
        );
    }

    @Test
    void shouldRestoreAnimalWithExistingState() {
        AnimalId id = AnimalId.generate();

        Animal animal = Animal.restore(
                id,
                "Luna",
                "Cachorro",
                "Vira-lata",
                3,
                AdoptionStatus.ADOPTED
        );

        assertAll(
                () -> assertEquals(id, animal.getId()),
                () -> assertEquals("Luna", animal.getName()),
                () -> assertEquals(AdoptionStatus.ADOPTED, animal.getStatus())
        );
    }

    @Test
    void shouldRejectRestoreWithoutIdOrStatus() {
        AnimalId id = AnimalId.generate();

        assertAll(
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Animal.restore(
                                null,
                                "Luna",
                                "Cachorro",
                                null,
                                3,
                                AdoptionStatus.AVAILABLE
                        )
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Animal.restore(
                                id,
                                "Luna",
                                "Cachorro",
                                null,
                                3,
                                null
                        )
                )
        );
    }

    @Test
    void shouldUpdateAnimalDetails() {
        Animal animal = createValidAnimal();
        AnimalId originalId = animal.getId();

        animal.updateDetails(
                " Thor ",
                " Gato ",
                " Siamês ",
                4
        );

        assertAll(
                () -> assertEquals(originalId, animal.getId()),
                () -> assertEquals("Thor", animal.getName()),
                () -> assertEquals("Gato", animal.getSpecies()),
                () -> assertEquals("Siamês", animal.getBreed()),
                () -> assertEquals(4, animal.getAge()),
                () -> assertEquals(
                        AdoptionStatus.AVAILABLE,
                        animal.getStatus()
                )
        );
    }

    @Test
    void shouldKeepCurrentDataWhenUpdateIsInvalid() {
        Animal animal = createValidAnimal();

        assertThrows(
                IllegalArgumentException.class,
                () -> animal.updateDetails(
                        "Thor",
                        "Gato",
                        "Siamês",
                        -1
                )
        );

        assertAll(
                () -> assertEquals("Luna", animal.getName()),
                () -> assertEquals("Cachorro", animal.getSpecies()),
                () -> assertEquals("Vira-lata", animal.getBreed()),
                () -> assertEquals(3, animal.getAge())
        );
    }

    @Test
    void shouldMarkAnimalAsAdopted() {
        Animal animal = createValidAnimal();

        animal.markAsAdopted();

        assertEquals(AdoptionStatus.ADOPTED, animal.getStatus());
    }

    @Test
    void shouldMarkAdoptedAnimalAsAvailable() {
        Animal animal = createValidAnimal();

        animal.markAsAdopted();
        animal.markAsAvailable();

        assertEquals(AdoptionStatus.AVAILABLE, animal.getStatus());
    }

    private Animal createValidAnimal() {
        return Animal.create(
                "Luna",
                "Cachorro",
                "Vira-lata",
                3
        );
    }
}