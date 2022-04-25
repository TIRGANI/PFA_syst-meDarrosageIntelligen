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
 * A TypeSol.
 */
@Table("type_sol")
public class TypeSol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("libelle")
    private Integer libelle;

    @Column("type")
    private String type;

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

    public TypeSol id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLibelle() {
        return this.libelle;
    }

    public TypeSol libelle(Integer libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(Integer libelle) {
        this.libelle = libelle;
    }

    public String getType() {
        return this.type;
    }

    public TypeSol type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Parcelle> getParcelles() {
        return this.parcelles;
    }

    public void setParcelles(Set<Parcelle> parcelles) {
        if (this.parcelles != null) {
            this.parcelles.forEach(i -> i.setTypeSol(null));
        }
        if (parcelles != null) {
            parcelles.forEach(i -> i.setTypeSol(this));
        }
        this.parcelles = parcelles;
    }

    public TypeSol parcelles(Set<Parcelle> parcelles) {
        this.setParcelles(parcelles);
        return this;
    }

    public TypeSol addParcelle(Parcelle parcelle) {
        this.parcelles.add(parcelle);
        parcelle.setTypeSol(this);
        return this;
    }

    public TypeSol removeParcelle(Parcelle parcelle) {
        this.parcelles.remove(parcelle);
        parcelle.setTypeSol(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeSol)) {
            return false;
        }
        return id != null && id.equals(((TypeSol) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeSol{" +
            "id=" + getId() +
            ", libelle=" + getLibelle() +
            ", type='" + getType() + "'" +
            "}";
    }
}
