package br.com.pedropavanello.animal_adoption_api.animal.infrastructure.persistence;

import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AnimalId;
import br.com.pedropavanello.animal_adoption_api.animal.domain.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnimalRepositoryAdapter implements AnimalRepository {

    private final SpringDataAnimalRepository springDataRepository;
    private final AnimalPersistenceMapper mapper;

    @Override
    public Animal save(Animal animal) {
        AnimalJpaEntity entity = mapper.toEntity(animal);
        AnimalJpaEntity savedEntity = springDataRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Animal> findById(AnimalId id) {
        return springDataRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<Animal> findAll() {
        return springDataRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void delete(Animal animal) {
        springDataRepository.deleteById(animal.getId().value());
    }
}