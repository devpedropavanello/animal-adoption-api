package br.com.pedropavanello.animal_adoption_api.animal.application.command;

public record UpdateAnimalCommand(
        String name,
        String species,
        String breed,
        Integer age
) {
}