package com.chainguard.sz.api.web.rest;

import com.chainguard.sz.api.domain.Parties;
import com.chainguard.sz.api.repository.PartiesRepository;
import com.chainguard.sz.api.service.PartiesService;
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
 * REST controller for managing {@link com.chainguard.sz.api.domain.Parties}.
 */
@RestController
@RequestMapping("/api")
public class PartiesResource {

    private final Logger log = LoggerFactory.getLogger(PartiesResource.class);

    private static final String ENTITY_NAME = "parties";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartiesService partiesService;

    private final PartiesRepository partiesRepository;

    public PartiesResource(PartiesService partiesService, PartiesRepository partiesRepository) {
        this.partiesService = partiesService;
        this.partiesRepository = partiesRepository;
    }

    /**
     * {@code POST  /parties} : Create a new parties.
     *
     * @param parties the parties to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parties, or with status {@code 400 (Bad Request)} if the parties has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parties")
    public ResponseEntity<Parties> createParties(@RequestBody Parties parties) throws URISyntaxException {
        log.debug("REST request to save Parties : {}", parties);
        if (parties.getId() != null) {
            throw new BadRequestAlertException("A new parties cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Parties result = partiesService.save(parties);
        return ResponseEntity
            .created(new URI("/api/parties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parties/:id} : Updates an existing parties.
     *
     * @param id the id of the parties to save.
     * @param parties the parties to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parties,
     * or with status {@code 400 (Bad Request)} if the parties is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parties couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parties/{id}")
    public ResponseEntity<Parties> updateParties(@PathVariable(value = "id", required = false) final Long id, @RequestBody Parties parties)
        throws URISyntaxException {
        log.debug("REST request to update Parties : {}, {}", id, parties);
        if (parties.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parties.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Parties result = partiesService.update(parties);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parties.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parties/:id} : Partial updates given fields of an existing parties, field will ignore if it is null
     *
     * @param id the id of the parties to save.
     * @param parties the parties to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parties,
     * or with status {@code 400 (Bad Request)} if the parties is not valid,
     * or with status {@code 404 (Not Found)} if the parties is not found,
     * or with status {@code 500 (Internal Server Error)} if the parties couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Parties> partialUpdateParties(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Parties parties
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parties partially : {}, {}", id, parties);
        if (parties.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parties.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Parties> result = partiesService.partialUpdate(parties);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parties.getId().toString())
        );
    }

    /**
     * {@code GET  /parties} : get all the parties.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parties in body.
     */
    @GetMapping("/parties")
    public ResponseEntity<List<Parties>> getAllParties(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Parties");
        Page<Parties> page = partiesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parties/:id} : get the "id" parties.
     *
     * @param id the id of the parties to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parties, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parties/{id}")
    public ResponseEntity<Parties> getParties(@PathVariable Long id) {
        log.debug("REST request to get Parties : {}", id);
        Optional<Parties> parties = partiesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parties);
    }

    /**
     * {@code DELETE  /parties/:id} : delete the "id" parties.
     *
     * @param id the id of the parties to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parties/{id}")
    public ResponseEntity<Void> deleteParties(@PathVariable Long id) {
        log.debug("REST request to delete Parties : {}", id);
        partiesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
