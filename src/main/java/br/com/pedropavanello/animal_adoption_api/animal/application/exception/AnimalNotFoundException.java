package br.com.pedropavanello.animal_adoption_api.animal.application.exception;

import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AnimalId;

public final class AnimalNotFoundException extends RuntimeException {

    public AnimalNotFoundException(AnimalId animalId) {
        super(
                "Animal não encontrado com o identificador: "
                        + animalId.value()
        );
    }
}