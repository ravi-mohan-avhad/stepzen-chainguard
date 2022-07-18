package com.chainguard.sz.api.domain;

import com.chainguard.sz.api.domain.enumeration.AccountStatus;
import com.chainguard.sz.api.domain.enumeration.UserCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserRecords.
 */
@Entity
@Table(name = "user_records")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "title")
    private String title;

    @Column(name = "contact_number", precision = 21, scale = 2)
    private BigDecimal contactNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private UserCategory category;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "userRecords" }, allowSetters = true)
    private Parties party;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserRecords id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UserRecords name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public UserRecords email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return this.title;
    }

    public UserRecords title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getContactNumber() {
        return this.contactNumber;
    }

    public UserRecords contactNumber(BigDecimal contactNumber) {
        this.setContactNumber(contactNumber);
        return this;
    }

    public void setContactNumber(BigDecimal contactNumber) {
        this.contactNumber = contactNumber;
    }

    public UserCategory getCategory() {
        return this.category;
    }

    public UserRecords category(UserCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(UserCategory category) {
        this.category = category;
    }

    public Integer getSortOrder() {
        return this.sortOrder;
    }

    public UserRecords sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public UserRecords dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public UserRecords dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public AccountStatus getStatus() {
        return this.status;
    }

    public UserRecords status(AccountStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Parties getParty() {
        return this.party;
    }

    public void setParty(Parties parties) {
        this.party = parties;
    }

    public UserRecords party(Parties parties) {
        this.setParty(parties);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRecords)) {
            return false;
        }
        return id != null && id.equals(((UserRecords) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRecords{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", title='" + getTitle() + "'" +
            ", contactNumber=" + getContactNumber() +
            ", category='" + getCategory() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
