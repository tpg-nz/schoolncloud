package co.tpg.catalog.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Paper.
 */
@Entity
@Table(name = "paper")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Paper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "year")
    private Integer year;

    @Column(name = "points")
    private Integer points;

    @Column(name = "teaching_period")
    private String teachingPeriod;

    @ManyToOne
    @JsonIgnoreProperties("papers")
    private Subject subject;

    @ManyToMany(mappedBy = "papers")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<TeachingStaff> teachingStaffs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Paper code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getYear() {
        return year;
    }

    public Paper year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPoints() {
        return points;
    }

    public Paper points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getTeachingPeriod() {
        return teachingPeriod;
    }

    public Paper teachingPeriod(String teachingPeriod) {
        this.teachingPeriod = teachingPeriod;
        return this;
    }

    public void setTeachingPeriod(String teachingPeriod) {
        this.teachingPeriod = teachingPeriod;
    }

    public Subject getSubject() {
        return subject;
    }

    public Paper subject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Set<TeachingStaff> getTeachingStaffs() {
        return teachingStaffs;
    }

    public Paper teachingStaffs(Set<TeachingStaff> teachingStaffs) {
        this.teachingStaffs = teachingStaffs;
        return this;
    }

    public Paper addTeachingStaff(TeachingStaff teachingStaff) {
        this.teachingStaffs.add(teachingStaff);
        teachingStaff.getPapers().add(this);
        return this;
    }

    public Paper removeTeachingStaff(TeachingStaff teachingStaff) {
        this.teachingStaffs.remove(teachingStaff);
        teachingStaff.getPapers().remove(this);
        return this;
    }

    public void setTeachingStaffs(Set<TeachingStaff> teachingStaffs) {
        this.teachingStaffs = teachingStaffs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paper)) {
            return false;
        }
        return id != null && id.equals(((Paper) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Paper{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", year=" + getYear() +
            ", points=" + getPoints() +
            ", teachingPeriod='" + getTeachingPeriod() + "'" +
            "}";
    }
}
