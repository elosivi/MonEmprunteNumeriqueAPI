package com.aubay.men.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.aubay.men.domain.Communication} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommunicationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 200)
    private String libelle;

    @DecimalMin(value = "0")
    private Float fe;

    @Size(min = 1, max = 100)
    private String feUnite;

    private UniteDTO unite;

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

    public Float getFe() {
        return fe;
    }

    public void setFe(Float fe) {
        this.fe = fe;
    }

    public String getFeUnite() {
        return feUnite;
    }

    public void setFeUnite(String feUnite) {
        this.feUnite = feUnite;
    }

    public UniteDTO getUnite() {
        return unite;
    }

    public void setUnite(UniteDTO unite) {
        this.unite = unite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommunicationDTO)) {
            return false;
        }

        CommunicationDTO communicationDTO = (CommunicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, communicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunicationDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", fe=" + getFe() +
            ", feUnite='" + getFeUnite() + "'" +
            ", unite=" + getUnite() +
            "}";
    }
}
