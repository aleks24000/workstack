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
 * A Deliverable.
 */
@Entity
@Table(name = "deliverable")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "deliverable")
public class Deliverable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "tech_id")
    private String techId;

    @Column(name = "name")
    private String name;

    @Column(name = "spent_time")
    private Long spentTime;

    @ManyToOne
    @JsonIgnoreProperties("deliverables")
    private Project project;

    @OneToMany(mappedBy = "deliverable")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> tasks = new HashSet<>();
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

    public Deliverable techId(String techId) {
        this.techId = techId;
        return this;
    }

    public void setTechId(String techId) {
        this.techId = techId;
    }

    public String getName() {
        return name;
    }

    public Deliverable name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSpentTime() {
        return spentTime;
    }

    public Deliverable spentTime(Long spentTime) {
        this.spentTime = spentTime;
        return this;
    }

    public void setSpentTime(Long spentTime) {
        this.spentTime = spentTime;
    }

    public Project getProject() {
        return project;
    }

    public Deliverable project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Deliverable tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Deliverable addTask(Task task) {
        this.tasks.add(task);
        task.setDeliverable(this);
        return this;
    }

    public Deliverable removeTask(Task task) {
        this.tasks.remove(task);
        task.setDeliverable(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
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
        Deliverable deliverable = (Deliverable) o;
        if (deliverable.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deliverable.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Deliverable{" +
            "id=" + getId() +
            ", techId='" + getTechId() + "'" +
            ", name='" + getName() + "'" +
            ", spentTime=" + getSpentTime() +
            "}";
    }
}
