package com.chainguard.sz.api.service;

import com.chainguard.sz.api.domain.Tasks;
import com.chainguard.sz.api.repository.TasksRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tasks}.
 */
@Service
@Transactional
public class TasksService {

    private final Logger log = LoggerFactory.getLogger(TasksService.class);

    private final TasksRepository tasksRepository;

    public TasksService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    /**
     * Save a tasks.
     *
     * @param tasks the entity to save.
     * @return the persisted entity.
     */
    public Tasks save(Tasks tasks) {
        log.debug("Request to save Tasks : {}", tasks);
        return tasksRepository.save(tasks);
    }

    /**
     * Update a tasks.
     *
     * @param tasks the entity to save.
     * @return the persisted entity.
     */
    public Tasks update(Tasks tasks) {
        log.debug("Request to save Tasks : {}", tasks);
        return tasksRepository.save(tasks);
    }

    /**
     * Partially update a tasks.
     *
     * @param tasks the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Tasks> partialUpdate(Tasks tasks) {
        log.debug("Request to partially update Tasks : {}", tasks);

        return tasksRepository
            .findById(tasks.getId())
            .map(existingTasks -> {
                if (tasks.getName() != null) {
                    existingTasks.setName(tasks.getName());
                }
                if (tasks.getDate() != null) {
                    existingTasks.setDate(tasks.getDate());
                }
                if (tasks.getType() != null) {
                    existingTasks.setType(tasks.getType());
                }
                if (tasks.getStatus() != null) {
                    existingTasks.setStatus(tasks.getStatus());
                }
                if (tasks.getAssignedTo() != null) {
                    existingTasks.setAssignedTo(tasks.getAssignedTo());
                }
                if (tasks.getSortOrder() != null) {
                    existingTasks.setSortOrder(tasks.getSortOrder());
                }
                if (tasks.getDateAdded() != null) {
                    existingTasks.setDateAdded(tasks.getDateAdded());
                }
                if (tasks.getDateModified() != null) {
                    existingTasks.setDateModified(tasks.getDateModified());
                }

                return existingTasks;
            })
            .map(tasksRepository::save);
    }

    /**
     * Get all the tasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Tasks> findAll(Pageable pageable) {
        log.debug("Request to get all Tasks");
        return tasksRepository.findAll(pageable);
    }

    /**
     * Get one tasks by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Tasks> findOne(Long id) {
        log.debug("Request to get Tasks : {}", id);
        return tasksRepository.findById(id);
    }

    /**
     * Delete the tasks by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tasks : {}", id);
        tasksRepository.deleteById(id);
    }
}
