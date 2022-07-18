package com.chainguard.sz.api.service;

import com.chainguard.sz.api.domain.AssessmentProjects;
import com.chainguard.sz.api.repository.AssessmentProjectsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssessmentProjects}.
 */
@Service
@Transactional
public class AssessmentProjectsService {

    private final Logger log = LoggerFactory.getLogger(AssessmentProjectsService.class);

    private final AssessmentProjectsRepository assessmentProjectsRepository;

    public AssessmentProjectsService(AssessmentProjectsRepository assessmentProjectsRepository) {
        this.assessmentProjectsRepository = assessmentProjectsRepository;
    }

    /**
     * Save a assessmentProjects.
     *
     * @param assessmentProjects the entity to save.
     * @return the persisted entity.
     */
    public AssessmentProjects save(AssessmentProjects assessmentProjects) {
        log.debug("Request to save AssessmentProjects : {}", assessmentProjects);
        return assessmentProjectsRepository.save(assessmentProjects);
    }

    /**
     * Update a assessmentProjects.
     *
     * @param assessmentProjects the entity to save.
     * @return the persisted entity.
     */
    public AssessmentProjects update(AssessmentProjects assessmentProjects) {
        log.debug("Request to save AssessmentProjects : {}", assessmentProjects);
        return assessmentProjectsRepository.save(assessmentProjects);
    }

    /**
     * Partially update a assessmentProjects.
     *
     * @param assessmentProjects the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssessmentProjects> partialUpdate(AssessmentProjects assessmentProjects) {
        log.debug("Request to partially update AssessmentProjects : {}", assessmentProjects);

        return assessmentProjectsRepository
            .findById(assessmentProjects.getId())
            .map(existingAssessmentProjects -> {
                if (assessmentProjects.getProjectName() != null) {
                    existingAssessmentProjects.setProjectName(assessmentProjects.getProjectName());
                }
                if (assessmentProjects.getPriority() != null) {
                    existingAssessmentProjects.setPriority(assessmentProjects.getPriority());
                }
                if (assessmentProjects.getProjectManager() != null) {
                    existingAssessmentProjects.setProjectManager(assessmentProjects.getProjectManager());
                }
                if (assessmentProjects.getEstStartDate() != null) {
                    existingAssessmentProjects.setEstStartDate(assessmentProjects.getEstStartDate());
                }
                if (assessmentProjects.getEstEndDate() != null) {
                    existingAssessmentProjects.setEstEndDate(assessmentProjects.getEstEndDate());
                }
                if (assessmentProjects.getActualStartDate() != null) {
                    existingAssessmentProjects.setActualStartDate(assessmentProjects.getActualStartDate());
                }
                if (assessmentProjects.getActualEndDate() != null) {
                    existingAssessmentProjects.setActualEndDate(assessmentProjects.getActualEndDate());
                }
                if (assessmentProjects.getDescription() != null) {
                    existingAssessmentProjects.setDescription(assessmentProjects.getDescription());
                }
                if (assessmentProjects.getSortOrder() != null) {
                    existingAssessmentProjects.setSortOrder(assessmentProjects.getSortOrder());
                }
                if (assessmentProjects.getDateAdded() != null) {
                    existingAssessmentProjects.setDateAdded(assessmentProjects.getDateAdded());
                }
                if (assessmentProjects.getDateModified() != null) {
                    existingAssessmentProjects.setDateModified(assessmentProjects.getDateModified());
                }

                return existingAssessmentProjects;
            })
            .map(assessmentProjectsRepository::save);
    }

    /**
     * Get all the assessmentProjects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssessmentProjects> findAll(Pageable pageable) {
        log.debug("Request to get all AssessmentProjects");
        return assessmentProjectsRepository.findAll(pageable);
    }

    /**
     * Get one assessmentProjects by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssessmentProjects> findOne(Long id) {
        log.debug("Request to get AssessmentProjects : {}", id);
        return assessmentProjectsRepository.findById(id);
    }

    /**
     * Delete the assessmentProjects by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AssessmentProjects : {}", id);
        assessmentProjectsRepository.deleteById(id);
    }
}
