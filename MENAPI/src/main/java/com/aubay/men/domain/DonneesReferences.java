package com.aubay.men.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DonneesReferences.
 */
@Entity
@Table(name = "donnees_references")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "donneesreferences")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DonneesReferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "libelle", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelle;

    @Column(name = "donnee_reference")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer donneeReference;

    @ManyToOne(fetch = FetchType.LAZY)
    private Unite unite;

    @ManyToOne(fetch = FetchType.LAZY)
    private Unite temporalite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DonneesReferences id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public DonneesReferences libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getDonneeReference() {
        return this.donneeReference;
    }

    public DonneesReferences donneeReference(Integer donneeReference) {
        this.setDonneeReference(donneeReference);
        return this;
    }

    public void setDonneeReference(Integer donneeReference) {
        this.donneeReference = donneeReference;
    }

    public Unite getUnite() {
        return this.unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }

    public DonneesReferences unite(Unite unite) {
        this.setUnite(unite);
        return this;
    }

    public Unite getTemporalite() {
        return this.temporalite;
    }

    public void setTemporalite(Unite unite) {
        this.temporalite = unite;
    }

    public DonneesReferences temporalite(Unite unite) {
        this.setTemporalite(unite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DonneesReferences)) {
            return false;
        }
        return getId() != null && getId().equals(((DonneesReferences) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DonneesReferences{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", donneeReference=" + getDonneeReference() +
            "}";
    }
}
