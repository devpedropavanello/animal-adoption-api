package br.com.pedropavanello.animal_adoption_api.animal.presentation.dto;

import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AdoptionStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAdoptionStatusRequest(
        @NotNull(message = "O status de adoção é obrigatório")
        AdoptionStatus status
) {
}