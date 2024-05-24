package com.aubay.men.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Materiel.
 */
@Entity
@Table(name = "materiel")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "materiel")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Materiel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "libelle", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelle;

    @DecimalMin(value = "0")
    @Column(name = "fe_veille")
    private Float feVeille;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "materiels")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "profils", "prestations", "materiels" }, allowSetters = true)
    private Set<MaterielProfil> materielProfils = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Materiel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Materiel libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Float getFeVeille() {
        return this.feVeille;
    }

    public Materiel feVeille(Float feVeille) {
        this.setFeVeille(feVeille);
        return this;
    }

    public void setFeVeille(Float feVeille) {
        this.feVeille = feVeille;
    }

    public Set<MaterielProfil> getMaterielProfils() {
        return this.materielProfils;
    }

    public void setMaterielProfils(Set<MaterielProfil> materielProfils) {
        if (this.materielProfils != null) {
            this.materielProfils.forEach(i -> i.removeMateriel(this));
        }
        if (materielProfils != null) {
            materielProfils.forEach(i -> i.addMateriel(this));
        }
        this.materielProfils = materielProfils;
    }

    public Materiel materielProfils(Set<MaterielProfil> materielProfils) {
        this.setMaterielProfils(materielProfils);
        return this;
    }

    public Materiel addMaterielProfil(MaterielProfil materielProfil) {
        this.materielProfils.add(materielProfil);
        materielProfil.getMateriels().add(this);
        return this;
    }

    public Materiel removeMaterielProfil(MaterielProfil materielProfil) {
        this.materielProfils.remove(materielProfil);
        materielProfil.getMateriels().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Materiel)) {
            return false;
        }
        return getId() != null && getId().equals(((Materiel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Materiel{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", feVeille=" + getFeVeille() +
            "}";
    }
}
