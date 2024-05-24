package com.aubay.men.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Unite.
 */
@Entity
@Table(name = "unite")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "unite")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Unite implements Serializable {

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

    @Column(name = "est_temporelle")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean estTemporelle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Unite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Unite libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getEstTemporelle() {
        return this.estTemporelle;
    }

    public Unite estTemporelle(Boolean estTemporelle) {
        this.setEstTemporelle(estTemporelle);
        return this;
    }

    public void setEstTemporelle(Boolean estTemporelle) {
        this.estTemporelle = estTemporelle;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Unite)) {
            return false;
        }
        return getId() != null && getId().equals(((Unite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Unite{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", estTemporelle='" + getEstTemporelle() + "'" +
            "}";
    }
}
