package br.com.pedropavanello.animal_adoption_api.animal.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateAnimalRequest(

        @NotBlank(message = "O nome do animal é obrigatório")
        String name,

        @NotBlank(message = "A espécie do animal é obrigatória")
        String species,

        String breed,

        @NotNull(message = "A idade do animal é obrigatória")
        @PositiveOrZero(message = "A idade do animal não pode ser negativa")
        Integer age
) {
}