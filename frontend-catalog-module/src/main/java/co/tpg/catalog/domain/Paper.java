package co.tpg.catalog.domain;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

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

    @NotNull
    @Min(value = 0)
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @Min(value = 0)
    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "teaching_period")
    private String teachingPeriod;

    @ManyToOne(optional = false)
    @NotNull
    @JsonProperty("subject")
    private Subject subject;

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

    @JsonGetter("subject")
    public Subject getSubject() {
        return subject;
    }

    public Paper subject(Subject subject) {
        this.subject = subject;
        return this;
    }

    @JsonSetter("subject")
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paper paper = (Paper) o;
        return id.equals(paper.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Paper{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", year=" + year +
            ", points=" + points +
            ", teachingPeriod='" + teachingPeriod + '\'' +
            ", subject=" + subject +
            '}';
    }
}
