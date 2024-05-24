package com.aubay.men.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.aubay.men.domain.Materiel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterielDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String libelle;

    @DecimalMin(value = "0")
    private Float feVeille;

    private Set<MaterielProfilDTO> materielProfils = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Float getFeVeille() {
        return feVeille;
    }

    public void setFeVeille(Float feVeille) {
        this.feVeille = feVeille;
    }

    public Set<MaterielProfilDTO> getMaterielProfils() {
        return materielProfils;
    }

    public void setMaterielProfils(Set<MaterielProfilDTO> materielProfils) {
        this.materielProfils = materielProfils;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterielDTO)) {
            return false;
        }

        MaterielDTO materielDTO = (MaterielDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, materielDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterielDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", feVeille=" + getFeVeille() +
            ", materielProfils=" + getMaterielProfils() +
            "}";
    }
}
