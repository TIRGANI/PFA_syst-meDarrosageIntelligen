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
 * A Capteur.
 */
@Table("capteur")
public class Capteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("type")
    private String type;

    @Column("image")
    private String image;

    @Column("description")
    private String description;

    @Transient
    @JsonIgnoreProperties(value = { "capteurs", "boitiers" }, allowSetters = true)
    private Set<Brache> braches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Capteur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Capteur type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return this.image;
    }

    public Capteur image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return this.description;
    }

    public Capteur description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Brache> getBraches() {
        return this.braches;
    }

    public void setBraches(Set<Brache> braches) {
        if (this.braches != null) {
            this.braches.forEach(i -> i.removeCapteur(this));
        }
        if (braches != null) {
            braches.forEach(i -> i.addCapteur(this));
        }
        this.braches = braches;
    }

    public Capteur braches(Set<Brache> braches) {
        this.setBraches(braches);
        return this;
    }

    public Capteur addBrache(Brache brache) {
        this.braches.add(brache);
        brache.getCapteurs().add(this);
        return this;
    }

    public Capteur removeBrache(Brache brache) {
        this.braches.remove(brache);
        brache.getCapteurs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Capteur)) {
            return false;
        }
        return id != null && id.equals(((Capteur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Capteur{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", image='" + getImage() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
