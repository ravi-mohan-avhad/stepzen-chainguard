package com.chainguard.sz.api.web.rest;

import com.chainguard.sz.api.domain.EventCards;
import com.chainguard.sz.api.repository.EventCardsRepository;
import com.chainguard.sz.api.service.EventCardsService;
import com.chainguard.sz.api.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.chainguard.sz.api.domain.EventCards}.
 */
@RestController
@RequestMapping("/api")
public class EventCardsResource {

    private final Logger log = LoggerFactory.getLogger(EventCardsResource.class);

    private static final String ENTITY_NAME = "eventCards";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventCardsService eventCardsService;

    private final EventCardsRepository eventCardsRepository;

    public EventCardsResource(EventCardsService eventCardsService, EventCardsRepository eventCardsRepository) {
        this.eventCardsService = eventCardsService;
        this.eventCardsRepository = eventCardsRepository;
    }

    /**
     * {@code POST  /event-cards} : Create a new eventCards.
     *
     * @param eventCards the eventCards to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventCards, or with status {@code 400 (Bad Request)} if the eventCards has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-cards")
    public ResponseEntity<EventCards> createEventCards(@RequestBody EventCards eventCards) throws URISyntaxException {
        log.debug("REST request to save EventCards : {}", eventCards);
        if (eventCards.getId() != null) {
            throw new BadRequestAlertException("A new eventCards cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventCards result = eventCardsService.save(eventCards);
        return ResponseEntity
            .created(new URI("/api/event-cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-cards/:id} : Updates an existing eventCards.
     *
     * @param id the id of the eventCards to save.
     * @param eventCards the eventCards to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventCards,
     * or with status {@code 400 (Bad Request)} if the eventCards is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventCards couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-cards/{id}")
    public ResponseEntity<EventCards> updateEventCards(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventCards eventCards
    ) throws URISyntaxException {
        log.debug("REST request to update EventCards : {}, {}", id, eventCards);
        if (eventCards.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventCards.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventCardsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventCards result = eventCardsService.update(eventCards);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventCards.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-cards/:id} : Partial updates given fields of an existing eventCards, field will ignore if it is null
     *
     * @param id the id of the eventCards to save.
     * @param eventCards the eventCards to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventCards,
     * or with status {@code 400 (Bad Request)} if the eventCards is not valid,
     * or with status {@code 404 (Not Found)} if the eventCards is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventCards couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-cards/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventCards> partialUpdateEventCards(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventCards eventCards
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventCards partially : {}, {}", id, eventCards);
        if (eventCards.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventCards.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventCardsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventCards> result = eventCardsService.partialUpdate(eventCards);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventCards.getId().toString())
        );
    }

    /**
     * {@code GET  /event-cards} : get all the eventCards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventCards in body.
     */
    @GetMapping("/event-cards")
    public ResponseEntity<List<EventCards>> getAllEventCards(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of EventCards");
        Page<EventCards> page = eventCardsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-cards/:id} : get the "id" eventCards.
     *
     * @param id the id of the eventCards to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventCards, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-cards/{id}")
    public ResponseEntity<EventCards> getEventCards(@PathVariable Long id) {
        log.debug("REST request to get EventCards : {}", id);
        Optional<EventCards> eventCards = eventCardsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventCards);
    }

    /**
     * {@code DELETE  /event-cards/:id} : delete the "id" eventCards.
     *
     * @param id the id of the eventCards to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-cards/{id}")
    public ResponseEntity<Void> deleteEventCards(@PathVariable Long id) {
        log.debug("REST request to delete EventCards : {}", id);
        eventCardsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
