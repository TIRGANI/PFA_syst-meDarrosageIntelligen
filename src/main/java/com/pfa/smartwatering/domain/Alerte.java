package com.pfa.smartwatering.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Alerte.
 */
@Table("alerte")
public class Alerte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("humidite")
    private Float humidite;

    @Column("temperature")
    private Float temperature;

    @Column("luminosite")
    private Float luminosite;

    @Transient
    @JsonIgnoreProperties(
        value = { "historiques", "grandeurs", "affectations", "ferms", "plantages", "typeSol", "alertes" },
        allowSetters = true
    )
    private Parcelle parcelle;

    @Transient
    private Boitier boitier;

    @Column("parcelle_id")
    private Long parcelleId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alerte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getHumidite() {
        return this.humidite;
    }

    public Alerte humidite(Float humidite) {
        this.setHumidite(humidite);
        return this;
    }

    public void setHumidite(Float humidite) {
        this.humidite = humidite;
    }

    public Float getTemperature() {
        return this.temperature;
    }

    public Alerte temperature(Float temperature) {
        this.setTemperature(temperature);
        return this;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getLuminosite() {
        return this.luminosite;
    }

    public Alerte luminosite(Float luminosite) {
        this.setLuminosite(luminosite);
        return this;
    }

    public void setLuminosite(Float luminosite) {
        this.luminosite = luminosite;
    }

    public Parcelle getParcelle() {
        return this.parcelle;
    }

    public void setParcelle(Parcelle parcelle) {
        this.parcelle = parcelle;
        this.parcelleId = parcelle != null ? parcelle.getId() : null;
    }

    public Alerte parcelle(Parcelle parcelle) {
        this.setParcelle(parcelle);
        return this;
    }

    public Boitier getBoitier() {
        return this.boitier;
    }

    public void setBoitier(Boitier boitier) {
        if (this.boitier != null) {
            this.boitier.setAlerte(null);
        }
        if (boitier != null) {
            boitier.setAlerte(this);
        }
        this.boitier = boitier;
    }

    public Alerte boitier(Boitier boitier) {
        this.setBoitier(boitier);
        return this;
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
        if (!(o instanceof Alerte)) {
            return false;
        }
        return id != null && id.equals(((Alerte) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alerte{" +
            "id=" + getId() +
            ", humidite=" + getHumidite() +
            ", temperature=" + getTemperature() +
            ", luminosite=" + getLuminosite() +
            "}";
    }
}
