package com.aubay.men.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Communication.
 */
@Entity
@Table(name = "communication")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "communication")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Communication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 200)
    @Column(name = "libelle", length = 200, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelle;

    @DecimalMin(value = "0")
    @Column(name = "fe")
    private Float fe;

    @Size(min = 1, max = 100)
    @Column(name = "fe_unite", length = 100)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String feUnite;

    @ManyToOne(fetch = FetchType.LAZY)
    private Unite unite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Communication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Communication libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Float getFe() {
        return this.fe;
    }

    public Communication fe(Float fe) {
        this.setFe(fe);
        return this;
    }

    public void setFe(Float fe) {
        this.fe = fe;
    }

    public String getFeUnite() {
        return this.feUnite;
    }

    public Communication feUnite(String feUnite) {
        this.setFeUnite(feUnite);
        return this;
    }

    public void setFeUnite(String feUnite) {
        this.feUnite = feUnite;
    }

    public Unite getUnite() {
        return this.unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }

    public Communication unite(Unite unite) {
        this.setUnite(unite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Communication)) {
            return false;
        }
        return getId() != null && getId().equals(((Communication) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Communication{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", fe=" + getFe() +
            ", feUnite='" + getFeUnite() + "'" +
            "}";
    }
}
