package com.chainguard.sz.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AssessmentProjects.
 */
@Entity
@Table(name = "assessment_projects")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AssessmentProjects implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "project_name", nullable = false)
    private String projectName;

    @NotNull
    @Column(name = "priority", nullable = false)
    private String priority;

    @NotNull
    @Column(name = "project_manager", nullable = false)
    private String projectManager;

    @Column(name = "est_start_date")
    private LocalDate estStartDate;

    @Column(name = "est_end_date")
    private LocalDate estEndDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @OneToMany(mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "eventCards", "project" }, allowSetters = true)
    private Set<Tasks> tasks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AssessmentProjects id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public AssessmentProjects projectName(String projectName) {
        this.setProjectName(projectName);
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPriority() {
        return this.priority;
    }

    public AssessmentProjects priority(String priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getProjectManager() {
        return this.projectManager;
    }

    public AssessmentProjects projectManager(String projectManager) {
        this.setProjectManager(projectManager);
        return this;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public LocalDate getEstStartDate() {
        return this.estStartDate;
    }

    public AssessmentProjects estStartDate(LocalDate estStartDate) {
        this.setEstStartDate(estStartDate);
        return this;
    }

    public void setEstStartDate(LocalDate estStartDate) {
        this.estStartDate = estStartDate;
    }

    public LocalDate getEstEndDate() {
        return this.estEndDate;
    }

    public AssessmentProjects estEndDate(LocalDate estEndDate) {
        this.setEstEndDate(estEndDate);
        return this;
    }

    public void setEstEndDate(LocalDate estEndDate) {
        this.estEndDate = estEndDate;
    }

    public LocalDate getActualStartDate() {
        return this.actualStartDate;
    }

    public AssessmentProjects actualStartDate(LocalDate actualStartDate) {
        this.setActualStartDate(actualStartDate);
        return this;
    }

    public void setActualStartDate(LocalDate actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public LocalDate getActualEndDate() {
        return this.actualEndDate;
    }

    public AssessmentProjects actualEndDate(LocalDate actualEndDate) {
        this.setActualEndDate(actualEndDate);
        return this;
    }

    public void setActualEndDate(LocalDate actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public String getDescription() {
        return this.description;
    }

    public AssessmentProjects description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public AssessmentProjects sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public AssessmentProjects dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public AssessmentProjects dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Set<Tasks> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Tasks> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setProject(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setProject(this));
        }
        this.tasks = tasks;
    }

    public AssessmentProjects tasks(Set<Tasks> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public AssessmentProjects addTask(Tasks tasks) {
        this.tasks.add(tasks);
        tasks.setProject(this);
        return this;
    }

    public AssessmentProjects removeTask(Tasks tasks) {
        this.tasks.remove(tasks);
        tasks.setProject(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssessmentProjects)) {
            return false;
        }
        return id != null && id.equals(((AssessmentProjects) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssessmentProjects{" +
            "id=" + getId() +
            ", projectName='" + getProjectName() + "'" +
            ", priority='" + getPriority() + "'" +
            ", projectManager='" + getProjectManager() + "'" +
            ", estStartDate='" + getEstStartDate() + "'" +
            ", estEndDate='" + getEstEndDate() + "'" +
            ", actualStartDate='" + getActualStartDate() + "'" +
            ", actualEndDate='" + getActualEndDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
