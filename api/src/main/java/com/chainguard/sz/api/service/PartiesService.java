package com.chainguard.sz.api.service;

import com.chainguard.sz.api.domain.Parties;
import com.chainguard.sz.api.repository.PartiesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Parties}.
 */
@Service
@Transactional
public class PartiesService {

    private final Logger log = LoggerFactory.getLogger(PartiesService.class);

    private final PartiesRepository partiesRepository;

    public PartiesService(PartiesRepository partiesRepository) {
        this.partiesRepository = partiesRepository;
    }

    /**
     * Save a parties.
     *
     * @param parties the entity to save.
     * @return the persisted entity.
     */
    public Parties save(Parties parties) {
        log.debug("Request to save Parties : {}", parties);
        return partiesRepository.save(parties);
    }

    /**
     * Update a parties.
     *
     * @param parties the entity to save.
     * @return the persisted entity.
     */
    public Parties update(Parties parties) {
        log.debug("Request to save Parties : {}", parties);
        return partiesRepository.save(parties);
    }

    /**
     * Partially update a parties.
     *
     * @param parties the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Parties> partialUpdate(Parties parties) {
        log.debug("Request to partially update Parties : {}", parties);

        return partiesRepository
            .findById(parties.getId())
            .map(existingParties -> {
                if (parties.getPartyName() != null) {
                    existingParties.setPartyName(parties.getPartyName());
                }
                if (parties.getMainContact() != null) {
                    existingParties.setMainContact(parties.getMainContact());
                }
                if (parties.getHashAddress() != null) {
                    existingParties.setHashAddress(parties.getHashAddress());
                }
                if (parties.getTitle() != null) {
                    existingParties.setTitle(parties.getTitle());
                }
                if (parties.getContactNumber() != null) {
                    existingParties.setContactNumber(parties.getContactNumber());
                }
                if (parties.getEmail() != null) {
                    existingParties.setEmail(parties.getEmail());
                }
                if (parties.getTelephone() != null) {
                    existingParties.setTelephone(parties.getTelephone());
                }
                if (parties.getStatus() != null) {
                    existingParties.setStatus(parties.getStatus());
                }
                if (parties.getPhoneNumber() != null) {
                    existingParties.setPhoneNumber(parties.getPhoneNumber());
                }
                if (parties.getDateAdded() != null) {
                    existingParties.setDateAdded(parties.getDateAdded());
                }
                if (parties.getDateModified() != null) {
                    existingParties.setDateModified(parties.getDateModified());
                }

                return existingParties;
            })
            .map(partiesRepository::save);
    }

    /**
     * Get all the parties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Parties> findAll(Pageable pageable) {
        log.debug("Request to get all Parties");
        return partiesRepository.findAll(pageable);
    }

    /**
     * Get one parties by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Parties> findOne(Long id) {
        log.debug("Request to get Parties : {}", id);
        return partiesRepository.findById(id);
    }

    /**
     * Delete the parties by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Parties : {}", id);
        partiesRepository.deleteById(id);
    }
}
