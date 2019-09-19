package co.tpg.catalog.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import co.tpg.catalog.domain.enumeration.GraduationType;

/**
 * A TeachingStaff.
 */
@Entity
@Table(name = "teaching_staff")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachingStaff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "graduation_type")
    private GraduationType graduationType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("teachingStaffs")
    private Paper paper;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TeachingStaff name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GraduationType getGraduationType() {
        return graduationType;
    }

    public TeachingStaff graduationType(GraduationType graduationType) {
        this.graduationType = graduationType;
        return this;
    }

    public void setGraduationType(GraduationType graduationType) {
        this.graduationType = graduationType;
    }

    public Paper getPaper() {
        return paper;
    }

    public TeachingStaff paper(Paper paper) {
        this.paper = paper;
        return this;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeachingStaff)) {
            return false;
        }
        return id != null && id.equals(((TeachingStaff) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TeachingStaff{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", graduationType='" + getGraduationType() + "'" +
            "}";
    }
}
