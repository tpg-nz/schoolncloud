package co.tpg.catalog.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
    @Column(name = "guid", nullable = false)
    private String guid;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "graduation_type")
    private GraduationType graduationType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "teaching_staff_paper",
               joinColumns = @JoinColumn(name = "teaching_staff_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "paper_id", referencedColumnName = "id"))
    private Set<Paper> papers = new HashSet<>();

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

    public TeachingStaff guid(String guid) {
        this.guid = guid;
        return this;
    }

    public void setGuid(String guid) {
        this.guid = guid;
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

    public Set<Paper> getPapers() {
        return papers;
    }

    public TeachingStaff papers(Set<Paper> papers) {
        this.papers = papers;
        return this;
    }

    public TeachingStaff addPaper(Paper paper) {
        this.papers.add(paper);
        paper.getTeachingStaffs().add(this);
        return this;
    }

    public TeachingStaff removePaper(Paper paper) {
        this.papers.remove(paper);
        paper.getTeachingStaffs().remove(this);
        return this;
    }

    public void setPapers(Set<Paper> papers) {
        this.papers = papers;
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
            ", guid='" + getGuid() + "'" +
            ", name='" + getName() + "'" +
            ", graduationType='" + getGraduationType() + "'" +
            "}";
    }
}
