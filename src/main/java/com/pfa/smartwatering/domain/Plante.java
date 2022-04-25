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
 * A Plante.
 */
@Table("plante")
public class Plante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("lebelle")
    private String lebelle;

    @Column("photo")
    private String photo;

    @Column("racin")
    private String racin;

    @Transient
    private TypePlant typePlant;

    @Transient
    @JsonIgnoreProperties(value = { "plantes", "parcelles" }, allowSetters = true)
    private Set<Plantage> plantages = new HashSet<>();

    @Column("type_plant_id")
    private Long typePlantId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Plante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLebelle() {
        return this.lebelle;
    }

    public Plante lebelle(String lebelle) {
        this.setLebelle(lebelle);
        return this;
    }

    public void setLebelle(String lebelle) {
        this.lebelle = lebelle;
    }

    public String getPhoto() {
        return this.photo;
    }

    public Plante photo(String photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRacin() {
        return this.racin;
    }

    public Plante racin(String racin) {
        this.setRacin(racin);
        return this;
    }

    public void setRacin(String racin) {
        this.racin = racin;
    }

    public TypePlant getTypePlant() {
        return this.typePlant;
    }

    public void setTypePlant(TypePlant typePlant) {
        this.typePlant = typePlant;
        this.typePlantId = typePlant != null ? typePlant.getId() : null;
    }

    public Plante typePlant(TypePlant typePlant) {
        this.setTypePlant(typePlant);
        return this;
    }

    public Set<Plantage> getPlantages() {
        return this.plantages;
    }

    public void setPlantages(Set<Plantage> plantages) {
        if (this.plantages != null) {
            this.plantages.forEach(i -> i.removePlante(this));
        }
        if (plantages != null) {
            plantages.forEach(i -> i.addPlante(this));
        }
        this.plantages = plantages;
    }

    public Plante plantages(Set<Plantage> plantages) {
        this.setPlantages(plantages);
        return this;
    }

    public Plante addPlantage(Plantage plantage) {
        this.plantages.add(plantage);
        plantage.getPlantes().add(this);
        return this;
    }

    public Plante removePlantage(Plantage plantage) {
        this.plantages.remove(plantage);
        plantage.getPlantes().remove(this);
        return this;
    }

    public Long getTypePlantId() {
        return this.typePlantId;
    }

    public void setTypePlantId(Long typePlant) {
        this.typePlantId = typePlant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plante)) {
            return false;
        }
        return id != null && id.equals(((Plante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plante{" +
            "id=" + getId() +
            ", lebelle='" + getLebelle() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", racin='" + getRacin() + "'" +
            "}";
    }
}
