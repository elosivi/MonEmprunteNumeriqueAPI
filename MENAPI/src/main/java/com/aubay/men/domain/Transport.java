package com.aubay.men.domain;

import com.aubay.men.domain.enumeration.TypeMoteur;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Référentiel des moyens de transports utilisables
 */
@Entity
@Table(name = "transport")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "transport")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "categorie", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String categorie;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_moteur")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TypeMoteur typeMoteur;

    @DecimalMin(value = "0")
    @Column(name = "fe_km")
    private Float feKm;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "transports")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "profils", "prestations", "transports" }, allowSetters = true)
    private Set<TransportProfil> transportProfils = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "transports")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "profils", "prestations", "transports" }, allowSetters = true)
    private Set<DeplacementProfil> deplacementProfils = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public Transport categorie(String categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public TypeMoteur getTypeMoteur() {
        return this.typeMoteur;
    }

    public Transport typeMoteur(TypeMoteur typeMoteur) {
        this.setTypeMoteur(typeMoteur);
        return this;
    }

    public void setTypeMoteur(TypeMoteur typeMoteur) {
        this.typeMoteur = typeMoteur;
    }

    public Float getFeKm() {
        return this.feKm;
    }

    public Transport feKm(Float feKm) {
        this.setFeKm(feKm);
        return this;
    }

    public void setFeKm(Float feKm) {
        this.feKm = feKm;
    }

    public Set<TransportProfil> getTransportProfils() {
        return this.transportProfils;
    }

    public void setTransportProfils(Set<TransportProfil> transportProfils) {
        if (this.transportProfils != null) {
            this.transportProfils.forEach(i -> i.removeTransport(this));
        }
        if (transportProfils != null) {
            transportProfils.forEach(i -> i.addTransport(this));
        }
        this.transportProfils = transportProfils;
    }

    public Transport transportProfils(Set<TransportProfil> transportProfils) {
        this.setTransportProfils(transportProfils);
        return this;
    }

    public Transport addTransportProfil(TransportProfil transportProfil) {
        this.transportProfils.add(transportProfil);
        transportProfil.getTransports().add(this);
        return this;
    }

    public Transport removeTransportProfil(TransportProfil transportProfil) {
        this.transportProfils.remove(transportProfil);
        transportProfil.getTransports().remove(this);
        return this;
    }

    public Set<DeplacementProfil> getDeplacementProfils() {
        return this.deplacementProfils;
    }

    public void setDeplacementProfils(Set<DeplacementProfil> deplacementProfils) {
        if (this.deplacementProfils != null) {
            this.deplacementProfils.forEach(i -> i.removeTransport(this));
        }
        if (deplacementProfils != null) {
            deplacementProfils.forEach(i -> i.addTransport(this));
        }
        this.deplacementProfils = deplacementProfils;
    }

    public Transport deplacementProfils(Set<DeplacementProfil> deplacementProfils) {
        this.setDeplacementProfils(deplacementProfils);
        return this;
    }

    public Transport addDeplacementProfil(DeplacementProfil deplacementProfil) {
        this.deplacementProfils.add(deplacementProfil);
        deplacementProfil.getTransports().add(this);
        return this;
    }

    public Transport removeDeplacementProfil(DeplacementProfil deplacementProfil) {
        this.deplacementProfils.remove(deplacementProfil);
        deplacementProfil.getTransports().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transport)) {
            return false;
        }
        return getId() != null && getId().equals(((Transport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transport{" +
            "id=" + getId() +
            ", categorie='" + getCategorie() + "'" +
            ", typeMoteur='" + getTypeMoteur() + "'" +
            ", feKm=" + getFeKm() +
            "}";
    }
}
