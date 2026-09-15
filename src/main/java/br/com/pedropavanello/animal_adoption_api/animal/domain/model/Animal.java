package br.com.pedropavanello.animal_adoption_api.animal.domain.model;

import lombok.Getter;

@Getter
public final class Animal {

    private final AnimalId id;
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private AdoptionStatus status;

    private Animal(
            AnimalId id,
            String name,
            String species,
            String breed,
            Integer age,
            AdoptionStatus status
    ) {
        this.id = requireId(id);
        this.status = requireStatus(status);
        updateDetails(name, species, breed, age);
    }

    public static Animal create(
            String name,
            String species,
            String breed,
            Integer age
    ) {
        return new Animal(
                AnimalId.generate(),
                name,
                species,
                breed,
                age,
                AdoptionStatus.AVAILABLE
        );
    }

    public static Animal restore(
            AnimalId id,
            String name,
            String species,
            String breed,
            Integer age,
            AdoptionStatus status
    ) {
        return new Animal(id, name, species, breed, age, status);
    }

    public void updateDetails(
            String name,
            String species,
            String breed,
            Integer age
    ) {
        String validatedName = requireNonBlank(
                name,
                "O nome do animal não pode ser nulo ou vazio"
        );
        String validatedSpecies = requireNonBlank(
                species,
                "A espécie do animal não pode ser nula ou vazia"
        );
        Integer validatedAge = requireValidAge(age);
        String normalizedBreed = normalizeBreed(breed);

        this.name = validatedName;
        this.species = validatedSpecies;
        this.breed = normalizedBreed;
        this.age = validatedAge;
    }

    public void markAsAdopted() {
        this.status = AdoptionStatus.ADOPTED;
    }

    public void markAsAvailable() {
        this.status = AdoptionStatus.AVAILABLE;
    }

    private static AnimalId requireId(AnimalId id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "O identificador do animal não pode ser nulo"
            );
        }

        return id;
    }

    private static AdoptionStatus requireStatus(AdoptionStatus status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "O status de adoção não pode ser nulo"
            );
        }

        return status;
    }

    private static String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.strip();
    }

    private static Integer requireValidAge(Integer age) {
        if (age == null) {
            throw new IllegalArgumentException(
                    "A idade do animal não pode ser nula"
            );
        }

        if (age < 0) {
            throw new IllegalArgumentException(
                    "A idade do animal não pode ser negativa"
            );
        }

        return age;
    }

    private static String normalizeBreed(String breed) {
        if (breed == null || breed.isBlank()) {
            return null;
        }

        return breed.strip();
    }
}