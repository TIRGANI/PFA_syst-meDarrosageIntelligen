package com.pfa.smartwatering.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Historique.
 */
@Table("historique")
public class Historique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("date_arosage")
    private String dateArosage;

    @Column("qtt_eau")
    private Integer qttEau;

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

    public Historique id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateArosage() {
        return this.dateArosage;
    }

    public Historique dateArosage(String dateArosage) {
        this.setDateArosage(dateArosage);
        return this;
    }

    public void setDateArosage(String dateArosage) {
        this.dateArosage = dateArosage;
    }

    public Integer getQttEau() {
        return this.qttEau;
    }

    public Historique qttEau(Integer qttEau) {
        this.setQttEau(qttEau);
        return this;
    }

    public void setQttEau(Integer qttEau) {
        this.qttEau = qttEau;
    }

    public Parcelle getParcelle() {
        return this.parcelle;
    }

    public void setParcelle(Parcelle parcelle) {
        this.parcelle = parcelle;
        this.parcelleId = parcelle != null ? parcelle.getId() : null;
    }

    public Historique parcelle(Parcelle parcelle) {
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
        if (!(o instanceof Historique)) {
            return false;
        }
        return id != null && id.equals(((Historique) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Historique{" +
            "id=" + getId() +
            ", dateArosage='" + getDateArosage() + "'" +
            ", qttEau=" + getQttEau() +
            "}";
    }
}
