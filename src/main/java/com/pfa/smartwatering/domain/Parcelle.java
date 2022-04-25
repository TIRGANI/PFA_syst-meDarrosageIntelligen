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
 * A Parcelle.
 */
@Table("parcelle")
public class Parcelle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("surface")
    private Float surface;

    @Column("photo")
    private String photo;

    @Transient
    @JsonIgnoreProperties(value = { "parcelle" }, allowSetters = true)
    private Set<Historique> historiques = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "parcelle" }, allowSetters = true)
    private Set<Grandeur> grandeurs = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "boitier", "parcelle" }, allowSetters = true)
    private Set<Affectation> affectations = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "parcelles" }, allowSetters = true)
    private Set<Ferm> ferms = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "plantes", "parcelles" }, allowSetters = true)
    private Set<Plantage> plantages = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "parcelles" }, allowSetters = true)
    private TypeSol typeSol;

    @Transient
    @JsonIgnoreProperties(value = { "parcelle", "boitier" }, allowSetters = true)
    private Set<Alerte> alertes = new HashSet<>();

    @Column("type_sol_id")
    private Long typeSolId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Parcelle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getSurface() {
        return this.surface;
    }

    public Parcelle surface(Float surface) {
        this.setSurface(surface);
        return this;
    }

    public void setSurface(Float surface) {
        this.surface = surface;
    }

    public String getPhoto() {
        return this.photo;
    }

    public Parcelle photo(String photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Set<Historique> getHistoriques() {
        return this.historiques;
    }

    public void setHistoriques(Set<Historique> historiques) {
        if (this.historiques != null) {
            this.historiques.forEach(i -> i.setParcelle(null));
        }
        if (historiques != null) {
            historiques.forEach(i -> i.setParcelle(this));
        }
        this.historiques = historiques;
    }

    public Parcelle historiques(Set<Historique> historiques) {
        this.setHistoriques(historiques);
        return this;
    }

    public Parcelle addHistorique(Historique historique) {
        this.historiques.add(historique);
        historique.setParcelle(this);
        return this;
    }

    public Parcelle removeHistorique(Historique historique) {
        this.historiques.remove(historique);
        historique.setParcelle(null);
        return this;
    }

    public Set<Grandeur> getGrandeurs() {
        return this.grandeurs;
    }

    public void setGrandeurs(Set<Grandeur> grandeurs) {
        if (this.grandeurs != null) {
            this.grandeurs.forEach(i -> i.setParcelle(null));
        }
        if (grandeurs != null) {
            grandeurs.forEach(i -> i.setParcelle(this));
        }
        this.grandeurs = grandeurs;
    }

    public Parcelle grandeurs(Set<Grandeur> grandeurs) {
        this.setGrandeurs(grandeurs);
        return this;
    }

    public Parcelle addGrandeur(Grandeur grandeur) {
        this.grandeurs.add(grandeur);
        grandeur.setParcelle(this);
        return this;
    }

    public Parcelle removeGrandeur(Grandeur grandeur) {
        this.grandeurs.remove(grandeur);
        grandeur.setParcelle(null);
        return this;
    }

    public Set<Affectation> getAffectations() {
        return this.affectations;
    }

    public void setAffectations(Set<Affectation> affectations) {
        if (this.affectations != null) {
            this.affectations.forEach(i -> i.setParcelle(null));
        }
        if (affectations != null) {
            affectations.forEach(i -> i.setParcelle(this));
        }
        this.affectations = affectations;
    }

    public Parcelle affectations(Set<Affectation> affectations) {
        this.setAffectations(affectations);
        return this;
    }

    public Parcelle addAffectation(Affectation affectation) {
        this.affectations.add(affectation);
        affectation.setParcelle(this);
        return this;
    }

    public Parcelle removeAffectation(Affectation affectation) {
        this.affectations.remove(affectation);
        affectation.setParcelle(null);
        return this;
    }

    public Set<Ferm> getFerms() {
        return this.ferms;
    }

    public void setFerms(Set<Ferm> ferms) {
        this.ferms = ferms;
    }

    public Parcelle ferms(Set<Ferm> ferms) {
        this.setFerms(ferms);
        return this;
    }

    public Parcelle addFerm(Ferm ferm) {
        this.ferms.add(ferm);
        ferm.getParcelles().add(this);
        return this;
    }

    public Parcelle removeFerm(Ferm ferm) {
        this.ferms.remove(ferm);
        ferm.getParcelles().remove(this);
        return this;
    }

    public Set<Plantage> getPlantages() {
        return this.plantages;
    }

    public void setPlantages(Set<Plantage> plantages) {
        this.plantages = plantages;
    }

    public Parcelle plantages(Set<Plantage> plantages) {
        this.setPlantages(plantages);
        return this;
    }

    public Parcelle addPlantage(Plantage plantage) {
        this.plantages.add(plantage);
        plantage.getParcelles().add(this);
        return this;
    }

    public Parcelle removePlantage(Plantage plantage) {
        this.plantages.remove(plantage);
        plantage.getParcelles().remove(this);
        return this;
    }

    public TypeSol getTypeSol() {
        return this.typeSol;
    }

    public void setTypeSol(TypeSol typeSol) {
        this.typeSol = typeSol;
        this.typeSolId = typeSol != null ? typeSol.getId() : null;
    }

    public Parcelle typeSol(TypeSol typeSol) {
        this.setTypeSol(typeSol);
        return this;
    }

    public Set<Alerte> getAlertes() {
        return this.alertes;
    }

    public void setAlertes(Set<Alerte> alertes) {
        if (this.alertes != null) {
            this.alertes.forEach(i -> i.setParcelle(null));
        }
        if (alertes != null) {
            alertes.forEach(i -> i.setParcelle(this));
        }
        this.alertes = alertes;
    }

    public Parcelle alertes(Set<Alerte> alertes) {
        this.setAlertes(alertes);
        return this;
    }

    public Parcelle addAlerte(Alerte alerte) {
        this.alertes.add(alerte);
        alerte.setParcelle(this);
        return this;
    }

    public Parcelle removeAlerte(Alerte alerte) {
        this.alertes.remove(alerte);
        alerte.setParcelle(null);
        return this;
    }

    public Long getTypeSolId() {
        return this.typeSolId;
    }

    public void setTypeSolId(Long typeSol) {
        this.typeSolId = typeSol;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parcelle)) {
            return false;
        }
        return id != null && id.equals(((Parcelle) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parcelle{" +
            "id=" + getId() +
            ", surface=" + getSurface() +
            ", photo='" + getPhoto() + "'" +
            "}";
    }
}
