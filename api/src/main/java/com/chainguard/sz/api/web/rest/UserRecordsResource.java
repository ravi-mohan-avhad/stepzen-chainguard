package com.chainguard.sz.api.web.rest;

import com.chainguard.sz.api.domain.UserRecords;
import com.chainguard.sz.api.repository.UserRecordsRepository;
import com.chainguard.sz.api.service.UserRecordsService;
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
 * REST controller for managing {@link com.chainguard.sz.api.domain.UserRecords}.
 */
@RestController
@RequestMapping("/api")
public class UserRecordsResource {

    private final Logger log = LoggerFactory.getLogger(UserRecordsResource.class);

    private static final String ENTITY_NAME = "userRecords";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserRecordsService userRecordsService;

    private final UserRecordsRepository userRecordsRepository;

    public UserRecordsResource(UserRecordsService userRecordsService, UserRecordsRepository userRecordsRepository) {
        this.userRecordsService = userRecordsService;
        this.userRecordsRepository = userRecordsRepository;
    }

    /**
     * {@code POST  /user-records} : Create a new userRecords.
     *
     * @param userRecords the userRecords to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userRecords, or with status {@code 400 (Bad Request)} if the userRecords has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-records")
    public ResponseEntity<UserRecords> createUserRecords(@RequestBody UserRecords userRecords) throws URISyntaxException {
        log.debug("REST request to save UserRecords : {}", userRecords);
        if (userRecords.getId() != null) {
            throw new BadRequestAlertException("A new userRecords cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserRecords result = userRecordsService.save(userRecords);
        return ResponseEntity
            .created(new URI("/api/user-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-records/:id} : Updates an existing userRecords.
     *
     * @param id the id of the userRecords to save.
     * @param userRecords the userRecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRecords,
     * or with status {@code 400 (Bad Request)} if the userRecords is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userRecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-records/{id}")
    public ResponseEntity<UserRecords> updateUserRecords(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserRecords userRecords
    ) throws URISyntaxException {
        log.debug("REST request to update UserRecords : {}, {}", id, userRecords);
        if (userRecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userRecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRecordsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserRecords result = userRecordsService.update(userRecords);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userRecords.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-records/:id} : Partial updates given fields of an existing userRecords, field will ignore if it is null
     *
     * @param id the id of the userRecords to save.
     * @param userRecords the userRecords to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRecords,
     * or with status {@code 400 (Bad Request)} if the userRecords is not valid,
     * or with status {@code 404 (Not Found)} if the userRecords is not found,
     * or with status {@code 500 (Internal Server Error)} if the userRecords couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserRecords> partialUpdateUserRecords(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserRecords userRecords
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserRecords partially : {}, {}", id, userRecords);
        if (userRecords.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userRecords.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRecordsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserRecords> result = userRecordsService.partialUpdate(userRecords);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userRecords.getId().toString())
        );
    }

    /**
     * {@code GET  /user-records} : get all the userRecords.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userRecords in body.
     */
    @GetMapping("/user-records")
    public ResponseEntity<List<UserRecords>> getAllUserRecords(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserRecords");
        Page<UserRecords> page = userRecordsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-records/:id} : get the "id" userRecords.
     *
     * @param id the id of the userRecords to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userRecords, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-records/{id}")
    public ResponseEntity<UserRecords> getUserRecords(@PathVariable Long id) {
        log.debug("REST request to get UserRecords : {}", id);
        Optional<UserRecords> userRecords = userRecordsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userRecords);
    }

    /**
     * {@code DELETE  /user-records/:id} : delete the "id" userRecords.
     *
     * @param id the id of the userRecords to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-records/{id}")
    public ResponseEntity<Void> deleteUserRecords(@PathVariable Long id) {
        log.debug("REST request to delete UserRecords : {}", id);
        userRecordsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
