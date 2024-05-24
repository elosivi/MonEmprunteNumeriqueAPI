package com.aubay.men.service.dto;

import com.aubay.men.domain.enumeration.LieuPresta;
import com.aubay.men.domain.enumeration.TypePresta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.aubay.men.domain.Prestation} entity.
 */
@Schema(description = "Entité Prestation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrestationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String nomPrestation;

    @NotNull
    @Size(min = 3, max = 150)
    private String nomUtilisateur;

    @NotNull
    @Size(min = 3, max = 150)
    private String nomMission;

    @NotNull
    @Size(min = 3, max = 150)
    private String nomClient;

    @NotNull
    @Size(min = 1, max = 50)
    private String ecUnite;

    @NotNull
    private Float ecMensuelle;

    @DecimalMin(value = "0")
    private Float ecTotale;

    @DecimalMin(value = "0")
    private Float ecTransportMensuel;

    @DecimalMin(value = "0")
    private Float ecFabMateriel;

    @DecimalMin(value = "0")
    private Float ecUtilMaterielMensuel;

    @DecimalMin(value = "0")
    private Float ecCommMensuel;

    @NotNull
    @Min(value = 0)
    private Integer nbrProfils;

    @NotNull
    @Min(value = 0)
    private Integer dureeMois;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @NotNull
    private TypePresta typePresta;

    @NotNull
    private LieuPresta lieupresta;

    private Boolean donneesSaisies;

    private Boolean donneesReperes;

    private Set<PrestationProfilDTO> prestationProfils = new HashSet<>();

    private Set<TransportProfilDTO> transportProfils = new HashSet<>();

    private Set<DeplacementProfilDTO> deplacementProfils = new HashSet<>();

    private Set<MaterielProfilDTO> materielProfils = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPrestation() {
        return nomPrestation;
    }

    public void setNomPrestation(String nomPrestation) {
        this.nomPrestation = nomPrestation;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getNomMission() {
        return nomMission;
    }

    public void setNomMission(String nomMission) {
        this.nomMission = nomMission;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getEcUnite() {
        return ecUnite;
    }

    public void setEcUnite(String ecUnite) {
        this.ecUnite = ecUnite;
    }

    public Float getEcMensuelle() {
        return ecMensuelle;
    }

    public void setEcMensuelle(Float ecMensuelle) {
        this.ecMensuelle = ecMensuelle;
    }

    public Float getEcTotale() {
        return ecTotale;
    }

    public void setEcTotale(Float ecTotale) {
        this.ecTotale = ecTotale;
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

    public Integer getNbrProfils() {
        return nbrProfils;
    }

    public void setNbrProfils(Integer nbrProfils) {
        this.nbrProfils = nbrProfils;
    }

    public Integer getDureeMois() {
        return dureeMois;
    }

    public void setDureeMois(Integer dureeMois) {
        this.dureeMois = dureeMois;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public TypePresta getTypePresta() {
        return typePresta;
    }

    public void setTypePresta(TypePresta typePresta) {
        this.typePresta = typePresta;
    }

    public LieuPresta getLieupresta() {
        return lieupresta;
    }

    public void setLieupresta(LieuPresta lieupresta) {
        this.lieupresta = lieupresta;
    }

    public Boolean getDonneesSaisies() {
        return donneesSaisies;
    }

    public void setDonneesSaisies(Boolean donneesSaisies) {
        this.donneesSaisies = donneesSaisies;
    }

    public Boolean getDonneesReperes() {
        return donneesReperes;
    }

    public void setDonneesReperes(Boolean donneesReperes) {
        this.donneesReperes = donneesReperes;
    }

    public Set<PrestationProfilDTO> getPrestationProfils() {
        return prestationProfils;
    }

    public void setPrestationProfils(Set<PrestationProfilDTO> prestationProfils) {
        this.prestationProfils = prestationProfils;
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
        if (!(o instanceof PrestationDTO)) {
            return false;
        }

        PrestationDTO prestationDTO = (PrestationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prestationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrestationDTO{" +
            "id=" + getId() +
            ", nomPrestation='" + getNomPrestation() + "'" +
            ", nomUtilisateur='" + getNomUtilisateur() + "'" +
            ", nomMission='" + getNomMission() + "'" +
            ", nomClient='" + getNomClient() + "'" +
            ", ecUnite='" + getEcUnite() + "'" +
            ", ecMensuelle=" + getEcMensuelle() +
            ", ecTotale=" + getEcTotale() +
            ", ecTransportMensuel=" + getEcTransportMensuel() +
            ", ecFabMateriel=" + getEcFabMateriel() +
            ", ecUtilMaterielMensuel=" + getEcUtilMaterielMensuel() +
            ", ecCommMensuel=" + getEcCommMensuel() +
            ", nbrProfils=" + getNbrProfils() +
            ", dureeMois=" + getDureeMois() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", typePresta='" + getTypePresta() + "'" +
            ", lieupresta='" + getLieupresta() + "'" +
            ", donneesSaisies='" + getDonneesSaisies() + "'" +
            ", donneesReperes='" + getDonneesReperes() + "'" +
            ", prestationProfils=" + getPrestationProfils() +
            ", transportProfils=" + getTransportProfils() +
            ", deplacementProfils=" + getDeplacementProfils() +
            ", materielProfils=" + getMaterielProfils() +
            "}";
    }
}
