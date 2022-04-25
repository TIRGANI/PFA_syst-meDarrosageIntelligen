package com.pfa.smartwatering.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Grandeur.
 */
@Table("grandeur")
public class Grandeur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("type")
    private String type;

    @NotNull(message = "must not be null")
    @Column("valeur")
    private String valeur;

    @Column("date")
    private String date;

    @Transient
    @JsonIgnoreProperties(
        value = { "historiques", "grandeurs", "affectations", "ferms", "plantages", "typeSol", "alertes" },
        allowSetters = true
    )
    private Parcelle parcelle;

    @Column("parcelle_id")
    private Long parcelleId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Grandeur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Grandeur type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValeur() {
        return this.valeur;
    }

    public Grandeur valeur(String valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getDate() {
        return this.date;
    }

    public Grandeur date(String date) {
        this.setDate(date);
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Parcelle getParcelle() {
        return this.parcelle;
    }

    public void setParcelle(Parcelle parcelle) {
        this.parcelle = parcelle;
        this.parcelleId = parcelle != null ? parcelle.getId() : null;
    }

    public Grandeur parcelle(Parcelle parcelle) {
        this.setParcelle(parcelle);
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
        if (!(o instanceof Grandeur)) {
            return false;
        }
        return id != null && id.equals(((Grandeur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Grandeur{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", valeur='" + getValeur() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
