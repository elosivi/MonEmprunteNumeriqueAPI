package com.aubay.men.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.aubay.men.domain.MaterielProfil} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterielProfilDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer dureeHebdo;

    private Boolean estNeuf;

    private Set<ProfilDTO> profils = new HashSet<>();

    private Set<PrestationDTO> prestations = new HashSet<>();

    private Set<MaterielDTO> materiels = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDureeHebdo() {
        return dureeHebdo;
    }

    public void setDureeHebdo(Integer dureeHebdo) {
        this.dureeHebdo = dureeHebdo;
    }

    public Boolean getEstNeuf() {
        return estNeuf;
    }

    public void setEstNeuf(Boolean estNeuf) {
        this.estNeuf = estNeuf;
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

    public Set<MaterielDTO> getMateriels() {
        return materiels;
    }

    public void setMateriels(Set<MaterielDTO> materiels) {
        this.materiels = materiels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterielProfilDTO)) {
            return false;
        }

        MaterielProfilDTO materielProfilDTO = (MaterielProfilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, materielProfilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterielProfilDTO{" +
            "id=" + getId() +
            ", dureeHebdo=" + getDureeHebdo() +
            ", estNeuf='" + getEstNeuf() + "'" +
            ", profils=" + getProfils() +
            ", prestations=" + getPrestations() +
            ", materiels=" + getMateriels() +
            "}";
    }
}
