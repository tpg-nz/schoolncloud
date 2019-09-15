package co.tpg.catalog.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A TeachingClass.
 */
@Entity
@Table(name = "teaching_class")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeachingClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "guid", nullable = false)
    private String guid;

    @Column(name = "code")
    private String code;

    @Column(name = "year")
    private Integer year;

    @Column(name = "semester")
    private Integer semester;

    @ManyToOne
    @JsonIgnoreProperties("teachingClasses")
    private Campus campus;

    @ManyToOne
    @JsonIgnoreProperties("teachingClasses")
    private Paper paper;

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

    public TeachingClass guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getCode() {
        return code;
    }

    public TeachingClass code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getYear() {
        return year;
    }

    public TeachingClass year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSemester() {
        return semester;
    }

    public TeachingClass semester(Integer semester) {
        this.semester = semester;
        return this;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Campus getCampus() {
        return campus;
    }

    public TeachingClass campus(Campus campus) {
        this.campus = campus;
        return this;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Paper getPaper() {
        return paper;
    }

    public TeachingClass paper(Paper paper) {
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
        if (!(o instanceof TeachingClass)) {
            return false;
        }
        return id != null && id.equals(((TeachingClass) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TeachingClass{" +
            "id=" + getId() +
            ", guid='" + getGuid() + "'" +
            ", code='" + getCode() + "'" +
            ", year=" + getYear() +
            ", semester=" + getSemester() +
            "}";
    }
}
