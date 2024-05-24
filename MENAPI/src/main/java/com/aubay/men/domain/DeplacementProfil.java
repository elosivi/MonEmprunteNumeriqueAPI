package com.aubay.men.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DeplacementProfil.
 */
@Entity
@Table(name = "deplacement_profil")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "deplacementprofil")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeplacementProfil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Column(name = "nb_deplacement")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbDeplacement;

    @Min(value = 0)
    @Column(name = "km_presta")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer kmPresta;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_deplacement_profil__profil",
        joinColumns = @JoinColumn(name = "deplacement_profil_id"),
        inverseJoinColumns = @JoinColumn(name = "profil_id")
    )
    @JsonIgnoreProperties(value = { "fonction", "transportProfils", "deplacementProfils", "materielProfils" }, allowSetters = true)
    private Set<Profil> profils = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_deplacement_profil__prestation",
        joinColumns = @JoinColumn(name = "deplacement_profil_id"),
        inverseJoinColumns = @JoinColumn(name = "prestation_id")
    )
    @JsonIgnoreProperties(value = { "prestationProfils", "transportProfils", "deplacementProfils", "materielProfils" }, allowSetters = true)
    private Set<Prestation> prestations = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_deplacement_profil__transport",
        joinColumns = @JoinColumn(name = "deplacement_profil_id"),
        inverseJoinColumns = @JoinColumn(name = "transport_id")
    )
    @JsonIgnoreProperties(value = { "transportProfils", "deplacementProfils" }, allowSetters = true)
    private Set<Transport> transports = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeplacementProfil id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbDeplacement() {
        return this.nbDeplacement;
    }

    public DeplacementProfil nbDeplacement(Integer nbDeplacement) {
        this.setNbDeplacement(nbDeplacement);
        return this;
    }

    public void setNbDeplacement(Integer nbDeplacement) {
        this.nbDeplacement = nbDeplacement;
    }

    public Integer getKmPresta() {
        return this.kmPresta;
    }

    public DeplacementProfil kmPresta(Integer kmPresta) {
        this.setKmPresta(kmPresta);
        return this;
    }

    public void setKmPresta(Integer kmPresta) {
        this.kmPresta = kmPresta;
    }

    public Set<Profil> getProfils() {
        return this.profils;
    }

    public void setProfils(Set<Profil> profils) {
        this.profils = profils;
    }

    public DeplacementProfil profils(Set<Profil> profils) {
        this.setProfils(profils);
        return this;
    }

    public DeplacementProfil addProfil(Profil profil) {
        this.profils.add(profil);
        return this;
    }

    public DeplacementProfil removeProfil(Profil profil) {
        this.profils.remove(profil);
        return this;
    }

    public Set<Prestation> getPrestations() {
        return this.prestations;
    }

    public void setPrestations(Set<Prestation> prestations) {
        this.prestations = prestations;
    }

    public DeplacementProfil prestations(Set<Prestation> prestations) {
        this.setPrestations(prestations);
        return this;
    }

    public DeplacementProfil addPrestation(Prestation prestation) {
        this.prestations.add(prestation);
        return this;
    }

    public DeplacementProfil removePrestation(Prestation prestation) {
        this.prestations.remove(prestation);
        return this;
    }

    public Set<Transport> getTransports() {
        return this.transports;
    }

    public void setTransports(Set<Transport> transports) {
        this.transports = transports;
    }

    public DeplacementProfil transports(Set<Transport> transports) {
        this.setTransports(transports);
        return this;
    }

    public DeplacementProfil addTransport(Transport transport) {
        this.transports.add(transport);
        return this;
    }

    public DeplacementProfil removeTransport(Transport transport) {
        this.transports.remove(transport);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeplacementProfil)) {
            return false;
        }
        return getId() != null && getId().equals(((DeplacementProfil) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeplacementProfil{" +
            "id=" + getId() +
            ", nbDeplacement=" + getNbDeplacement() +
            ", kmPresta=" + getKmPresta() +
            "}";
    }
}
