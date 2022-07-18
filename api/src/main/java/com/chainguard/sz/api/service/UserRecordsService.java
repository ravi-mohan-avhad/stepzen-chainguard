package com.chainguard.sz.api.service;

import com.chainguard.sz.api.domain.UserRecords;
import com.chainguard.sz.api.repository.UserRecordsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserRecords}.
 */
@Service
@Transactional
public class UserRecordsService {

    private final Logger log = LoggerFactory.getLogger(UserRecordsService.class);

    private final UserRecordsRepository userRecordsRepository;

    public UserRecordsService(UserRecordsRepository userRecordsRepository) {
        this.userRecordsRepository = userRecordsRepository;
    }

    /**
     * Save a userRecords.
     *
     * @param userRecords the entity to save.
     * @return the persisted entity.
     */
    public UserRecords save(UserRecords userRecords) {
        log.debug("Request to save UserRecords : {}", userRecords);
        return userRecordsRepository.save(userRecords);
    }

    /**
     * Update a userRecords.
     *
     * @param userRecords the entity to save.
     * @return the persisted entity.
     */
    public UserRecords update(UserRecords userRecords) {
        log.debug("Request to save UserRecords : {}", userRecords);
        return userRecordsRepository.save(userRecords);
    }

    /**
     * Partially update a userRecords.
     *
     * @param userRecords the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserRecords> partialUpdate(UserRecords userRecords) {
        log.debug("Request to partially update UserRecords : {}", userRecords);

        return userRecordsRepository
            .findById(userRecords.getId())
            .map(existingUserRecords -> {
                if (userRecords.getName() != null) {
                    existingUserRecords.setName(userRecords.getName());
                }
                if (userRecords.getEmail() != null) {
                    existingUserRecords.setEmail(userRecords.getEmail());
                }
                if (userRecords.getTitle() != null) {
                    existingUserRecords.setTitle(userRecords.getTitle());
                }
                if (userRecords.getContactNumber() != null) {
                    existingUserRecords.setContactNumber(userRecords.getContactNumber());
                }
                if (userRecords.getCategory() != null) {
                    existingUserRecords.setCategory(userRecords.getCategory());
                }
                if (userRecords.getSortOrder() != null) {
                    existingUserRecords.setSortOrder(userRecords.getSortOrder());
                }
                if (userRecords.getDateAdded() != null) {
                    existingUserRecords.setDateAdded(userRecords.getDateAdded());
                }
                if (userRecords.getDateModified() != null) {
                    existingUserRecords.setDateModified(userRecords.getDateModified());
                }
                if (userRecords.getStatus() != null) {
                    existingUserRecords.setStatus(userRecords.getStatus());
                }

                return existingUserRecords;
            })
            .map(userRecordsRepository::save);
    }

    /**
     * Get all the userRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserRecords> findAll(Pageable pageable) {
        log.debug("Request to get all UserRecords");
        return userRecordsRepository.findAll(pageable);
    }

    /**
     * Get one userRecords by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserRecords> findOne(Long id) {
        log.debug("Request to get UserRecords : {}", id);
        return userRecordsRepository.findById(id);
    }

    /**
     * Delete the userRecords by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserRecords : {}", id);
        userRecordsRepository.deleteById(id);
    }
}
