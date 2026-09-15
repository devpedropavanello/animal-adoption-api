package br.com.pedropavanello.animal_adoption_api.animal.presentation.dto;

import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AdoptionStatus;

import java.util.UUID;

public record AnimalResponse(
        UUID id,
        String name,
        String species,
        String breed,
        Integer age,
        AdoptionStatus status
) {
}