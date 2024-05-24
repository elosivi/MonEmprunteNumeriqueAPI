package com.aubay.men.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.aubay.men.domain.Unite} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniteDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String libelle;

    private Boolean estTemporelle;

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

    public Boolean getEstTemporelle() {
        return estTemporelle;
    }

    public void setEstTemporelle(Boolean estTemporelle) {
        this.estTemporelle = estTemporelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniteDTO)) {
            return false;
        }

        UniteDTO uniteDTO = (UniteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uniteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniteDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", estTemporelle='" + getEstTemporelle() + "'" +
            "}";
    }
}
