package com.aubay.men.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.aubay.men.domain.DeplacementProfil} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeplacementProfilDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer nbDeplacement;

    @Min(value = 0)
    private Integer kmPresta;

    private Set<ProfilDTO> profils = new HashSet<>();

    private Set<PrestationDTO> prestations = new HashSet<>();

    private Set<TransportDTO> transports = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbDeplacement() {
        return nbDeplacement;
    }

    public void setNbDeplacement(Integer nbDeplacement) {
        this.nbDeplacement = nbDeplacement;
    }

    public Integer getKmPresta() {
        return kmPresta;
    }

    public void setKmPresta(Integer kmPresta) {
        this.kmPresta = kmPresta;
    }

    public Set<ProfilDTO> getProfils() {
        return profils;
    }

    public void setProfils(Set<ProfilDTO> profils) {
        this.profils = profils;
    }

    public Set<PrestationDTO> getPrestations() {
        return prestations;
    }

    public void setPrestations(Set<PrestationDTO> prestations) {
        this.prestations = prestations;
    }

    public Set<TransportDTO> getTransports() {
        return transports;
    }

    public void setTransports(Set<TransportDTO> transports) {
        this.transports = transports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeplacementProfilDTO)) {
            return false;
        }

        DeplacementProfilDTO deplacementProfilDTO = (DeplacementProfilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deplacementProfilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeplacementProfilDTO{" +
            "id=" + getId() +
            ", nbDeplacement=" + getNbDeplacement() +
            ", kmPresta=" + getKmPresta() +
            ", profils=" + getProfils() +
            ", prestations=" + getPrestations() +
            ", transports=" + getTransports() +
            "}";
    }
}
