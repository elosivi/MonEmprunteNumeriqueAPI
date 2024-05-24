package com.aubay.men.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A MaterielProfil.
 */
@Entity
@Table(name = "materiel_profil")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "materielprofil")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterielProfil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Column(name = "duree_hebdo")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer dureeHebdo;

    @Column(name = "est_neuf")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean estNeuf;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_materiel_profil__profil",
        joinColumns = @JoinColumn(name = "materiel_profil_id"),
        inverseJoinColumns = @JoinColumn(name = "profil_id")
    )
    @JsonIgnoreProperties(value = { "fonction", "transportProfils", "deplacementProfils", "materielProfils" }, allowSetters = true)
    private Set<Profil> profils = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_materiel_profil__prestation",
        joinColumns = @JoinColumn(name = "materiel_profil_id"),
        inverseJoinColumns = @JoinColumn(name = "prestation_id")
    )
    @JsonIgnoreProperties(value = { "prestationProfils", "transportProfils", "deplacementProfils", "materielProfils" }, allowSetters = true)
    private Set<Prestation> prestations = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_materiel_profil__materiel",
        joinColumns = @JoinColumn(name = "materiel_profil_id"),
        inverseJoinColumns = @JoinColumn(name = "materiel_id")
    )
    @JsonIgnoreProperties(value = { "materielProfils" }, allowSetters = true)
    private Set<Materiel> materiels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaterielProfil id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDureeHebdo() {
        return this.dureeHebdo;
    }

    public MaterielProfil dureeHebdo(Integer dureeHebdo) {
        this.setDureeHebdo(dureeHebdo);
        return this;
    }

    public void setDureeHebdo(Integer dureeHebdo) {
        this.dureeHebdo = dureeHebdo;
    }

    public Boolean getEstNeuf() {
        return this.estNeuf;
    }

    public MaterielProfil estNeuf(Boolean estNeuf) {
        this.setEstNeuf(estNeuf);
        return this;
    }

    public void setEstNeuf(Boolean estNeuf) {
        this.estNeuf = estNeuf;
    }

    public Set<Profil> getProfils() {
        return this.profils;
    }

    public void setProfils(Set<Profil> profils) {
        this.profils = profils;
    }

    public MaterielProfil profils(Set<Profil> profils) {
        this.setProfils(profils);
        return this;
    }

    public MaterielProfil addProfil(Profil profil) {
        this.profils.add(profil);
        return this;
    }

    public MaterielProfil removeProfil(Profil profil) {
        this.profils.remove(profil);
        return this;
    }

    public Set<Prestation> getPrestations() {
        return this.prestations;
    }

    public void setPrestations(Set<Prestation> prestations) {
        this.prestations = prestations;
    }

    public MaterielProfil prestations(Set<Prestation> prestations) {
        this.setPrestations(prestations);
        return this;
    }

    public MaterielProfil addPrestation(Prestation prestation) {
        this.prestations.add(prestation);
        return this;
    }

    public MaterielProfil removePrestation(Prestation prestation) {
        this.prestations.remove(prestation);
        return this;
    }

    public Set<Materiel> getMateriels() {
        return this.materiels;
    }

    public void setMateriels(Set<Materiel> materiels) {
        this.materiels = materiels;
    }

    public MaterielProfil materiels(Set<Materiel> materiels) {
        this.setMateriels(materiels);
        return this;
    }

    public MaterielProfil addMateriel(Materiel materiel) {
        this.materiels.add(materiel);
        return this;
    }

    public MaterielProfil removeMateriel(Materiel materiel) {
        this.materiels.remove(materiel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterielProfil)) {
            return false;
        }
        return getId() != null && getId().equals(((MaterielProfil) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterielProfil{" +
            "id=" + getId() +
            ", dureeHebdo=" + getDureeHebdo() +
            ", estNeuf='" + getEstNeuf() + "'" +
            "}";
    }
}
