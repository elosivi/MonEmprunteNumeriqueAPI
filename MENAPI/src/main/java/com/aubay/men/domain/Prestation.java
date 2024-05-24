package com.aubay.men.domain;

import com.aubay.men.domain.enumeration.LieuPresta;
import com.aubay.men.domain.enumeration.TypePresta;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité Prestation
 */
@Entity
@Table(name = "prestation")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "prestation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Prestation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "nom_prestation", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nomPrestation;

    @NotNull
    @Size(min = 3, max = 150)
    @Column(name = "nom_utilisateur", length = 150, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nomUtilisateur;

    @NotNull
    @Size(min = 3, max = 150)
    @Column(name = "nom_mission", length = 150, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nomMission;

    @NotNull
    @Size(min = 3, max = 150)
    @Column(name = "nom_client", length = 150, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nomClient;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ec_unite", length = 50, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String ecUnite;

    @NotNull
    @Column(name = "ec_mensuelle", nullable = false)
    private Float ecMensuelle;

    @DecimalMin(value = "0")
    @Column(name = "ec_totale")
    private Float ecTotale;

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

    @NotNull
    @Min(value = 0)
    @Column(name = "nbr_profils", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbrProfils;

    @NotNull
    @Min(value = 0)
    @Column(name = "duree_mois", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer dureeMois;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_presta", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TypePresta typePresta;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "lieupresta", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private LieuPresta lieupresta;

    @Column(name = "donnees_saisies")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean donneesSaisies;

    @Column(name = "donnees_reperes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean donneesReperes;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "prestations")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "profil", "prestations" }, allowSetters = true)
    private Set<PrestationProfil> prestationProfils = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "prestations")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "profils", "prestations", "transports" }, allowSetters = true)
    private Set<TransportProfil> transportProfils = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "prestations")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "profils", "prestations", "transports" }, allowSetters = true)
    private Set<DeplacementProfil> deplacementProfils = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "prestations")
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "profils", "prestations", "materiels" }, allowSetters = true)
    private Set<MaterielProfil> materielProfils = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Prestation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPrestation() {
        return this.nomPrestation;
    }

    public Prestation nomPrestation(String nomPrestation) {
        this.setNomPrestation(nomPrestation);
        return this;
    }

    public void setNomPrestation(String nomPrestation) {
        this.nomPrestation = nomPrestation;
    }

    public String getNomUtilisateur() {
        return this.nomUtilisateur;
    }

    public Prestation nomUtilisateur(String nomUtilisateur) {
        this.setNomUtilisateur(nomUtilisateur);
        return this;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getNomMission() {
        return this.nomMission;
    }

    public Prestation nomMission(String nomMission) {
        this.setNomMission(nomMission);
        return this;
    }

    public void setNomMission(String nomMission) {
        this.nomMission = nomMission;
    }

    public String getNomClient() {
        return this.nomClient;
    }

    public Prestation nomClient(String nomClient) {
        this.setNomClient(nomClient);
        return this;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getEcUnite() {
        return this.ecUnite;
    }

    public Prestation ecUnite(String ecUnite) {
        this.setEcUnite(ecUnite);
        return this;
    }

    public void setEcUnite(String ecUnite) {
        this.ecUnite = ecUnite;
    }

    public Float getEcMensuelle() {
        return this.ecMensuelle;
    }

    public Prestation ecMensuelle(Float ecMensuelle) {
        this.setEcMensuelle(ecMensuelle);
        return this;
    }

    public void setEcMensuelle(Float ecMensuelle) {
        this.ecMensuelle = ecMensuelle;
    }

    public Float getEcTotale() {
        return this.ecTotale;
    }

    public Prestation ecTotale(Float ecTotale) {
        this.setEcTotale(ecTotale);
        return this;
    }

    public void setEcTotale(Float ecTotale) {
        this.ecTotale = ecTotale;
    }

    public Float getEcTransportMensuel() {
        return this.ecTransportMensuel;
    }

    public Prestation ecTransportMensuel(Float ecTransportMensuel) {
        this.setEcTransportMensuel(ecTransportMensuel);
        return this;
    }

    public void setEcTransportMensuel(Float ecTransportMensuel) {
        this.ecTransportMensuel = ecTransportMensuel;
    }

    public Float getEcFabMateriel() {
        return this.ecFabMateriel;
    }

    public Prestation ecFabMateriel(Float ecFabMateriel) {
        this.setEcFabMateriel(ecFabMateriel);
        return this;
    }

    public void setEcFabMateriel(Float ecFabMateriel) {
        this.ecFabMateriel = ecFabMateriel;
    }

    public Float getEcUtilMaterielMensuel() {
        return this.ecUtilMaterielMensuel;
    }

    public Prestation ecUtilMaterielMensuel(Float ecUtilMaterielMensuel) {
        this.setEcUtilMaterielMensuel(ecUtilMaterielMensuel);
        return this;
    }

    public void setEcUtilMaterielMensuel(Float ecUtilMaterielMensuel) {
        this.ecUtilMaterielMensuel = ecUtilMaterielMensuel;
    }

    public Float getEcCommMensuel() {
        return this.ecCommMensuel;
    }

    public Prestation ecCommMensuel(Float ecCommMensuel) {
        this.setEcCommMensuel(ecCommMensuel);
        return this;
    }

    public void setEcCommMensuel(Float ecCommMensuel) {
        this.ecCommMensuel = ecCommMensuel;
    }

    public Integer getNbrProfils() {
        return this.nbrProfils;
    }

    public Prestation nbrProfils(Integer nbrProfils) {
        this.setNbrProfils(nbrProfils);
        return this;
    }

    public void setNbrProfils(Integer nbrProfils) {
        this.nbrProfils = nbrProfils;
    }

    public Integer getDureeMois() {
        return this.dureeMois;
    }

    public Prestation dureeMois(Integer dureeMois) {
        this.setDureeMois(dureeMois);
        return this;
    }

    public void setDureeMois(Integer dureeMois) {
        this.dureeMois = dureeMois;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Prestation dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Prestation dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public TypePresta getTypePresta() {
        return this.typePresta;
    }

    public Prestation typePresta(TypePresta typePresta) {
        this.setTypePresta(typePresta);
        return this;
    }

    public void setTypePresta(TypePresta typePresta) {
        this.typePresta = typePresta;
    }

    public LieuPresta getLieupresta() {
        return this.lieupresta;
    }

    public Prestation lieupresta(LieuPresta lieupresta) {
        this.setLieupresta(lieupresta);
        return this;
    }

    public void setLieupresta(LieuPresta lieupresta) {
        this.lieupresta = lieupresta;
    }

    public Boolean getDonneesSaisies() {
        return this.donneesSaisies;
    }

    public Prestation donneesSaisies(Boolean donneesSaisies) {
        this.setDonneesSaisies(donneesSaisies);
        return this;
    }

    public void setDonneesSaisies(Boolean donneesSaisies) {
        this.donneesSaisies = donneesSaisies;
    }

    public Boolean getDonneesReperes() {
        return this.donneesReperes;
    }

    public Prestation donneesReperes(Boolean donneesReperes) {
        this.setDonneesReperes(donneesReperes);
        return this;
    }

    public void setDonneesReperes(Boolean donneesReperes) {
        this.donneesReperes = donneesReperes;
    }

    public Set<PrestationProfil> getPrestationProfils() {
        return this.prestationProfils;
    }

    public void setPrestationProfils(Set<PrestationProfil> prestationProfils) {
        if (this.prestationProfils != null) {
            this.prestationProfils.forEach(i -> i.removePrestation(this));
        }
        if (prestationProfils != null) {
            prestationProfils.forEach(i -> i.addPrestation(this));
        }
        this.prestationProfils = prestationProfils;
    }

    public Prestation prestationProfils(Set<PrestationProfil> prestationProfils) {
        this.setPrestationProfils(prestationProfils);
        return this;
    }

    public Prestation addPrestationProfil(PrestationProfil prestationProfil) {
        this.prestationProfils.add(prestationProfil);
        prestationProfil.getPrestations().add(this);
        return this;
    }

    public Prestation removePrestationProfil(PrestationProfil prestationProfil) {
        this.prestationProfils.remove(prestationProfil);
        prestationProfil.getPrestations().remove(this);
        return this;
    }

    public Set<TransportProfil> getTransportProfils() {
        return this.transportProfils;
    }

    public void setTransportProfils(Set<TransportProfil> transportProfils) {
        if (this.transportProfils != null) {
            this.transportProfils.forEach(i -> i.removePrestation(this));
        }
        if (transportProfils != null) {
            transportProfils.forEach(i -> i.addPrestation(this));
        }
        this.transportProfils = transportProfils;
    }

    public Prestation transportProfils(Set<TransportProfil> transportProfils) {
        this.setTransportProfils(transportProfils);
        return this;
    }

    public Prestation addTransportProfil(TransportProfil transportProfil) {
        this.transportProfils.add(transportProfil);
        transportProfil.getPrestations().add(this);
        return this;
    }

    public Prestation removeTransportProfil(TransportProfil transportProfil) {
        this.transportProfils.remove(transportProfil);
        transportProfil.getPrestations().remove(this);
        return this;
    }

    public Set<DeplacementProfil> getDeplacementProfils() {
        return this.deplacementProfils;
    }

    public void setDeplacementProfils(Set<DeplacementProfil> deplacementProfils) {
        if (this.deplacementProfils != null) {
            this.deplacementProfils.forEach(i -> i.removePrestation(this));
        }
        if (deplacementProfils != null) {
            deplacementProfils.forEach(i -> i.addPrestation(this));
        }
        this.deplacementProfils = deplacementProfils;
    }

    public Prestation deplacementProfils(Set<DeplacementProfil> deplacementProfils) {
        this.setDeplacementProfils(deplacementProfils);
        return this;
    }

    public Prestation addDeplacementProfil(DeplacementProfil deplacementProfil) {
        this.deplacementProfils.add(deplacementProfil);
        deplacementProfil.getPrestations().add(this);
        return this;
    }

    public Prestation removeDeplacementProfil(DeplacementProfil deplacementProfil) {
        this.deplacementProfils.remove(deplacementProfil);
        deplacementProfil.getPrestations().remove(this);
        return this;
    }

    public Set<MaterielProfil> getMaterielProfils() {
        return this.materielProfils;
    }

    public void setMaterielProfils(Set<MaterielProfil> materielProfils) {
        if (this.materielProfils != null) {
            this.materielProfils.forEach(i -> i.removePrestation(this));
        }
        if (materielProfils != null) {
            materielProfils.forEach(i -> i.addPrestation(this));
        }
        this.materielProfils = materielProfils;
    }

    public Prestation materielProfils(Set<MaterielProfil> materielProfils) {
        this.setMaterielProfils(materielProfils);
        return this;
    }

    public Prestation addMaterielProfil(MaterielProfil materielProfil) {
        this.materielProfils.add(materielProfil);
        materielProfil.getPrestations().add(this);
        return this;
    }

    public Prestation removeMaterielProfil(MaterielProfil materielProfil) {
        this.materielProfils.remove(materielProfil);
        materielProfil.getPrestations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prestation)) {
            return false;
        }
        return getId() != null && getId().equals(((Prestation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prestation{" +
            "id=" + getId() +
            ", nomPrestation='" + getNomPrestation() + "'" +
            ", nomUtilisateur='" + getNomUtilisateur() + "'" +
            ", nomMission='" + getNomMission() + "'" +
            ", nomClient='" + getNomClient() + "'" +
            ", ecUnite='" + getEcUnite() + "'" +
            ", ecMensuelle=" + getEcMensuelle() +
            ", ecTotale=" + getEcTotale() +
            ", ecTransportMensuel=" + getEcTransportMensuel() +
            ", ecFabMateriel=" + getEcFabMateriel() +
            ", ecUtilMaterielMensuel=" + getEcUtilMaterielMensuel() +
            ", ecCommMensuel=" + getEcCommMensuel() +
            ", nbrProfils=" + getNbrProfils() +
            ", dureeMois=" + getDureeMois() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", typePresta='" + getTypePresta() + "'" +
            ", lieupresta='" + getLieupresta() + "'" +
            ", donneesSaisies='" + getDonneesSaisies() + "'" +
            ", donneesReperes='" + getDonneesReperes() + "'" +
            "}";
    }
}
