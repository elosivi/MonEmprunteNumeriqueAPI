package com.aubay.men.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.aubay.men.domain.Profil} entity.
 */
@Schema(description = "TEntité profil")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfilDTO implements Serializable {

    private Long id;

    @Size(min = 3, max = 50)
    private String nom;

    @Size(min = 3, max = 50)
    private String prenom;

    @NotNull
    private String email;

    private FonctionDTO fonction;

    private Set<TransportProfilDTO> transportProfils = new HashSet<>();

    private Set<DeplacementProfilDTO> deplacementProfils = new HashSet<>();

    private Set<MaterielProfilDTO> materielProfils = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FonctionDTO getFonction() {
        return fonction;
    }

    public void setFonction(FonctionDTO fonction) {
        this.fonction = fonction;
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
        if (!(o instanceof ProfilDTO)) {
            return false;
        }

        ProfilDTO profilDTO = (ProfilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfilDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", fonction=" + getFonction() +
            ", transportProfils=" + getTransportProfils() +
            ", deplacementProfils=" + getDeplacementProfils() +
            ", materielProfils=" + getMaterielProfils() +
            "}";
    }
}
