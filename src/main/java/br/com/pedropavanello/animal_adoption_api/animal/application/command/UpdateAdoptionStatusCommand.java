package br.com.pedropavanello.animal_adoption_api.animal.application.command;

import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AdoptionStatus;

public record UpdateAdoptionStatusCommand(
        AdoptionStatus status
) {
}