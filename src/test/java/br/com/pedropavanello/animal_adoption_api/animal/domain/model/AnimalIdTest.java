package br.com.pedropavanello.animal_adoption_api.animal.domain.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnimalIdTest {

    @Test
    void shouldCreateAnimalIdWithProvidedUuid() {
        UUID value = UUID.randomUUID();

        AnimalId animalId = new AnimalId(value);

        assertEquals(value, animalId.value());
    }

    @Test
    void shouldGenerateAnimalIdWithUuid() {
        AnimalId animalId = AnimalId.generate();

        assertNotNull(animalId.value());
    }

    @Test
    void shouldNotCreateAnimalIdWithNullValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AnimalId(null)
        );

        assertEquals(
                "O identificador do animal não pode ser nulo",
                exception.getMessage()
        );
    }
}