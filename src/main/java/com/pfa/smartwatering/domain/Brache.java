package com.pfa.smartwatering.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Brache.
 */
@Table("brache")
public class Brache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("branche")
    private Integer branche;

    @Transient
    @JsonIgnoreProperties(value = { "braches" }, allowSetters = true)
    private Set<Capteur> capteurs = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "alerte", "braches", "affectations" }, allowSetters = true)
    private Set<Boitier> boitiers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Brache id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBranche() {
        return this.branche;
    }

    public Brache branche(Integer branche) {
        this.setBranche(branche);
        return this;
    }

    public void setBranche(Integer branche) {
        this.branche = branche;
    }

    public Set<Capteur> getCapteurs() {
        return this.capteurs;
    }

    public void setCapteurs(Set<Capteur> capteurs) {
        this.capteurs = capteurs;
    }

    public Brache capteurs(Set<Capteur> capteurs) {
        this.setCapteurs(capteurs);
        return this;
    }

    public Brache addCapteur(Capteur capteur) {
        this.capteurs.add(capteur);
        capteur.getBraches().add(this);
        return this;
    }

    public Brache removeCapteur(Capteur capteur) {
        this.capteurs.remove(capteur);
        capteur.getBraches().remove(this);
        return this;
    }

    public Set<Boitier> getBoitiers() {
        return this.boitiers;
    }

    public void setBoitiers(Set<Boitier> boitiers) {
        if (this.boitiers != null) {
            this.boitiers.forEach(i -> i.removeBrache(this));
        }
        if (boitiers != null) {
            boitiers.forEach(i -> i.addBrache(this));
        }
        this.boitiers = boitiers;
    }

    public Brache boitiers(Set<Boitier> boitiers) {
        this.setBoitiers(boitiers);
        return this;
    }

    public Brache addBoitier(Boitier boitier) {
        this.boitiers.add(boitier);
        boitier.getBraches().add(this);
        return this;
    }

    public Brache removeBoitier(Boitier boitier) {
        this.boitiers.remove(boitier);
        boitier.getBraches().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Brache)) {
            return false;
        }
        return id != null && id.equals(((Brache) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Brache{" +
            "id=" + getId() +
            ", branche=" + getBranche() +
            "}";
    }
}
