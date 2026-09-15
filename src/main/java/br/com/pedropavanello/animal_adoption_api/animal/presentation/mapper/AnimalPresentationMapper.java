package br.com.pedropavanello.animal_adoption_api.animal.presentation.mapper;

import br.com.pedropavanello.animal_adoption_api.animal.application.command.CreateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.application.command.UpdateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.dto.AnimalResponse;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.dto.CreateAnimalRequest;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.dto.UpdateAnimalRequest;
import org.springframework.stereotype.Component;

@Component
public class AnimalPresentationMapper {

    public CreateAnimalCommand toCreateCommand(CreateAnimalRequest request) {
        return new CreateAnimalCommand(
                request.name(),
                request.species(),
                request.breed(),
                request.age()
        );
    }

    public UpdateAnimalCommand toUpdateCommand(UpdateAnimalRequest request) {
        return new UpdateAnimalCommand(
                request.name(),
                request.species(),
                request.breed(),
                request.age()
        );
    }

    public AnimalResponse toResponse(Animal animal) {
        return new AnimalResponse(
                animal.getId().value(),
                animal.getName(),
                animal.getSpecies(),
                animal.getBreed(),
                animal.getAge(),
                animal.getStatus()
        );
    }
}