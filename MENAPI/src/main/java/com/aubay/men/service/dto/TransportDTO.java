package com.aubay.men.service.dto;

import com.aubay.men.domain.enumeration.TypeMoteur;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.aubay.men.domain.Transport} entity.
 */
@Schema(description = "Référentiel des moyens de transports utilisables")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransportDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String categorie;

    private TypeMoteur typeMoteur;

    @DecimalMin(value = "0")
    private Float feKm;

    private Set<TransportProfilDTO> transportProfils = new HashSet<>();

    private Set<DeplacementProfilDTO> deplacementProfils = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public TypeMoteur getTypeMoteur() {
        return typeMoteur;
    }

    public void setTypeMoteur(TypeMoteur typeMoteur) {
        this.typeMoteur = typeMoteur;
    }

    public Float getFeKm() {
        return feKm;
    }

    public void setFeKm(Float feKm) {
        this.feKm = feKm;
    }

    public Set<TransportProfilDTO> getTransportProfils() {
        return transportProfils;
    }

    public void setTransportProfils(Set<TransportProfilDTO> transportProfils) {
        this.transportProfils = transportProfils;
    }

    public Set<DeplacementProfilDTO> getDeplacementProfils() {
        return deplacementProfils;
    }

    public void setDeplacementProfils(Set<DeplacementProfilDTO> deplacementProfils) {
        this.deplacementProfils = deplacementProfils;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransportDTO)) {
            return false;
        }

        TransportDTO transportDTO = (TransportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransportDTO{" +
            "id=" + getId() +
            ", categorie='" + getCategorie() + "'" +
            ", typeMoteur='" + getTypeMoteur() + "'" +
            ", feKm=" + getFeKm() +
            ", transportProfils=" + getTransportProfils() +
            ", deplacementProfils=" + getDeplacementProfils() +
            "}";
    }
}
