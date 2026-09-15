package br.com.pedropavanello.animal_adoption_api.animal.infrastructure.persistence;

import br.com.pedropavanello.animal_adoption_api.animal.application.command.CreateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.application.command.UpdateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.application.exception.AnimalNotFoundException;
import br.com.pedropavanello.animal_adoption_api.animal.application.service.AnimalService;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AdoptionStatus;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AnimalId;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class AnimalPersistenceIntegrationTest {

    private final AnimalService animalService;
    private final SpringDataAnimalRepository springDataRepository;
    private final EntityManager entityManager;

    @Autowired
    AnimalPersistenceIntegrationTest(
            AnimalService animalService,
            SpringDataAnimalRepository springDataRepository,
            EntityManager entityManager
    ) {
        this.animalService = animalService;
        this.springDataRepository = springDataRepository;
        this.entityManager = entityManager;
    }

    @Test
    void shouldPersistAndFindAnimal() {
        Animal createdAnimal = animalService.create(
                createCommand("Luna")
        );

        flushAndClear();

        Animal foundAnimal = animalService.findById(
                createdAnimal.getId()
        );

        assertAll(
                () -> assertNotNull(createdAnimal.getId()),
                () -> assertEquals(
                        createdAnimal.getId(),
                        foundAnimal.getId()
                ),
                () -> assertEquals("Luna", foundAnimal.getName()),
                () -> assertEquals(
                        "Cachorro",
                        foundAnimal.getSpecies()
                ),
                () -> assertEquals(
                        "Vira-lata",
                        foundAnimal.getBreed()
                ),
                () -> assertEquals(3, foundAnimal.getAge()),
                () -> assertEquals(
                        AdoptionStatus.AVAILABLE,
                        foundAnimal.getStatus()
                )
        );
    }

    @Test
    void shouldUpdatePersistedAnimal() {
        Animal createdAnimal = animalService.create(
                createCommand("Luna")
        );

        flushAndClear();

        Animal updatedAnimal = animalService.update(
                createdAnimal.getId(),
                new UpdateAnimalCommand(
                        "Milo",
                        "Gato",
                        "Siamês",
                        2
                )
        );

        flushAndClear();

        Animal foundAnimal = animalService.findById(
                updatedAnimal.getId()
        );

        assertAll(
                () -> assertEquals(
                        createdAnimal.getId(),
                        foundAnimal.getId()
                ),
                () -> assertEquals("Milo", foundAnimal.getName()),
                () -> assertEquals("Gato", foundAnimal.getSpecies()),
                () -> assertEquals(
                        "Siamês",
                        foundAnimal.getBreed()
                ),
                () -> assertEquals(2, foundAnimal.getAge()),
                () -> assertEquals(
                        AdoptionStatus.AVAILABLE,
                        foundAnimal.getStatus()
                )
        );
    }

    @Test
    void shouldListPersistedAnimals() {
        Animal firstAnimal = animalService.create(
                createCommand("Luna")
        );
        Animal secondAnimal = animalService.create(
                createCommand("Thor")
        );

        flushAndClear();

        List<Animal> animals = animalService.findAll();

        assertAll(
                () -> assertTrue(
                        containsAnimal(animals, firstAnimal.getId())
                ),
                () -> assertTrue(
                        containsAnimal(animals, secondAnimal.getId())
                )
        );
    }

    @Test
    void shouldDeletePersistedAnimal() {
        Animal createdAnimal = animalService.create(
                createCommand("Luna")
        );

        flushAndClear();

        AnimalId id = createdAnimal.getId();

        animalService.delete(id);

        flushAndClear();

        assertThrows(
                AnimalNotFoundException.class,
                () -> animalService.findById(id)
        );
    }

    private CreateAnimalCommand createCommand(String name) {
        return new CreateAnimalCommand(
                name,
                "Cachorro",
                "Vira-lata",
                3
        );
    }

    private boolean containsAnimal(
            List<Animal> animals,
            AnimalId expectedId
    ) {
        return animals.stream()
                .map(Animal::getId)
                .anyMatch(expectedId::equals);
    }

    private void flushAndClear() {
        springDataRepository.flush();
        entityManager.clear();
    }
}