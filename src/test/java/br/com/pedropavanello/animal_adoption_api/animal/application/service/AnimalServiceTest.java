package br.com.pedropavanello.animal_adoption_api.animal.application.service;

import br.com.pedropavanello.animal_adoption_api.animal.application.command.CreateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.application.command.UpdateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.application.exception.AnimalNotFoundException;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AdoptionStatus;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AnimalId;
import br.com.pedropavanello.animal_adoption_api.animal.domain.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    private AnimalService animalService;

    @BeforeEach
    void setUp() {
        animalService = new AnimalService(animalRepository);
    }

    @Test
    void shouldRejectNullRepository() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AnimalService(null)
        );
    }

    @Test
    void shouldCreateAndSaveAnimal() {
        CreateAnimalCommand command = new CreateAnimalCommand(
                "Luna",
                "Cachorro",
                "Vira-lata",
                3
        );

        when(animalRepository.save(any(Animal.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Animal animal = animalService.create(command);

        assertAll(
                () -> assertNotNull(animal.getId()),
                () -> assertEquals("Luna", animal.getName()),
                () -> assertEquals("Cachorro", animal.getSpecies()),
                () -> assertEquals("Vira-lata", animal.getBreed()),
                () -> assertEquals(3, animal.getAge()),
                () -> assertEquals(
                        AdoptionStatus.AVAILABLE,
                        animal.getStatus()
                )
        );

        verify(animalRepository).save(animal);
    }

    @Test
    void shouldRejectNullCreateCommand() {
        assertThrows(
                IllegalArgumentException.class,
                () -> animalService.create(null)
        );

        verifyNoInteractions(animalRepository);
    }

    @Test
    void shouldFindAnimalById() {
        Animal animal = createStoredAnimal();
        AnimalId id = animal.getId();

        when(animalRepository.findById(id))
                .thenReturn(Optional.of(animal));

        Animal result = animalService.findById(id);

        assertSame(animal, result);
        verify(animalRepository).findById(id);
    }

    @Test
    void shouldThrowWhenAnimalDoesNotExist() {
        AnimalId id = new AnimalId(UUID.randomUUID());

        when(animalRepository.findById(id))
                .thenReturn(Optional.empty());

        AnimalNotFoundException exception = assertThrows(
                AnimalNotFoundException.class,
                () -> animalService.findById(id)
        );

        assertEquals(
                "Animal não encontrado com o identificador: " + id.value(),
                exception.getMessage()
        );
    }

    @Test
    void shouldListAnimals() {
        List<Animal> animals = List.of(
                createStoredAnimal(),
                Animal.restore(
                        new AnimalId(UUID.randomUUID()),
                        "Milo",
                        "Gato",
                        "Siamês",
                        2,
                        AdoptionStatus.AVAILABLE
                )
        );

        when(animalRepository.findAll()).thenReturn(animals);

        List<Animal> result = animalService.findAll();

        assertEquals(animals, result);
        verify(animalRepository).findAll();
    }

    @Test
    void shouldUpdateAndSaveAnimal() {
        Animal animal = createStoredAnimal();
        AnimalId id = animal.getId();
        UpdateAnimalCommand command = new UpdateAnimalCommand(
                "Thor",
                "Gato",
                "Siamês",
                4
        );

        when(animalRepository.findById(id))
                .thenReturn(Optional.of(animal));
        when(animalRepository.save(animal))
                .thenReturn(animal);

        Animal result = animalService.update(id, command);

        assertAll(
                () -> assertSame(animal, result),
                () -> assertEquals(id, result.getId()),
                () -> assertEquals("Thor", result.getName()),
                () -> assertEquals("Gato", result.getSpecies()),
                () -> assertEquals("Siamês", result.getBreed()),
                () -> assertEquals(4, result.getAge()),
                () -> assertEquals(
                        AdoptionStatus.AVAILABLE,
                        result.getStatus()
                )
        );

        verify(animalRepository).findById(id);
        verify(animalRepository).save(animal);
    }

    @Test
    void shouldNotSaveWhenAnimalToUpdateDoesNotExist() {
        AnimalId id = new AnimalId(UUID.randomUUID());
        UpdateAnimalCommand command = new UpdateAnimalCommand(
                "Thor",
                "Gato",
                null,
                4
        );

        when(animalRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AnimalNotFoundException.class,
                () -> animalService.update(id, command)
        );

        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    void shouldDeleteExistingAnimal() {
        Animal animal = createStoredAnimal();
        AnimalId id = animal.getId();

        when(animalRepository.findById(id))
                .thenReturn(Optional.of(animal));

        animalService.delete(id);

        verify(animalRepository).findById(id);
        verify(animalRepository).delete(animal);
    }

    @Test
    void shouldNotDeleteWhenAnimalDoesNotExist() {
        AnimalId id = new AnimalId(UUID.randomUUID());

        when(animalRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                AnimalNotFoundException.class,
                () -> animalService.delete(id)
        );

        verify(animalRepository, never()).delete(any(Animal.class));
    }

    private Animal createStoredAnimal() {
        return Animal.restore(
                new AnimalId(UUID.randomUUID()),
                "Luna",
                "Cachorro",
                "Vira-lata",
                3,
                AdoptionStatus.AVAILABLE
        );
    }
}