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
 * A Boitier.
 */
@Table("boitier")
public class Boitier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("reference")
    private String reference;

    @Column("type")
    private String type;

    @Transient
    private Alerte alerte;

    @Transient
    @JsonIgnoreProperties(value = { "capteurs", "boitiers" }, allowSetters = true)
    private Set<Brache> braches = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "boitier", "parcelle" }, allowSetters = true)
    private Set<Affectation> affectations = new HashSet<>();

    @Column("alerte_id")
    private Long alerteId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Boitier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return this.reference;
    }

    public Boitier reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() {
        return this.type;
    }

    public Boitier type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Alerte getAlerte() {
        return this.alerte;
    }

    public void setAlerte(Alerte alerte) {
        this.alerte = alerte;
        this.alerteId = alerte != null ? alerte.getId() : null;
    }

    public Boitier alerte(Alerte alerte) {
        this.setAlerte(alerte);
        return this;
    }

    public Set<Brache> getBraches() {
        return this.braches;
    }

    public void setBraches(Set<Brache> braches) {
        this.braches = braches;
    }

    public Boitier braches(Set<Brache> braches) {
        this.setBraches(braches);
        return this;
    }

    public Boitier addBrache(Brache brache) {
        this.braches.add(brache);
        brache.getBoitiers().add(this);
        return this;
    }

    public Boitier removeBrache(Brache brache) {
        this.braches.remove(brache);
        brache.getBoitiers().remove(this);
        return this;
    }

    public Set<Affectation> getAffectations() {
        return this.affectations;
    }

    public void setAffectations(Set<Affectation> affectations) {
        if (this.affectations != null) {
            this.affectations.forEach(i -> i.setBoitier(null));
        }
        if (affectations != null) {
            affectations.forEach(i -> i.setBoitier(this));
        }
        this.affectations = affectations;
    }

    public Boitier affectations(Set<Affectation> affectations) {
        this.setAffectations(affectations);
        return this;
    }

    public Boitier addAffectation(Affectation affectation) {
        this.affectations.add(affectation);
        affectation.setBoitier(this);
        return this;
    }

    public Boitier removeAffectation(Affectation affectation) {
        this.affectations.remove(affectation);
        affectation.setBoitier(null);
        return this;
    }

    public Long getAlerteId() {
        return this.alerteId;
    }

    public void setAlerteId(Long alerte) {
        this.alerteId = alerte;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Boitier)) {
            return false;
        }
        return id != null && id.equals(((Boitier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Boitier{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
