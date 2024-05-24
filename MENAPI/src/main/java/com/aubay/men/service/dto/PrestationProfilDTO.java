package com.aubay.men.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.aubay.men.domain.PrestationProfil} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrestationProfilDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer nbMoisPresta;

    @Min(value = 0)
    private Integer nbSemCongesEstime;

    @Min(value = 0)
    private Integer dureeHebdo;

    @Min(value = 0)
    private Integer dureeTeletravail;

    @DecimalMin(value = "0")
    private Float dureeReuAudio;

    @DecimalMin(value = "0")
    private Float dureeReuVisio;

    @Min(value = 0)
    private Integer nbMailsSansPJ;

    @Min(value = 0)
    private Integer nbMailsAvecPJ;

    private Boolean veillePause;

    private Boolean veilleSoir;

    private Boolean veilleWeekend;

    @Min(value = 0)
    private Integer nbTerminaux;

    @Min(value = 0)
    private Integer nbDeplacements;

    @DecimalMin(value = "0")
    private Float ecMensuelle;

    @DecimalMin(value = "0")
    private Float ecTotalePreta;

    @DecimalMin(value = "0")
    private Float ecTransportMensuel;

    @DecimalMin(value = "0")
    private Float ecFabMateriel;

    @DecimalMin(value = "0")
    private Float ecUtilMaterielMensuel;

    @DecimalMin(value = "0")
    private Float ecCommMensuel;

    private ProfilDTO profil;

    private Set<PrestationDTO> prestations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbMoisPresta() {
        return nbMoisPresta;
    }

    public void setNbMoisPresta(Integer nbMoisPresta) {
        this.nbMoisPresta = nbMoisPresta;
    }

    public Integer getNbSemCongesEstime() {
        return nbSemCongesEstime;
    }

    public void setNbSemCongesEstime(Integer nbSemCongesEstime) {
        this.nbSemCongesEstime = nbSemCongesEstime;
    }

    public Integer getDureeHebdo() {
        return dureeHebdo;
    }

    public void setDureeHebdo(Integer dureeHebdo) {
        this.dureeHebdo = dureeHebdo;
    }

    public Integer getDureeTeletravail() {
        return dureeTeletravail;
    }

    public void setDureeTeletravail(Integer dureeTeletravail) {
        this.dureeTeletravail = dureeTeletravail;
    }

    public Float getDureeReuAudio() {
        return dureeReuAudio;
    }

    public void setDureeReuAudio(Float dureeReuAudio) {
        this.dureeReuAudio = dureeReuAudio;
    }

    public Float getDureeReuVisio() {
        return dureeReuVisio;
    }

    public void setDureeReuVisio(Float dureeReuVisio) {
        this.dureeReuVisio = dureeReuVisio;
    }

    public Integer getNbMailsSansPJ() {
        return nbMailsSansPJ;
    }

    public void setNbMailsSansPJ(Integer nbMailsSansPJ) {
        this.nbMailsSansPJ = nbMailsSansPJ;
    }

    public Integer getNbMailsAvecPJ() {
        return nbMailsAvecPJ;
    }

    public void setNbMailsAvecPJ(Integer nbMailsAvecPJ) {
        this.nbMailsAvecPJ = nbMailsAvecPJ;
    }

    public Boolean getVeillePause() {
        return veillePause;
    }

    public void setVeillePause(Boolean veillePause) {
        this.veillePause = veillePause;
    }

    public Boolean getVeilleSoir() {
        return veilleSoir;
    }

    public void setVeilleSoir(Boolean veilleSoir) {
        this.veilleSoir = veilleSoir;
    }

    public Boolean getVeilleWeekend() {
        return veilleWeekend;
    }

    public void setVeilleWeekend(Boolean veilleWeekend) {
        this.veilleWeekend = veilleWeekend;
    }

    public Integer getNbTerminaux() {
        return nbTerminaux;
    }

    public void setNbTerminaux(Integer nbTerminaux) {
        this.nbTerminaux = nbTerminaux;
    }

    public Integer getNbDeplacements() {
        return nbDeplacements;
    }

    public void setNbDeplacements(Integer nbDeplacements) {
        this.nbDeplacements = nbDeplacements;
    }

    public Float getEcMensuelle() {
        return ecMensuelle;
    }

    public void setEcMensuelle(Float ecMensuelle) {
        this.ecMensuelle = ecMensuelle;
    }

    public Float getEcTotalePreta() {
        return ecTotalePreta;
    }

    public void setEcTotalePreta(Float ecTotalePreta) {
        this.ecTotalePreta = ecTotalePreta;
    }

    public Float getEcTransportMensuel() {
        return ecTransportMensuel;
    }

    public void setEcTransportMensuel(Float ecTransportMensuel) {
        this.ecTransportMensuel = ecTransportMensuel;
    }

    public Float getEcFabMateriel() {
        return ecFabMateriel;
    }

    public void setEcFabMateriel(Float ecFabMateriel) {
        this.ecFabMateriel = ecFabMateriel;
    }

    public Float getEcUtilMaterielMensuel() {
        return ecUtilMaterielMensuel;
    }

    public void setEcUtilMaterielMensuel(Float ecUtilMaterielMensuel) {
        this.ecUtilMaterielMensuel = ecUtilMaterielMensuel;
    }

    public Float getEcCommMensuel() {
        return ecCommMensuel;
    }

    public void setEcCommMensuel(Float ecCommMensuel) {
        this.ecCommMensuel = ecCommMensuel;
    }

    public ProfilDTO getProfil() {
        return profil;
    }

    public void setProfil(ProfilDTO profil) {
        this.profil = profil;
    }

    public Set<PrestationDTO> getPrestations() {
        return prestations;
    }

    public void setPrestations(Set<PrestationDTO> prestations) {
        this.prestations = prestations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrestationProfilDTO)) {
            return false;
        }

        PrestationProfilDTO prestationProfilDTO = (PrestationProfilDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prestationProfilDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrestationProfilDTO{" +
            "id=" + getId() +
            ", nbMoisPresta=" + getNbMoisPresta() +
            ", nbSemCongesEstime=" + getNbSemCongesEstime() +
            ", dureeHebdo=" + getDureeHebdo() +
            ", dureeTeletravail=" + getDureeTeletravail() +
            ", dureeReuAudio=" + getDureeReuAudio() +
            ", dureeReuVisio=" + getDureeReuVisio() +
            ", nbMailsSansPJ=" + getNbMailsSansPJ() +
            ", nbMailsAvecPJ=" + getNbMailsAvecPJ() +
            ", veillePause='" + getVeillePause() + "'" +
            ", veilleSoir='" + getVeilleSoir() + "'" +
            ", veilleWeekend='" + getVeilleWeekend() + "'" +
            ", nbTerminaux=" + getNbTerminaux() +
            ", nbDeplacements=" + getNbDeplacements() +
            ", ecMensuelle=" + getEcMensuelle() +
            ", ecTotalePreta=" + getEcTotalePreta() +
            ", ecTransportMensuel=" + getEcTransportMensuel() +
            ", ecFabMateriel=" + getEcFabMateriel() +
            ", ecUtilMaterielMensuel=" + getEcUtilMaterielMensuel() +
            ", ecCommMensuel=" + getEcCommMensuel() +
            ", profil=" + getProfil() +
            ", prestations=" + getPrestations() +
            "}";
    }
}
