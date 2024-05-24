package com.aubay.men.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PrestationProfil.
 */
@Entity
@Table(name = "prestation_profil")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prestationprofil")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PrestationProfil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Column(name = "nb_mois_presta")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbMoisPresta;

    @Min(value = 0)
    @Column(name = "nb_sem_conges_estime")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbSemCongesEstime;

    @Min(value = 0)
    @Column(name = "duree_hebdo")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer dureeHebdo;

    @Min(value = 0)
    @Column(name = "duree_teletravail")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer dureeTeletravail;

    @DecimalMin(value = "0")
    @Column(name = "duree_reu_audio")
    private Float dureeReuAudio;

    @DecimalMin(value = "0")
    @Column(name = "duree_reu_visio")
    private Float dureeReuVisio;

    @Min(value = 0)
    @Column(name = "nb_mails_sans_pj")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbMailsSansPJ;

    @Min(value = 0)
    @Column(name = "nb_mails_avec_pj")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbMailsAvecPJ;

    @Column(name = "veille_pause")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean veillePause;

    @Column(name = "veille_soir")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean veilleSoir;

    @Column(name = "veille_weekend")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean veilleWeekend;

    @Min(value = 0)
    @Column(name = "nb_terminaux")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbTerminaux;

    @Min(value = 0)
    @Column(name = "nb_deplacements")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbDeplacements;

    @DecimalMin(value = "0")
    @Column(name = "ec_mensuelle")
    private Float ecMensuelle;

    @DecimalMin(value = "0")
    @Column(name = "ec_totale_preta")
    private Float ecTotalePreta;

    @DecimalMin(value = "0")
    @Column(name = "ec_transport_mensuel")
    private Float ecTransportMensuel;

    @DecimalMin(value = "0")
    @Column(name = "ec_fab_materiel")
    private Float ecFabMateriel;

    @DecimalMin(value = "0")
    @Column(name = "ec_util_materiel_mensuel")
    private Float ecUtilMaterielMensuel;

    @DecimalMin(value = "0")
    @Column(name = "ec_comm_mensuel")
    private Float ecCommMensuel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "fonction", "transportProfils", "deplacementProfils", "materielProfils" }, allowSetters = true)
    private Profil profil;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_prestation_profil__prestation",
        joinColumns = @JoinColumn(name = "prestation_profil_id"),
        inverseJoinColumns = @JoinColumn(name = "prestation_id")
    )
    @JsonIgnoreProperties(value = { "prestationProfils", "transportProfils", "deplacementProfils", "materielProfils" }, allowSetters = true)
    private Set<Prestation> prestations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PrestationProfil id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbMoisPresta() {
        return this.nbMoisPresta;
    }

    public PrestationProfil nbMoisPresta(Integer nbMoisPresta) {
        this.setNbMoisPresta(nbMoisPresta);
        return this;
    }

    public void setNbMoisPresta(Integer nbMoisPresta) {
        this.nbMoisPresta = nbMoisPresta;
    }

    public Integer getNbSemCongesEstime() {
        return this.nbSemCongesEstime;
    }

    public PrestationProfil nbSemCongesEstime(Integer nbSemCongesEstime) {
        this.setNbSemCongesEstime(nbSemCongesEstime);
        return this;
    }

    public void setNbSemCongesEstime(Integer nbSemCongesEstime) {
        this.nbSemCongesEstime = nbSemCongesEstime;
    }

    public Integer getDureeHebdo() {
        return this.dureeHebdo;
    }

    public PrestationProfil dureeHebdo(Integer dureeHebdo) {
        this.setDureeHebdo(dureeHebdo);
        return this;
    }

    public void setDureeHebdo(Integer dureeHebdo) {
        this.dureeHebdo = dureeHebdo;
    }

    public Integer getDureeTeletravail() {
        return this.dureeTeletravail;
    }

    public PrestationProfil dureeTeletravail(Integer dureeTeletravail) {
        this.setDureeTeletravail(dureeTeletravail);
        return this;
    }

    public void setDureeTeletravail(Integer dureeTeletravail) {
        this.dureeTeletravail = dureeTeletravail;
    }

    public Float getDureeReuAudio() {
        return this.dureeReuAudio;
    }

    public PrestationProfil dureeReuAudio(Float dureeReuAudio) {
        this.setDureeReuAudio(dureeReuAudio);
        return this;
    }

    public void setDureeReuAudio(Float dureeReuAudio) {
        this.dureeReuAudio = dureeReuAudio;
    }

    public Float getDureeReuVisio() {
        return this.dureeReuVisio;
    }

    public PrestationProfil dureeReuVisio(Float dureeReuVisio) {
        this.setDureeReuVisio(dureeReuVisio);
        return this;
    }

    public void setDureeReuVisio(Float dureeReuVisio) {
        this.dureeReuVisio = dureeReuVisio;
    }

    public Integer getNbMailsSansPJ() {
        return this.nbMailsSansPJ;
    }

    public PrestationProfil nbMailsSansPJ(Integer nbMailsSansPJ) {
        this.setNbMailsSansPJ(nbMailsSansPJ);
        return this;
    }

    public void setNbMailsSansPJ(Integer nbMailsSansPJ) {
        this.nbMailsSansPJ = nbMailsSansPJ;
    }

    public Integer getNbMailsAvecPJ() {
        return this.nbMailsAvecPJ;
    }

    public PrestationProfil nbMailsAvecPJ(Integer nbMailsAvecPJ) {
        this.setNbMailsAvecPJ(nbMailsAvecPJ);
        return this;
    }

    public void setNbMailsAvecPJ(Integer nbMailsAvecPJ) {
        this.nbMailsAvecPJ = nbMailsAvecPJ;
    }

    public Boolean getVeillePause() {
        return this.veillePause;
    }

    public PrestationProfil veillePause(Boolean veillePause) {
        this.setVeillePause(veillePause);
        return this;
    }

    public void setVeillePause(Boolean veillePause) {
        this.veillePause = veillePause;
    }

    public Boolean getVeilleSoir() {
        return this.veilleSoir;
    }

    public PrestationProfil veilleSoir(Boolean veilleSoir) {
        this.setVeilleSoir(veilleSoir);
        return this;
    }

    public void setVeilleSoir(Boolean veilleSoir) {
        this.veilleSoir = veilleSoir;
    }

    public Boolean getVeilleWeekend() {
        return this.veilleWeekend;
    }

    public PrestationProfil veilleWeekend(Boolean veilleWeekend) {
        this.setVeilleWeekend(veilleWeekend);
        return this;
    }

    public void setVeilleWeekend(Boolean veilleWeekend) {
        this.veilleWeekend = veilleWeekend;
    }

    public Integer getNbTerminaux() {
        return this.nbTerminaux;
    }

    public PrestationProfil nbTerminaux(Integer nbTerminaux) {
        this.setNbTerminaux(nbTerminaux);
        return this;
    }

    public void setNbTerminaux(Integer nbTerminaux) {
        this.nbTerminaux = nbTerminaux;
    }

    public Integer getNbDeplacements() {
        return this.nbDeplacements;
    }

    public PrestationProfil nbDeplacements(Integer nbDeplacements) {
        this.setNbDeplacements(nbDeplacements);
        return this;
    }

    public void setNbDeplacements(Integer nbDeplacements) {
        this.nbDeplacements = nbDeplacements;
    }

    public Float getEcMensuelle() {
        return this.ecMensuelle;
    }

    public PrestationProfil ecMensuelle(Float ecMensuelle) {
        this.setEcMensuelle(ecMensuelle);
        return this;
    }

    public void setEcMensuelle(Float ecMensuelle) {
        this.ecMensuelle = ecMensuelle;
    }

    public Float getEcTotalePreta() {
        return this.ecTotalePreta;
    }

    public PrestationProfil ecTotalePreta(Float ecTotalePreta) {
        this.setEcTotalePreta(ecTotalePreta);
        return this;
    }

    public void setEcTotalePreta(Float ecTotalePreta) {
        this.ecTotalePreta = ecTotalePreta;
    }

    public Float getEcTransportMensuel() {
        return this.ecTransportMensuel;
    }

    public PrestationProfil ecTransportMensuel(Float ecTransportMensuel) {
        this.setEcTransportMensuel(ecTransportMensuel);
        return this;
    }

    public void setEcTransportMensuel(Float ecTransportMensuel) {
        this.ecTransportMensuel = ecTransportMensuel;
    }

    public Float getEcFabMateriel() {
        return this.ecFabMateriel;
    }

    public PrestationProfil ecFabMateriel(Float ecFabMateriel) {
        this.setEcFabMateriel(ecFabMateriel);
        return this;
    }

    public void setEcFabMateriel(Float ecFabMateriel) {
        this.ecFabMateriel = ecFabMateriel;
    }

    public Float getEcUtilMaterielMensuel() {
        return this.ecUtilMaterielMensuel;
    }

    public PrestationProfil ecUtilMaterielMensuel(Float ecUtilMaterielMensuel) {
        this.setEcUtilMaterielMensuel(ecUtilMaterielMensuel);
        return this;
    }

    public void setEcUtilMaterielMensuel(Float ecUtilMaterielMensuel) {
        this.ecUtilMaterielMensuel = ecUtilMaterielMensuel;
    }

    public Float getEcCommMensuel() {
        return this.ecCommMensuel;
    }

    public PrestationProfil ecCommMensuel(Float ecCommMensuel) {
        this.setEcCommMensuel(ecCommMensuel);
        return this;
    }

    public void setEcCommMensuel(Float ecCommMensuel) {
        this.ecCommMensuel = ecCommMensuel;
    }

    public Profil getProfil() {
        return this.profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public PrestationProfil profil(Profil profil) {
        this.setProfil(profil);
        return this;
    }

    public Set<Prestation> getPrestations() {
        return this.prestations;
    }

    public void setPrestations(Set<Prestation> prestations) {
        this.prestations = prestations;
    }

    public PrestationProfil prestations(Set<Prestation> prestations) {
        this.setPrestations(prestations);
        return this;
    }

    public PrestationProfil addPrestation(Prestation prestation) {
        this.prestations.add(prestation);
        return this;
    }

    public PrestationProfil removePrestation(Prestation prestation) {
        this.prestations.remove(prestation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrestationProfil)) {
            return false;
        }
        return getId() != null && getId().equals(((PrestationProfil) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrestationProfil{" +
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
            "}";
    }
}
