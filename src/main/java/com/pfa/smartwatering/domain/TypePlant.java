package com.pfa.smartwatering.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A TypePlant.
 */
@Table("type_plant")
public class TypePlant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("lebelle")
    private String lebelle;

    @NotNull(message = "must not be null")
    @Column("humidite_max")
    private Float humiditeMax;

    @NotNull(message = "must not be null")
    @Column("humidite_min")
    private Float humiditeMin;

    @NotNull(message = "must not be null")
    @Column("temperature")
    private Float temperature;

    @NotNull(message = "must not be null")
    @Column("luminisite")
    private Float luminisite;

    @Transient
    private Plante plante;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypePlant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLebelle() {
        return this.lebelle;
    }

    public TypePlant lebelle(String lebelle) {
        this.setLebelle(lebelle);
        return this;
    }

    public void setLebelle(String lebelle) {
        this.lebelle = lebelle;
    }

    public Float getHumiditeMax() {
        return this.humiditeMax;
    }

    public TypePlant humiditeMax(Float humiditeMax) {
        this.setHumiditeMax(humiditeMax);
        return this;
    }

    public void setHumiditeMax(Float humiditeMax) {
        this.humiditeMax = humiditeMax;
    }

    public Float getHumiditeMin() {
        return this.humiditeMin;
    }

    public TypePlant humiditeMin(Float humiditeMin) {
        this.setHumiditeMin(humiditeMin);
        return this;
    }

    public void setHumiditeMin(Float humiditeMin) {
        this.humiditeMin = humiditeMin;
    }

    public Float getTemperature() {
        return this.temperature;
    }

    public TypePlant temperature(Float temperature) {
        this.setTemperature(temperature);
        return this;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getLuminisite() {
        return this.luminisite;
    }

    public TypePlant luminisite(Float luminisite) {
        this.setLuminisite(luminisite);
        return this;
    }

    public void setLuminisite(Float luminisite) {
        this.luminisite = luminisite;
    }

    public Plante getPlante() {
        return this.plante;
    }

    public void setPlante(Plante plante) {
        if (this.plante != null) {
            this.plante.setTypePlant(null);
        }
        if (plante != null) {
            plante.setTypePlant(this);
        }
        this.plante = plante;
    }

    public TypePlant plante(Plante plante) {
        this.setPlante(plante);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypePlant)) {
            return false;
        }
        return id != null && id.equals(((TypePlant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypePlant{" +
            "id=" + getId() +
            ", lebelle='" + getLebelle() + "'" +
            ", humiditeMax=" + getHumiditeMax() +
            ", humiditeMin=" + getHumiditeMin() +
            ", temperature=" + getTemperature() +
            ", luminisite=" + getLuminisite() +
            "}";
    }
}
