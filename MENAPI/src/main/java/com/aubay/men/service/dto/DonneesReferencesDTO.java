package com.aubay.men.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.aubay.men.domain.DonneesReferences} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DonneesReferencesDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String libelle;

    private Integer donneeReference;

    private UniteDTO unite;

    private UniteDTO temporalite;

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

    public Integer getDonneeReference() {
        return donneeReference;
    }

    public void setDonneeReference(Integer donneeReference) {
        this.donneeReference = donneeReference;
    }

    public UniteDTO getUnite() {
        return unite;
    }

    public void setUnite(UniteDTO unite) {
        this.unite = unite;
    }

    public UniteDTO getTemporalite() {
        return temporalite;
    }

    public void setTemporalite(UniteDTO temporalite) {
        this.temporalite = temporalite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DonneesReferencesDTO)) {
            return false;
        }

        DonneesReferencesDTO donneesReferencesDTO = (DonneesReferencesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, donneesReferencesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DonneesReferencesDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", donneeReference=" + getDonneeReference() +
            ", unite=" + getUnite() +
            ", temporalite=" + getTemporalite() +
            "}";
    }
}
