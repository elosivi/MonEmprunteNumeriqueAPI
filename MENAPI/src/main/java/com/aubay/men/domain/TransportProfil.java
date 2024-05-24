package com.aubay.men.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TransportProfil.
 */
@Entity
@Table(name = "transport_profil")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "transportprofil")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransportProfil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Column(name = "nb_hebdo")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbHebdo;

    @Min(value = 0)
    @Column(name = "km_hebdo")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer kmHebdo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_transport_profil__profil",
        joinColumns = @JoinColumn(name = "transport_profil_id"),
        inverseJoinColumns = @JoinColumn(name = "profil_id")
    )
    @JsonIgnoreProperties(value = { "fonction", "transportProfils", "deplacementProfils", "materielProfils" }, allowSetters = true)
    private Set<Profil> profils = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_transport_profil__prestation",
        joinColumns = @JoinColumn(name = "transport_profil_id"),
        inverseJoinColumns = @JoinColumn(name = "prestation_id")
    )
    @JsonIgnoreProperties(value = { "prestationProfils", "transportProfils", "deplacementProfils", "materielProfils" }, allowSetters = true)
    private Set<Prestation> prestations = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_transport_profil__transport",
        joinColumns = @JoinColumn(name = "transport_profil_id"),
        inverseJoinColumns = @JoinColumn(name = "transport_id")
    )
    @JsonIgnoreProperties(value = { "transportProfils", "deplacementProfils" }, allowSetters = true)
    private Set<Transport> transports = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransportProfil id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbHebdo() {
        return this.nbHebdo;
    }

    public TransportProfil nbHebdo(Integer nbHebdo) {
        this.setNbHebdo(nbHebdo);
        return this;
    }

    public void setNbHebdo(Integer nbHebdo) {
        this.nbHebdo = nbHebdo;
    }

    public Integer getKmHebdo() {
        return this.kmHebdo;
    }

    public TransportProfil kmHebdo(Integer kmHebdo) {
        this.setKmHebdo(kmHebdo);
        return this;
    }

    public void setKmHebdo(Integer kmHebdo) {
        this.kmHebdo = kmHebdo;
    }

    public Set<Profil> getProfils() {
        return this.profils;
    }

    public void setProfils(Set<Profil> profils) {
        this.profils = profils;
    }

    public TransportProfil profils(Set<Profil> profils) {
        this.setProfils(profils);
        return this;
    }

    public TransportProfil addProfil(Profil profil) {
        this.profils.add(profil);
        return this;
    }

    public TransportProfil removeProfil(Profil profil) {
        this.profils.remove(profil);
        return this;
    }

    public Set<Prestation> getPrestations() {
        return this.prestations;
    }

    public void setPrestations(Set<Prestation> prestations) {
        this.prestations = prestations;
    }

    public TransportProfil prestations(Set<Prestation> prestations) {
        this.setPrestations(prestations);
        return this;
    }

    public TransportProfil addPrestation(Prestation prestation) {
        this.prestations.add(prestation);
        return this;
    }

    public TransportProfil removePrestation(Prestation prestation) {
        this.prestations.remove(prestation);
        return this;
    }

    public Set<Transport> getTransports() {
        return this.transports;
    }

    public void setTransports(Set<Transport> transports) {
        this.transports = transports;
    }

    public TransportProfil transports(Set<Transport> transports) {
        this.setTransports(transports);
        return this;
    }

    public TransportProfil addTransport(Transport transport) {
        this.transports.add(transport);
        return this;
    }

    public TransportProfil removeTransport(Transport transport) {
        this.transports.remove(transport);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransportProfil)) {
            return false;
        }
        return getId() != null && getId().equals(((TransportProfil) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransportProfil{" +
            "id=" + getId() +
            ", nbHebdo=" + getNbHebdo() +
            ", kmHebdo=" + getKmHebdo() +
            "}";
    }
}
