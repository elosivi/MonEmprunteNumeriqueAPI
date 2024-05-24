package com.aubay.men.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * TEntité profil
 */
@Entity
@Table(name = "profil")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "profil")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Profil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(min = 3, max = 50)
    @Column(name = "nom", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nom;

    @Size(min = 3, max = 50)
    @Column(name = "prenom", length = 50)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String prenom;

    @NotNull
    @Column(name = "email", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    private Fonction fonction;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "profils")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "profils", "prestations", "transports" }, allowSetters = true)
    private Set<TransportProfil> transportProfils = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "profils")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "profils", "prestations", "transports" }, allowSetters = true)
    private Set<DeplacementProfil> deplacementProfils = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "profils")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "profils", "prestations", "materiels" }, allowSetters = true)
    private Set<MaterielProfil> materielProfils = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Profil id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Profil nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Profil prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public Profil email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Fonction getFonction() {
        return this.fonction;
    }

    public void setFonction(Fonction fonction) {
        this.fonction = fonction;
    }

    public Profil fonction(Fonction fonction) {
        this.setFonction(fonction);
        return this;
    }

    public Set<TransportProfil> getTransportProfils() {
        return this.transportProfils;
    }

    public void setTransportProfils(Set<TransportProfil> transportProfils) {
        if (this.transportProfils != null) {
            this.transportProfils.forEach(i -> i.removeProfil(this));
        }
        if (transportProfils != null) {
            transportProfils.forEach(i -> i.addProfil(this));
        }
        this.transportProfils = transportProfils;
    }

    public Profil transportProfils(Set<TransportProfil> transportProfils) {
        this.setTransportProfils(transportProfils);
        return this;
    }

    public Profil addTransportProfil(TransportProfil transportProfil) {
        this.transportProfils.add(transportProfil);
        transportProfil.getProfils().add(this);
        return this;
    }

    public Profil removeTransportProfil(TransportProfil transportProfil) {
        this.transportProfils.remove(transportProfil);
        transportProfil.getProfils().remove(this);
        return this;
    }

    public Set<DeplacementProfil> getDeplacementProfils() {
        return this.deplacementProfils;
    }

    public void setDeplacementProfils(Set<DeplacementProfil> deplacementProfils) {
        if (this.deplacementProfils != null) {
            this.deplacementProfils.forEach(i -> i.removeProfil(this));
        }
        if (deplacementProfils != null) {
            deplacementProfils.forEach(i -> i.addProfil(this));
        }
        this.deplacementProfils = deplacementProfils;
    }

    public Profil deplacementProfils(Set<DeplacementProfil> deplacementProfils) {
        this.setDeplacementProfils(deplacementProfils);
        return this;
    }

    public Profil addDeplacementProfil(DeplacementProfil deplacementProfil) {
        this.deplacementProfils.add(deplacementProfil);
        deplacementProfil.getProfils().add(this);
        return this;
    }

    public Profil removeDeplacementProfil(DeplacementProfil deplacementProfil) {
        this.deplacementProfils.remove(deplacementProfil);
        deplacementProfil.getProfils().remove(this);
        return this;
    }

    public Set<MaterielProfil> getMaterielProfils() {
        return this.materielProfils;
    }

    public void setMaterielProfils(Set<MaterielProfil> materielProfils) {
        if (this.materielProfils != null) {
            this.materielProfils.forEach(i -> i.removeProfil(this));
        }
        if (materielProfils != null) {
            materielProfils.forEach(i -> i.addProfil(this));
        }
        this.materielProfils = materielProfils;
    }

    public Profil materielProfils(Set<MaterielProfil> materielProfils) {
        this.setMaterielProfils(materielProfils);
        return this;
    }

    public Profil addMaterielProfil(MaterielProfil materielProfil) {
        this.materielProfils.add(materielProfil);
        materielProfil.getProfils().add(this);
        return this;
    }

    public Profil removeMaterielProfil(MaterielProfil materielProfil) {
        this.materielProfils.remove(materielProfil);
        materielProfil.getProfils().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profil)) {
            return false;
        }
        return getId() != null && getId().equals(((Profil) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profil{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
