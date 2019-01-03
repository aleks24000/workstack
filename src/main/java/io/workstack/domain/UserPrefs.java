package io.workstack.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UserPrefs.
 */
@Entity
@Table(name = "user_prefs")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "userprefs")
public class UserPrefs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "login")
    private String login;

    @OneToMany(mappedBy = "userPrefs")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Project> projects = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public UserPrefs login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public UserPrefs projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public UserPrefs addProject(Project project) {
        this.projects.add(project);
        project.setUserPrefs(this);
        return this;
    }

    public UserPrefs removeProject(Project project) {
        this.projects.remove(project);
        project.setUserPrefs(null);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
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
        UserPrefs userPrefs = (UserPrefs) o;
        if (userPrefs.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userPrefs.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserPrefs{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            "}";
    }
}
