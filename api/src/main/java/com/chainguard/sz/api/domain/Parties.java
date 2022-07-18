package com.chainguard.sz.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Parties.
 */
@Entity
@Table(name = "parties")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Parties implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "party_name")
    private String partyName;

    @Column(name = "main_contact")
    private String mainContact;

    @Column(name = "hash_address")
    private String hashAddress;

    @Column(name = "title")
    private String title;

    @Column(name = "contact_number", precision = 21, scale = 2)
    private BigDecimal contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "status")
    private String status;

    @Column(name = "phone_number")
    private Integer phoneNumber;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "date_modified")
    private LocalDate dateModified;

    @OneToMany(mappedBy = "party")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "party" }, allowSetters = true)
    private Set<UserRecords> userRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Parties id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartyName() {
        return this.partyName;
    }

    public Parties partyName(String partyName) {
        this.setPartyName(partyName);
        return this;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getMainContact() {
        return this.mainContact;
    }

    public Parties mainContact(String mainContact) {
        this.setMainContact(mainContact);
        return this;
    }

    public void setMainContact(String mainContact) {
        this.mainContact = mainContact;
    }

    public String getHashAddress() {
        return this.hashAddress;
    }

    public Parties hashAddress(String hashAddress) {
        this.setHashAddress(hashAddress);
        return this;
    }

    public void setHashAddress(String hashAddress) {
        this.hashAddress = hashAddress;
    }

    public String getTitle() {
        return this.title;
    }

    public Parties title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getContactNumber() {
        return this.contactNumber;
    }

    public Parties contactNumber(BigDecimal contactNumber) {
        this.setContactNumber(contactNumber);
        return this;
    }

    public void setContactNumber(BigDecimal contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Parties email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Parties telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getStatus() {
        return this.status;
    }

    public Parties status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPhoneNumber() {
        return this.phoneNumber;
    }

    public Parties phoneNumber(Integer phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateAdded() {
        return this.dateAdded;
    }

    public Parties dateAdded(LocalDate dateAdded) {
        this.setDateAdded(dateAdded);
        return this;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDateModified() {
        return this.dateModified;
    }

    public Parties dateModified(LocalDate dateModified) {
        this.setDateModified(dateModified);
        return this;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Set<UserRecords> getUserRecords() {
        return this.userRecords;
    }

    public void setUserRecords(Set<UserRecords> userRecords) {
        if (this.userRecords != null) {
            this.userRecords.forEach(i -> i.setParty(null));
        }
        if (userRecords != null) {
            userRecords.forEach(i -> i.setParty(this));
        }
        this.userRecords = userRecords;
    }

    public Parties userRecords(Set<UserRecords> userRecords) {
        this.setUserRecords(userRecords);
        return this;
    }

    public Parties addUserRecord(UserRecords userRecords) {
        this.userRecords.add(userRecords);
        userRecords.setParty(this);
        return this;
    }

    public Parties removeUserRecord(UserRecords userRecords) {
        this.userRecords.remove(userRecords);
        userRecords.setParty(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parties)) {
            return false;
        }
        return id != null && id.equals(((Parties) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parties{" +
            "id=" + getId() +
            ", partyName='" + getPartyName() + "'" +
            ", mainContact='" + getMainContact() + "'" +
            ", hashAddress='" + getHashAddress() + "'" +
            ", title='" + getTitle() + "'" +
            ", contactNumber=" + getContactNumber() +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", status='" + getStatus() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", dateAdded='" + getDateAdded() + "'" +
            ", dateModified='" + getDateModified() + "'" +
            "}";
    }
}
