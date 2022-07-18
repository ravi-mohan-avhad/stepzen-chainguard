package com.chainguard.sz.api.domain;

import com.chainguard.sz.api.domain.enumeration.EventCardStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventCards.
 */
@Entity
@Table(name = "event_cards")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventCards implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "user_reporting_count")
    private Integer userReportingCount;

    @Column(name = "event_cards_repition")
    private Integer eventCardsRepition;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventCardStatus status;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @ManyToOne
    @JsonIgnoreProperties(value = { "eventCards", "project" }, allowSetters = true)
    private Tasks task;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventCards id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEventDate() {
        return this.eventDate;
    }

    public EventCards eventDate(LocalDate eventDate) {
        this.setEventDate(eventDate);
        return this;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getUserReportingCount() {
        return this.userReportingCount;
    }

    public EventCards userReportingCount(Integer userReportingCount) {
        this.setUserReportingCount(userReportingCount);
        return this;
    }

    public void setUserReportingCount(Integer userReportingCount) {
        this.userReportingCount = userReportingCount;
    }

    public Integer getEventCardsRepition() {
        return this.eventCardsRepition;
    }

    public EventCards eventCardsRepition(Integer eventCardsRepition) {
        this.setEventCardsRepition(eventCardsRepition);
        return this;
    }

    public void setEventCardsRepition(Integer eventCardsRepition) {
        this.eventCardsRepition = eventCardsRepition;
    }

    public EventCardStatus getStatus() {
        return this.status;
    }

    public EventCards status(EventCardStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(EventCardStatus status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public EventCards sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public EventCards transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public EventCards dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Tasks getTask() {
        return this.task;
    }

    public void setTask(Tasks tasks) {
        this.task = tasks;
    }

    public EventCards task(Tasks tasks) {
        this.setTask(tasks);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCards)) {
            return false;
        }
        return id != null && id.equals(((EventCards) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCards{" +
            "id=" + getId() +
            ", eventDate='" + getEventDate() + "'" +
            ", userReportingCount=" + getUserReportingCount() +
            ", eventCardsRepition=" + getEventCardsRepition() +
            ", status='" + getStatus() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
