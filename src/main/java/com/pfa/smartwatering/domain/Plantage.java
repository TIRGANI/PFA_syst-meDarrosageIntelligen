package com.pfa.smartwatering.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Plantage.
 */
@Table("plantage")
public class Plantage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("date")
    private String date;

    @Column("nbr_plate")
    private Integer nbrPlate;

    @Transient
    @JsonIgnoreProperties(value = { "typePlant", "plantages" }, allowSetters = true)
    private Set<Plante> plantes = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(
        value = { "historiques", "grandeurs", "affectations", "ferms", "plantages", "typeSol", "alertes" },
        allowSetters = true
    )
    private Set<Parcelle> parcelles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Plantage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public Plantage date(String date) {
        this.setDate(date);
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getNbrPlate() {
        return this.nbrPlate;
    }

    public Plantage nbrPlate(Integer nbrPlate) {
        this.setNbrPlate(nbrPlate);
        return this;
    }

    public void setNbrPlate(Integer nbrPlate) {
        this.nbrPlate = nbrPlate;
    }

    public Set<Plante> getPlantes() {
        return this.plantes;
    }

    public void setPlantes(Set<Plante> plantes) {
        this.plantes = plantes;
    }

    public Plantage plantes(Set<Plante> plantes) {
        this.setPlantes(plantes);
        return this;
    }

    public Plantage addPlante(Plante plante) {
        this.plantes.add(plante);
        plante.getPlantages().add(this);
        return this;
    }

    public Plantage removePlante(Plante plante) {
        this.plantes.remove(plante);
        plante.getPlantages().remove(this);
        return this;
    }

    public Set<Parcelle> getParcelles() {
        return this.parcelles;
    }

    public void setParcelles(Set<Parcelle> parcelles) {
        if (this.parcelles != null) {
            this.parcelles.forEach(i -> i.removePlantage(this));
        }
        if (parcelles != null) {
            parcelles.forEach(i -> i.addPlantage(this));
        }
        this.parcelles = parcelles;
    }

    public Plantage parcelles(Set<Parcelle> parcelles) {
        this.setParcelles(parcelles);
        return this;
    }

    public Plantage addParcelle(Parcelle parcelle) {
        this.parcelles.add(parcelle);
        parcelle.getPlantages().add(this);
        return this;
    }

    public Plantage removeParcelle(Parcelle parcelle) {
        this.parcelles.remove(parcelle);
        parcelle.getPlantages().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plantage)) {
            return false;
        }
        return id != null && id.equals(((Plantage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plantage{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", nbrPlate=" + getNbrPlate() +
            "}";
    }
}
