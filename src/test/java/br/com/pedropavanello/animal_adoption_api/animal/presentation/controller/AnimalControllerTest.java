package br.com.pedropavanello.animal_adoption_api.animal.presentation.controller;

import br.com.pedropavanello.animal_adoption_api.animal.application.command.CreateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.application.command.UpdateAnimalCommand;
import br.com.pedropavanello.animal_adoption_api.animal.application.exception.AnimalNotFoundException;
import br.com.pedropavanello.animal_adoption_api.animal.application.service.AnimalService;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.Animal;
import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AnimalId;
import br.com.pedropavanello.animal_adoption_api.animal.presentation.mapper.AnimalPresentationMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnimalController.class)
@Import(AnimalPresentationMapper.class)
class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnimalService animalService;

    @Test
    void shouldCreateAnimal() throws Exception {
        CreateAnimalCommand command = new CreateAnimalCommand(
                "Luna",
                "Cachorro",
                "Vira-lata",
                3
        );

        Animal animal = Animal.create(
                command.name(),
                command.species(),
                command.breed(),
                command.age()
        );

        when(animalService.create(command)).thenReturn(animal);

        mockMvc.perform(
                        post("/api/v1/animals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Luna",
                                          "species": "Cachorro",
                                          "breed": "Vira-lata",
                                          "age": 3
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        HttpHeaders.LOCATION,
                        endsWith(
                                "/api/v1/animals/"
                                        + animal.getId().value()
                        )
                ))
                .andExpect(jsonPath("$.id").value(
                        animal.getId().value().toString()
                ))
                .andExpect(jsonPath("$.name").value("Luna"))
                .andExpect(jsonPath("$.species").value("Cachorro"))
                .andExpect(jsonPath("$.breed").value("Vira-lata"))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));

        verify(animalService).create(command);
    }

    @Test
    void shouldListAnimals() throws Exception {
        Animal firstAnimal = Animal.create(
                "Luna",
                "Cachorro",
                "Vira-lata",
                3
        );

        Animal secondAnimal = Animal.create(
                "Mia",
                "Gato",
                "Siamês",
                2
        );

        when(animalService.findAll()).thenReturn(
                List.of(firstAnimal, secondAnimal)
        );

        mockMvc.perform(get("/api/v1/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(
                        firstAnimal.getId().value().toString()
                ))
                .andExpect(jsonPath("$[0].name").value("Luna"))
                .andExpect(jsonPath("$[1].id").value(
                        secondAnimal.getId().value().toString()
                ))
                .andExpect(jsonPath("$[1].name").value("Mia"));

        verify(animalService).findAll();
    }

    @Test
    void shouldFindAnimalById() throws Exception {
        Animal animal = Animal.create(
                "Luna",
                "Cachorro",
                "Vira-lata",
                3
        );

        AnimalId animalId = animal.getId();

        when(animalService.findById(animalId)).thenReturn(animal);

        mockMvc.perform(
                        get(
                                "/api/v1/animals/{id}",
                                animalId.value()
                        )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(
                        animalId.value().toString()
                ))
                .andExpect(jsonPath("$.name").value("Luna"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));

        verify(animalService).findById(animalId);
    }

    @Test
    void shouldUpdateAnimal() throws Exception {
        Animal animal = Animal.create(
                "Luna",
                "Cachorro",
                "Vira-lata",
                3
        );

        animal.updateDetails(
                "Luna Atualizada",
                "Cachorro",
                "Labrador",
                4
        );

        AnimalId animalId = animal.getId();

        UpdateAnimalCommand command = new UpdateAnimalCommand(
                "Luna Atualizada",
                "Cachorro",
                "Labrador",
                4
        );

        when(animalService.update(animalId, command))
                .thenReturn(animal);

        mockMvc.perform(
                        put(
                                "/api/v1/animals/{id}",
                                animalId.value()
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Luna Atualizada",
                                          "species": "Cachorro",
                                          "breed": "Labrador",
                                          "age": 4
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(
                        animalId.value().toString()
                ))
                .andExpect(jsonPath("$.name").value(
                        "Luna Atualizada"
                ))
                .andExpect(jsonPath("$.breed").value("Labrador"))
                .andExpect(jsonPath("$.age").value(4));

        verify(animalService).update(animalId, command);
    }

    @Test
    void shouldDeleteAnimal() throws Exception {
        AnimalId animalId = new AnimalId(UUID.randomUUID());

        mockMvc.perform(
                        delete(
                                "/api/v1/animals/{id}",
                                animalId.value()
                        )
                )
                .andExpect(status().isNoContent());

        verify(animalService).delete(animalId);
    }

    @Test
    void shouldReturnBadRequestWhenCreateRequestIsInvalid()
            throws Exception {
        mockMvc.perform(
                        post("/api/v1/animals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": " ",
                                          "species": "",
                                          "breed": "Vira-lata",
                                          "age": -1
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(
                        "Um ou mais campos estão inválidos"
                ))
                .andExpect(jsonPath("$.path").value(
                        "/api/v1/animals"
                ))
                .andExpect(jsonPath("$.fieldErrors.name").value(
                        "O nome do animal é obrigatório"
                ))
                .andExpect(jsonPath("$.fieldErrors.species").value(
                        "A espécie do animal é obrigatória"
                ))
                .andExpect(jsonPath("$.fieldErrors.age").value(
                        "A idade do animal não pode ser negativa"
                ));

        verify(
                animalService,
                never()
        ).create(any(CreateAnimalCommand.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()
            throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(
                        put("/api/v1/animals/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Luna",
                                          "species": "Cachorro",
                                          "breed": "Vira-lata"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.age").value(
                        "A idade do animal é obrigatória"
                ));

        verify(
                animalService,
                never()
        ).update(
                any(AnimalId.class),
                any(UpdateAnimalCommand.class)
        );
    }

    @Test
    void shouldReturnBadRequestWhenIdIsNotValidUuid()
            throws Exception {
        mockMvc.perform(
                        get("/api/v1/animals/{id}", "invalid-id")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(
                        "O identificador do animal deve ser um UUID válido"
                ))
                .andExpect(jsonPath("$.path").value(
                        "/api/v1/animals/invalid-id"
                ));

        verify(
                animalService,
                never()
        ).findById(any(AnimalId.class));
    }

    @Test
    void shouldReturnNotFoundWhenAnimalDoesNotExist()
            throws Exception {
        AnimalId animalId = new AnimalId(UUID.randomUUID());

        when(animalService.findById(animalId))
                .thenThrow(new AnimalNotFoundException(animalId));

        mockMvc.perform(
                        get(
                                "/api/v1/animals/{id}",
                                animalId.value()
                        )
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value(
                        "Animal não encontrado com o identificador: "
                                + animalId.value()
                ))
                .andExpect(jsonPath("$.path").value(
                        "/api/v1/animals/" + animalId.value()
                ));

        verify(animalService).findById(animalId);
    }

    @Test
    void shouldReturnBadRequestWhenJsonIsMalformed()
            throws Exception {
        mockMvc.perform(
                        post("/api/v1/animals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Luna"
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(
                        "O corpo da requisição está inválido ou malformado"
                ))
                .andExpect(jsonPath("$.path").value(
                        "/api/v1/animals"
                ));

        verify(
                animalService,
                never()
        ).create(any(CreateAnimalCommand.class));
    }
}