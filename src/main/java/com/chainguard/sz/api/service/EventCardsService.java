package com.chainguard.sz.api.service;

import com.chainguard.sz.api.domain.EventCards;
import com.chainguard.sz.api.repository.EventCardsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventCards}.
 */
@Service
@Transactional
public class EventCardsService {

    private final Logger log = LoggerFactory.getLogger(EventCardsService.class);

    private final EventCardsRepository eventCardsRepository;

    public EventCardsService(EventCardsRepository eventCardsRepository) {
        this.eventCardsRepository = eventCardsRepository;
    }

    /**
     * Save a eventCards.
     *
     * @param eventCards the entity to save.
     * @return the persisted entity.
     */
    public EventCards save(EventCards eventCards) {
        log.debug("Request to save EventCards : {}", eventCards);
        return eventCardsRepository.save(eventCards);
    }

    /**
     * Update a eventCards.
     *
     * @param eventCards the entity to save.
     * @return the persisted entity.
     */
    public EventCards update(EventCards eventCards) {
        log.debug("Request to save EventCards : {}", eventCards);
        return eventCardsRepository.save(eventCards);
    }

    /**
     * Partially update a eventCards.
     *
     * @param eventCards the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventCards> partialUpdate(EventCards eventCards) {
        log.debug("Request to partially update EventCards : {}", eventCards);

        return eventCardsRepository
            .findById(eventCards.getId())
            .map(existingEventCards -> {
                if (eventCards.getEventDate() != null) {
                    existingEventCards.setEventDate(eventCards.getEventDate());
                }
                if (eventCards.getUserReportingCount() != null) {
                    existingEventCards.setUserReportingCount(eventCards.getUserReportingCount());
                }
                if (eventCards.getEventCardsRepition() != null) {
                    existingEventCards.setEventCardsRepition(eventCards.getEventCardsRepition());
                }
                if (eventCards.getStatus() != null) {
                    existingEventCards.setStatus(eventCards.getStatus());
                }
                if (eventCards.getSortOrder() != null) {
                    existingEventCards.setSortOrder(eventCards.getSortOrder());
                }
                if (eventCards.getTransactionDate() != null) {
                    existingEventCards.setTransactionDate(eventCards.getTransactionDate());
                }
                if (eventCards.getDateModified() != null) {
                    existingEventCards.setDateModified(eventCards.getDateModified());
                }

                return existingEventCards;
            })
            .map(eventCardsRepository::save);
    }

    /**
     * Get all the eventCards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EventCards> findAll(Pageable pageable) {
        log.debug("Request to get all EventCards");
        return eventCardsRepository.findAll(pageable);
    }

    /**
     * Get one eventCards by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventCards> findOne(Long id) {
        log.debug("Request to get EventCards : {}", id);
        return eventCardsRepository.findById(id);
    }

    /**
     * Delete the eventCards by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EventCards : {}", id);
        eventCardsRepository.deleteById(id);
    }
}
