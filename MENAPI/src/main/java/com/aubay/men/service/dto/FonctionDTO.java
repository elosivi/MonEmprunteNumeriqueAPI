package com.aubay.men.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.aubay.men.domain.Fonction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FonctionDTO implements Serializable {

    private Long id;

    @Size(min = 3, max = 100)
    private String libelle;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FonctionDTO)) {
            return false;
        }

        FonctionDTO fonctionDTO = (FonctionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fonctionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FonctionDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
