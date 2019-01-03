package io.workstack.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "tech_id")
    private String techId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("projects")
    private UserPrefs userPrefs;

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Deliverable> deliverables = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTechId() {
        return techId;
    }

    public Project techId(String techId) {
        this.techId = techId;
        return this;
    }

    public void setTechId(String techId) {
        this.techId = techId;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserPrefs getUserPrefs() {
        return userPrefs;
    }

    public Project userPrefs(UserPrefs userPrefs) {
        this.userPrefs = userPrefs;
        return this;
    }

    public void setUserPrefs(UserPrefs userPrefs) {
        this.userPrefs = userPrefs;
    }

    public Set<Deliverable> getDeliverables() {
        return deliverables;
    }

    public Project deliverables(Set<Deliverable> deliverables) {
        this.deliverables = deliverables;
        return this;
    }

    public Project addDeliverable(Deliverable deliverable) {
        this.deliverables.add(deliverable);
        deliverable.setProject(this);
        return this;
    }

    public Project removeDeliverable(Deliverable deliverable) {
        this.deliverables.remove(deliverable);
        deliverable.setProject(null);
        return this;
    }

    public void setDeliverables(Set<Deliverable> deliverables) {
        this.deliverables = deliverables;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", techId='" + getTechId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
