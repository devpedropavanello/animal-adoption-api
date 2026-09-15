package br.com.pedropavanello.animal_adoption_api.animal.presentation.controller;

import br.com.pedropavanello.animal_adoption_api.animal.application.service.AnimalService;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AnimalId;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.dto.AnimalResponse;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.dto.CreateAnimalRequest;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.dto.UpdateAdoptionStatusRequest;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.dto.UpdateAnimalRequest;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.mapper.AnimalPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/animals")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;
    private final AnimalPresentationMapper mapper;

    @PostMapping
    public ResponseEntity<AnimalResponse> create(
            @Valid @RequestBody CreateAnimalRequest request
    ) {
        Animal animal = animalService.create(
                mapper.toCreateCommand(request)
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(animal.getId().value())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(mapper.toResponse(animal));
    }

    @GetMapping
    public ResponseEntity<List<AnimalResponse>> findAll() {
        List<AnimalResponse> response = animalService.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> findById(
            @PathVariable UUID id
    ) {
        Animal animal = animalService.findById(new AnimalId(id));

        return ResponseEntity.ok(mapper.toResponse(animal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAnimalRequest request
    ) {
        Animal animal = animalService.update(
                new AnimalId(id),
                mapper.toUpdateCommand(request)
        );

        return ResponseEntity.ok(mapper.toResponse(animal));
    }

    @PatchMapping("/{id}/adoption-status")
    public ResponseEntity<AnimalResponse> updateAdoptionStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAdoptionStatusRequest request
    ) {
        Animal animal = animalService.updateAdoptionStatus(
                new AnimalId(id),
                mapper.toUpdateAdoptionStatusCommand(request)
        );

        return ResponseEntity.ok(mapper.toResponse(animal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        animalService.delete(new AnimalId(id));

        return ResponseEntity.noContent().build();
    }
}