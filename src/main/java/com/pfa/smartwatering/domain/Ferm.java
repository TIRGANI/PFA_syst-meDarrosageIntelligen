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
 * A Ferm.
 */
@Table("ferm")
public class Ferm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("num_parcelle")
    private Integer numParcelle;

    @Column("photo")
    private String photo;

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

    public Ferm id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumParcelle() {
        return this.numParcelle;
    }

    public Ferm numParcelle(Integer numParcelle) {
        this.setNumParcelle(numParcelle);
        return this;
    }

    public void setNumParcelle(Integer numParcelle) {
        this.numParcelle = numParcelle;
    }

    public String getPhoto() {
        return this.photo;
    }

    public Ferm photo(String photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Set<Parcelle> getParcelles() {
        return this.parcelles;
    }

    public void setParcelles(Set<Parcelle> parcelles) {
        if (this.parcelles != null) {
            this.parcelles.forEach(i -> i.removeFerm(this));
        }
        if (parcelles != null) {
            parcelles.forEach(i -> i.addFerm(this));
        }
        this.parcelles = parcelles;
    }

    public Ferm parcelles(Set<Parcelle> parcelles) {
        this.setParcelles(parcelles);
        return this;
    }

    public Ferm addParcelle(Parcelle parcelle) {
        this.parcelles.add(parcelle);
        parcelle.getFerms().add(this);
        return this;
    }

    public Ferm removeParcelle(Parcelle parcelle) {
        this.parcelles.remove(parcelle);
        parcelle.getFerms().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ferm)) {
            return false;
        }
        return id != null && id.equals(((Ferm) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ferm{" +
            "id=" + getId() +
            ", numParcelle=" + getNumParcelle() +
            ", photo='" + getPhoto() + "'" +
            "}";
    }
}
