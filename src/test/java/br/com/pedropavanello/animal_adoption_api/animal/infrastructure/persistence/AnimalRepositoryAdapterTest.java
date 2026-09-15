package br.com.pedropavanello.animal_adoption_api.animal.infrastructure.persistence;

import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AdoptionStatus;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AnimalId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnimalRepositoryAdapterTest {

    @Mock
    private SpringDataAnimalRepository springDataRepository;

    @Mock
    private AnimalPersistenceMapper mapper;

    private AnimalRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new AnimalRepositoryAdapter(
                springDataRepository,
                mapper
        );
    }

    @Test
    void shouldSaveAnimal() {
        Animal animal = createAnimal("Luna");
        AnimalJpaEntity entity = createEntity(
                animal.getId().value(),
                "Luna"
        );
        Animal savedAnimal = createAnimal("Luna salva");

        when(mapper.toEntity(animal)).thenReturn(entity);
        when(springDataRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(savedAnimal);

        Animal result = adapter.save(animal);

        assertSame(savedAnimal, result);
        verify(mapper).toEntity(animal);
        verify(springDataRepository).save(entity);
        verify(mapper).toDomain(entity);
    }

    @Test
    void shouldFindAnimalById() {
        AnimalId id = new AnimalId(UUID.randomUUID());
        AnimalJpaEntity entity = createEntity(id.value(), "Luna");
        Animal animal = Animal.restore(
                id,
                "Luna",
                "Cachorro",
                "Vira-lata",
                3,
                AdoptionStatus.AVAILABLE
        );

        when(springDataRepository.findById(id.value()))
                .thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(animal);

        Optional<Animal> result = adapter.findById(id);

        assertTrue(result.isPresent());
        assertSame(animal, result.orElseThrow());
        verify(springDataRepository).findById(id.value());
        verify(mapper).toDomain(entity);
    }

    @Test
    void shouldReturnEmptyWhenAnimalDoesNotExist() {
        AnimalId id = new AnimalId(UUID.randomUUID());

        when(springDataRepository.findById(id.value()))
                .thenReturn(Optional.empty());

        Optional<Animal> result = adapter.findById(id);

        assertTrue(result.isEmpty());
        verify(springDataRepository).findById(id.value());
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldListAnimals() {
        AnimalJpaEntity firstEntity = createEntity(
                UUID.randomUUID(),
                "Luna"
        );
        AnimalJpaEntity secondEntity = createEntity(
                UUID.randomUUID(),
                "Milo"
        );
        Animal firstAnimal = createAnimal("Luna");
        Animal secondAnimal = createAnimal("Milo");

        when(springDataRepository.findAll())
                .thenReturn(List.of(firstEntity, secondEntity));
        when(mapper.toDomain(firstEntity)).thenReturn(firstAnimal);
        when(mapper.toDomain(secondEntity)).thenReturn(secondAnimal);

        List<Animal> result = adapter.findAll();

        assertEquals(List.of(firstAnimal, secondAnimal), result);
        verify(springDataRepository).findAll();
        verify(mapper).toDomain(firstEntity);
        verify(mapper).toDomain(secondEntity);
    }

    @Test
    void shouldDeleteAnimalById() {
        Animal animal = createAnimal("Luna");

        adapter.delete(animal);

        verify(springDataRepository)
                .deleteById(animal.getId().value());
        verifyNoInteractions(mapper);
    }

    private Animal createAnimal(String name) {
        return Animal.restore(
                new AnimalId(UUID.randomUUID()),
                name,
                "Cachorro",
                "Vira-lata",
                3,
                AdoptionStatus.AVAILABLE
        );
    }

    private AnimalJpaEntity createEntity(UUID id, String name) {
        return new AnimalJpaEntity(
                id,
                name,
                "Cachorro",
                "Vira-lata",
                3,
                AdoptionStatus.AVAILABLE
        );
    }
}