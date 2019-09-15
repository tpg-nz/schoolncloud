package co.tpg.catalog.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Campus.
 */
@Entity
@Table(name = "campus")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Campus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "guid", nullable = false)
    private String guid;

    @Column(name = "name")
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("campuses")
    private EducationalInstituition educationalInstitution;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public Campus guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public Campus name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EducationalInstituition getEducationalInstitution() {
        return educationalInstitution;
    }

    public Campus educationalInstitution(EducationalInstituition educationalInstituition) {
        this.educationalInstitution = educationalInstituition;
        return this;
    }

    public void setEducationalInstitution(EducationalInstituition educationalInstituition) {
        this.educationalInstitution = educationalInstituition;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Campus)) {
            return false;
        }
        return id != null && id.equals(((Campus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Campus{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
