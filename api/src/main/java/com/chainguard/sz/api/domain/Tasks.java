package com.chainguard.sz.api.domain;

import com.chainguard.sz.api.domain.enumeration.TaskStatus;
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
 * A Tasks.
 */
@Entity
@Table(name = "tasks")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tasks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "type")
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @OneToMany(mappedBy = "task")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "task" }, allowSetters = true)
    private Set<EventCards> eventCards = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "tasks" }, allowSetters = true)
    private AssessmentProjects project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tasks id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tasks name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Tasks date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return this.type;
    }

    public Tasks type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public Tasks status(TaskStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return this.assignedTo;
    }

    public Tasks assignedTo(String assignedTo) {
        this.setAssignedTo(assignedTo);
        return this;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public Tasks sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public Tasks dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public Tasks dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Set<EventCards> getEventCards() {
        return this.eventCards;
    }

    public void setEventCards(Set<EventCards> eventCards) {
        if (this.eventCards != null) {
            this.eventCards.forEach(i -> i.setTask(null));
        }
        if (eventCards != null) {
            eventCards.forEach(i -> i.setTask(this));
        }
        this.eventCards = eventCards;
    }

    public Tasks eventCards(Set<EventCards> eventCards) {
        this.setEventCards(eventCards);
        return this;
    }

    public Tasks addEventCard(EventCards eventCards) {
        this.eventCards.add(eventCards);
        eventCards.setTask(this);
        return this;
    }

    public Tasks removeEventCard(EventCards eventCards) {
        this.eventCards.remove(eventCards);
        eventCards.setTask(null);
        return this;
    }

    public AssessmentProjects getProject() {
        return this.project;
    }

    public void setProject(AssessmentProjects assessmentProjects) {
        this.project = assessmentProjects;
    }

    public Tasks project(AssessmentProjects assessmentProjects) {
        this.setProject(assessmentProjects);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tasks)) {
            return false;
        }
        return id != null && id.equals(((Tasks) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tasks{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", assignedTo='" + getAssignedTo() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
