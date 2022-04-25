package com.pfa.smartwatering.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Affectation.
 */
@Table("affectation")
public class Affectation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("date_debut")
    private String dateDebut;

    @Column("date_fin")
    private String dateFin;

    @Transient
    @JsonIgnoreProperties(value = { "alerte", "braches", "affectations" }, allowSetters = true)
    private Boitier boitier;

    @Transient
    @JsonIgnoreProperties(
        value = { "historiques", "grandeurs", "affectations", "ferms", "plantages", "typeSol", "alertes" },
        allowSetters = true
    )
    private Parcelle parcelle;

    @Column("boitier_id")
    private Long boitierId;

    @Column("parcelle_id")
    private Long parcelleId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Affectation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateDebut() {
        return this.dateDebut;
    }

    public Affectation dateDebut(String dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return this.dateFin;
    }

    public Affectation dateFin(String dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public Boitier getBoitier() {
        return this.boitier;
    }

    public void setBoitier(Boitier boitier) {
        this.boitier = boitier;
        this.boitierId = boitier != null ? boitier.getId() : null;
    }

    public Affectation boitier(Boitier boitier) {
        this.setBoitier(boitier);
        return this;
    }

    public Parcelle getParcelle() {
        return this.parcelle;
    }

    public void setParcelle(Parcelle parcelle) {
        this.parcelle = parcelle;
        this.parcelleId = parcelle != null ? parcelle.getId() : null;
    }

    public Affectation parcelle(Parcelle parcelle) {
        this.setParcelle(parcelle);
        return this;
    }

    public Long getBoitierId() {
        return this.boitierId;
    }

    public void setBoitierId(Long boitier) {
        this.boitierId = boitier;
    }

    public Long getParcelleId() {
        return this.parcelleId;
    }

    public void setParcelleId(Long parcelle) {
        this.parcelleId = parcelle;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Affectation)) {
            return false;
        }
        return id != null && id.equals(((Affectation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Affectation{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
