package com.chainguard.sz.api.web.rest;

import com.chainguard.sz.api.domain.Tasks;
import com.chainguard.sz.api.repository.TasksRepository;
import com.chainguard.sz.api.service.TasksService;
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
 * REST controller for managing {@link com.chainguard.sz.api.domain.Tasks}.
 */
@RestController
@RequestMapping("/api")
public class TasksResource {

    private final Logger log = LoggerFactory.getLogger(TasksResource.class);

    private static final String ENTITY_NAME = "tasks";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TasksService tasksService;

    private final TasksRepository tasksRepository;

    public TasksResource(TasksService tasksService, TasksRepository tasksRepository) {
        this.tasksService = tasksService;
        this.tasksRepository = tasksRepository;
    }

    /**
     * {@code POST  /tasks} : Create a new tasks.
     *
     * @param tasks the tasks to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tasks, or with status {@code 400 (Bad Request)} if the tasks has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tasks")
    public ResponseEntity<Tasks> createTasks(@Valid @RequestBody Tasks tasks) throws URISyntaxException {
        log.debug("REST request to save Tasks : {}", tasks);
        if (tasks.getId() != null) {
            throw new BadRequestAlertException("A new tasks cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tasks result = tasksService.save(tasks);
        return ResponseEntity
            .created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tasks/:id} : Updates an existing tasks.
     *
     * @param id the id of the tasks to save.
     * @param tasks the tasks to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tasks,
     * or with status {@code 400 (Bad Request)} if the tasks is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tasks couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Tasks> updateTasks(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Tasks tasks)
        throws URISyntaxException {
        log.debug("REST request to update Tasks : {}, {}", id, tasks);
        if (tasks.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tasks.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tasksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tasks result = tasksService.update(tasks);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tasks.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tasks/:id} : Partial updates given fields of an existing tasks, field will ignore if it is null
     *
     * @param id the id of the tasks to save.
     * @param tasks the tasks to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tasks,
     * or with status {@code 400 (Bad Request)} if the tasks is not valid,
     * or with status {@code 404 (Not Found)} if the tasks is not found,
     * or with status {@code 500 (Internal Server Error)} if the tasks couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tasks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tasks> partialUpdateTasks(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tasks tasks
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tasks partially : {}, {}", id, tasks);
        if (tasks.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tasks.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tasksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tasks> result = tasksService.partialUpdate(tasks);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tasks.getId().toString())
        );
    }

    /**
     * {@code GET  /tasks} : get all the tasks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tasks in body.
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<Tasks>> getAllTasks(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Tasks");
        Page<Tasks> page = tasksService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tasks/:id} : get the "id" tasks.
     *
     * @param id the id of the tasks to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tasks, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Tasks> getTasks(@PathVariable Long id) {
        log.debug("REST request to get Tasks : {}", id);
        Optional<Tasks> tasks = tasksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tasks);
    }

    /**
     * {@code DELETE  /tasks/:id} : delete the "id" tasks.
     *
     * @param id the id of the tasks to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTasks(@PathVariable Long id) {
        log.debug("REST request to delete Tasks : {}", id);
        tasksService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
