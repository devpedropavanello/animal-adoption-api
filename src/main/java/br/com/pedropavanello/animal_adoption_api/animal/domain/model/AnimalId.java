package br.com.pedropavanello.animal_adoption_api.animal.domain.model;

import java.util.UUID;

public record AnimalId(UUID value) {

    public AnimalId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "O identificador do animal não pode ser nulo"
            );
        }
    }

    public static AnimalId generate() {
        return new AnimalId(UUID.randomUUID());
    }
}
