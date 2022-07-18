package com.chainguard.sz.api.repository;

import com.chainguard.sz.api.domain.AssessmentProjects;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssessmentProjects entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssessmentProjectsRepository extends JpaRepository<AssessmentProjects, Long> {}
