package br.com.pedropavanello.animal_adoption_api.animal.application.service;

import br.com.pedropavanello.animal_adoption_api.animal.application.command.CreateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.application.command.UpdateAdoptionStatusCommand;
import br.com.pedropavanello.animal_adoption_api.animal.application.command.UpdateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.application.exception.AnimalNotFoundException;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AnimalId;
import br.com.pedropavanello.animal_adoption_api.animal.domain.repository.AnimalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        if (animalRepository == null) {
            throw new IllegalArgumentException(
                    "O repositório de animais não pode ser nulo"
            );
        }

        this.animalRepository = animalRepository;
    }

    @Transactional
    public Animal create(CreateAnimalCommand command) {
        requireCreateCommand(command);

        Animal animal = Animal.create(
                command.name(),
                command.species(),
                command.breed(),
                command.age()
        );

        return animalRepository.save(animal);
    }

    public Animal findById(AnimalId id) {
        requireId(id);

        return animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException(id));
    }

    public List<Animal> findAll() {
        return animalRepository.findAll();
    }

    @Transactional
    public Animal update(AnimalId id, UpdateAnimalCommand command) {
        requireUpdateCommand(command);

        Animal animal = findById(id);

        animal.updateDetails(
                command.name(),
                command.species(),
                command.breed(),
                command.age()
        );

        return animalRepository.save(animal);
    }

    @Transactional
    public Animal updateAdoptionStatus(
            AnimalId id,
            UpdateAdoptionStatusCommand command
    ) {
        requireUpdateAdoptionStatusCommand(command);

        Animal animal = findById(id);

        switch (command.status()) {
            case AVAILABLE -> animal.markAsAvailable();
            case ADOPTED -> animal.markAsAdopted();
        }

        return animalRepository.save(animal);
    }

    @Transactional
    public void delete(AnimalId id) {
        Animal animal = findById(id);

        animalRepository.delete(animal);
    }

    private static void requireId(AnimalId id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "O identificador do animal não pode ser nulo"
            );
        }
    }

    private static void requireCreateCommand(CreateAnimalCommand command) {
        if (command == null) {
            throw new IllegalArgumentException(
                    "O comando de cadastro não pode ser nulo"
            );
        }
    }

    private static void requireUpdateCommand(UpdateAnimalCommand command) {
        if (command == null) {
            throw new IllegalArgumentException(
                    "O comando de atualização não pode ser nulo"
            );
        }
    }

    private static void requireUpdateAdoptionStatusCommand(
            UpdateAdoptionStatusCommand command
    ) {
        if (command == null) {
            throw new IllegalArgumentException(
                    "O comando de atualização do status de adoção não pode ser nulo"
            );
        }

        if (command.status() == null) {
            throw new IllegalArgumentException(
                    "O status de adoção não pode ser nulo"
            );
        }
    }
}