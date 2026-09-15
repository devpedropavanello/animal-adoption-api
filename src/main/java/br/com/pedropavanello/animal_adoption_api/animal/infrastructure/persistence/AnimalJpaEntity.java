package br.com.pedropavanello.animal_adoption_api.animal.infrastructure.persistence;

import br.com.pedropavanello.animal_adoption_api.animal.domain.model.AdoptionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(name = "animals")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class AnimalJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    private String breed;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdoptionStatus status;
}