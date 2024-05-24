package com.aubay.men.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.aubay.men.domain.TransportProfil} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransportProfilDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer nbHebdo;

    @Min(value = 0)
    private Integer kmHebdo;

    private Set<ProfilDTO> profils = new HashSet<>();

    private Set<PrestationDTO> prestations = new HashSet<>();

    private Set<TransportDTO> transports = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbHebdo() {
        return nbHebdo;
    }

    public void setNbHebdo(Integer nbHebdo) {
        this.nbHebdo = nbHebdo;
    }

    public Integer getKmHebdo() {
        return kmHebdo;
    }

    public void setKmHebdo(Integer kmHebdo) {
        this.kmHebdo = kmHebdo;
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
        if (!(o instanceof TransportProfilDTO)) {
            return false;
        }

        TransportProfilDTO transportProfilDTO = (TransportProfilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transportProfilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransportProfilDTO{" +
            "id=" + getId() +
            ", nbHebdo=" + getNbHebdo() +
            ", kmHebdo=" + getKmHebdo() +
            ", profils=" + getProfils() +
            ", prestations=" + getPrestations() +
            ", transports=" + getTransports() +
            "}";
    }
}
