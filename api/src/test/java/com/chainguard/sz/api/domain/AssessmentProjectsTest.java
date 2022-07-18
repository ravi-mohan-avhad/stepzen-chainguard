package com.chainguard.sz.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chainguard.sz.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssessmentProjectsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssessmentProjects.class);
        AssessmentProjects assessmentProjects1 = new AssessmentProjects();
        assessmentProjects1.setId(1L);
        AssessmentProjects assessmentProjects2 = new AssessmentProjects();
        assessmentProjects2.setId(assessmentProjects1.getId());
        assertThat(assessmentProjects1).isEqualTo(assessmentProjects2);
        assessmentProjects2.setId(2L);
        assertThat(assessmentProjects1).isNotEqualTo(assessmentProjects2);
        assessmentProjects1.setId(null);
        assertThat(assessmentProjects1).isNotEqualTo(assessmentProjects2);
    }
}
