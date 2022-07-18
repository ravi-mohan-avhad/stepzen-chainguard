package com.chainguard.sz.api.web.rest;

import com.chainguard.sz.api.domain.AssessmentProjects;
import com.chainguard.sz.api.repository.AssessmentProjectsRepository;
import com.chainguard.sz.api.service.AssessmentProjectsService;
import com.chainguard.sz.api.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.chainguard.sz.api.domain.AssessmentProjects}.
 */
@RestController
@RequestMapping("/api")
public class AssessmentProjectsResource {

    private final Logger log = LoggerFactory.getLogger(AssessmentProjectsResource.class);

    private static final String ENTITY_NAME = "assessmentProjects";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssessmentProjectsService assessmentProjectsService;

    private final AssessmentProjectsRepository assessmentProjectsRepository;

    public AssessmentProjectsResource(
        AssessmentProjectsService assessmentProjectsService,
        AssessmentProjectsRepository assessmentProjectsRepository
    ) {
        this.assessmentProjectsService = assessmentProjectsService;
        this.assessmentProjectsRepository = assessmentProjectsRepository;
    }

    /**
     * {@code POST  /assessment-projects} : Create a new assessmentProjects.
     *
     * @param assessmentProjects the assessmentProjects to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assessmentProjects, or with status {@code 400 (Bad Request)} if the assessmentProjects has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assessment-projects")
    public ResponseEntity<AssessmentProjects> createAssessmentProjects(@Valid @RequestBody AssessmentProjects assessmentProjects)
        throws URISyntaxException {
        log.debug("REST request to save AssessmentProjects : {}", assessmentProjects);
        if (assessmentProjects.getId() != null) {
            throw new BadRequestAlertException("A new assessmentProjects cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssessmentProjects result = assessmentProjectsService.save(assessmentProjects);
        return ResponseEntity
            .created(new URI("/api/assessment-projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assessment-projects/:id} : Updates an existing assessmentProjects.
     *
     * @param id the id of the assessmentProjects to save.
     * @param assessmentProjects the assessmentProjects to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assessmentProjects,
     * or with status {@code 400 (Bad Request)} if the assessmentProjects is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assessmentProjects couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assessment-projects/{id}")
    public ResponseEntity<AssessmentProjects> updateAssessmentProjects(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssessmentProjects assessmentProjects
    ) throws URISyntaxException {
        log.debug("REST request to update AssessmentProjects : {}, {}", id, assessmentProjects);
        if (assessmentProjects.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assessmentProjects.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assessmentProjectsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssessmentProjects result = assessmentProjectsService.update(assessmentProjects);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assessmentProjects.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /assessment-projects/:id} : Partial updates given fields of an existing assessmentProjects, field will ignore if it is null
     *
     * @param id the id of the assessmentProjects to save.
     * @param assessmentProjects the assessmentProjects to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assessmentProjects,
     * or with status {@code 400 (Bad Request)} if the assessmentProjects is not valid,
     * or with status {@code 404 (Not Found)} if the assessmentProjects is not found,
     * or with status {@code 500 (Internal Server Error)} if the assessmentProjects couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assessment-projects/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssessmentProjects> partialUpdateAssessmentProjects(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssessmentProjects assessmentProjects
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssessmentProjects partially : {}, {}", id, assessmentProjects);
        if (assessmentProjects.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assessmentProjects.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assessmentProjectsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssessmentProjects> result = assessmentProjectsService.partialUpdate(assessmentProjects);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assessmentProjects.getId().toString())
        );
    }

    /**
     * {@code GET  /assessment-projects} : get all the assessmentProjects.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assessmentProjects in body.
     */
    @GetMapping("/assessment-projects")
    public ResponseEntity<List<AssessmentProjects>> getAllAssessmentProjects(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AssessmentProjects");
        Page<AssessmentProjects> page = assessmentProjectsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assessment-projects/:id} : get the "id" assessmentProjects.
     *
     * @param id the id of the assessmentProjects to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assessmentProjects, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assessment-projects/{id}")
    public ResponseEntity<AssessmentProjects> getAssessmentProjects(@PathVariable Long id) {
        log.debug("REST request to get AssessmentProjects : {}", id);
        Optional<AssessmentProjects> assessmentProjects = assessmentProjectsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assessmentProjects);
    }

    /**
     * {@code DELETE  /assessment-projects/:id} : delete the "id" assessmentProjects.
     *
     * @param id the id of the assessmentProjects to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assessment-projects/{id}")
    public ResponseEntity<Void> deleteAssessmentProjects(@PathVariable Long id) {
        log.debug("REST request to delete AssessmentProjects : {}", id);
        assessmentProjectsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
