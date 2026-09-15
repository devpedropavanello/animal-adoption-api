package br.com.pedropavanello.animal_adoption_api.animal.presentation.mapper;

import br.com.pedropavanello.animal_adoption_api.animal.application.command.CreateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.application.command.UpdateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AdoptionStatus;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.dto.AnimalResponse;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.dto.CreateAnimalRequest;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.dto.UpdateAnimalRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnimalPresentationMapperTest {

    private final AnimalPresentationMapper mapper =
            new AnimalPresentationMapper();

    @Test
    void shouldMapCreateRequestToCommand() {
        CreateAnimalRequest request = new CreateAnimalRequest(
                "Luna",
                "Cachorro",
                "Vira-lata",
                3
        );

        CreateAnimalCommand command = mapper.toCreateCommand(request);

        assertAll(
                () -> assertEquals(request.name(), command.name()),
                () -> assertEquals(request.species(), command.species()),
                () -> assertEquals(request.breed(), command.breed()),
                () -> assertEquals(request.age(), command.age())
        );
    }

    @Test
    void shouldMapUpdateRequestToCommand() {
        UpdateAnimalRequest request = new UpdateAnimalRequest(
                "Luna",
                "Cachorro",
                "Labrador",
                4
        );

        UpdateAnimalCommand command = mapper.toUpdateCommand(request);

        assertAll(
                () -> assertEquals(request.name(), command.name()),
                () -> assertEquals(request.species(), command.species()),
                () -> assertEquals(request.breed(), command.breed()),
                () -> assertEquals(request.age(), command.age())
        );
    }

    @Test
    void shouldMapAnimalToResponse() {
        Animal animal = Animal.create(
                "Luna",
                "Cachorro",
                "Vira-lata",
                3
        );

        AnimalResponse response = mapper.toResponse(animal);

        assertAll(
                () -> assertEquals(animal.getId().value(), response.id()),
                () -> assertEquals(animal.getName(), response.name()),
                () -> assertEquals(animal.getSpecies(), response.species()),
                () -> assertEquals(animal.getBreed(), response.breed()),
                () -> assertEquals(animal.getAge(), response.age()),
                () -> assertEquals(AdoptionStatus.AVAILABLE, response.status())
        );
    }
}