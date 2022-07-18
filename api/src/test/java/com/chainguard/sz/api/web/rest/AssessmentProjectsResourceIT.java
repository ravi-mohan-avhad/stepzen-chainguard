package com.chainguard.sz.api.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chainguard.sz.api.IntegrationTest;
import com.chainguard.sz.api.domain.AssessmentProjects;
import com.chainguard.sz.api.repository.AssessmentProjectsRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AssessmentProjectsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssessmentProjectsResourceIT {

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRIORITY = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITY = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_MANAGER = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_MANAGER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EST_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EST_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EST_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EST_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ACTUAL_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTUAL_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ACTUAL_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTUAL_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/assessment-projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssessmentProjectsRepository assessmentProjectsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssessmentProjectsMockMvc;

    private AssessmentProjects assessmentProjects;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssessmentProjects createEntity(EntityManager em) {
        AssessmentProjects assessmentProjects = new AssessmentProjects()
            .projectName(DEFAULT_PROJECT_NAME)
            .priority(DEFAULT_PRIORITY)
            .projectManager(DEFAULT_PROJECT_MANAGER)
            .estStartDate(DEFAULT_EST_START_DATE)
            .estEndDate(DEFAULT_EST_END_DATE)
            .actualStartDate(DEFAULT_ACTUAL_START_DATE)
            .actualEndDate(DEFAULT_ACTUAL_END_DATE)
            .description(DEFAULT_DESCRIPTION)
            .sortOrder(DEFAULT_SORT_ORDER)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return assessmentProjects;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssessmentProjects createUpdatedEntity(EntityManager em) {
        AssessmentProjects assessmentProjects = new AssessmentProjects()
            .projectName(UPDATED_PROJECT_NAME)
            .priority(UPDATED_PRIORITY)
            .projectManager(UPDATED_PROJECT_MANAGER)
            .estStartDate(UPDATED_EST_START_DATE)
            .estEndDate(UPDATED_EST_END_DATE)
            .actualStartDate(UPDATED_ACTUAL_START_DATE)
            .actualEndDate(UPDATED_ACTUAL_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);
        return assessmentProjects;
    }

    @BeforeEach
    public void initTest() {
        assessmentProjects = createEntity(em);
    }

    @Test
    @Transactional
    void createAssessmentProjects() throws Exception {
        int databaseSizeBeforeCreate = assessmentProjectsRepository.findAll().size();
        // Create the AssessmentProjects
        restAssessmentProjectsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isCreated());

        // Validate the AssessmentProjects in the database
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeCreate + 1);
        AssessmentProjects testAssessmentProjects = assessmentProjectsList.get(assessmentProjectsList.size() - 1);
        assertThat(testAssessmentProjects.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testAssessmentProjects.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testAssessmentProjects.getProjectManager()).isEqualTo(DEFAULT_PROJECT_MANAGER);
        assertThat(testAssessmentProjects.getEstStartDate()).isEqualTo(DEFAULT_EST_START_DATE);
        assertThat(testAssessmentProjects.getEstEndDate()).isEqualTo(DEFAULT_EST_END_DATE);
        assertThat(testAssessmentProjects.getActualStartDate()).isEqualTo(DEFAULT_ACTUAL_START_DATE);
        assertThat(testAssessmentProjects.getActualEndDate()).isEqualTo(DEFAULT_ACTUAL_END_DATE);
        assertThat(testAssessmentProjects.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssessmentProjects.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testAssessmentProjects.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testAssessmentProjects.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void createAssessmentProjectsWithExistingId() throws Exception {
        // Create the AssessmentProjects with an existing ID
        assessmentProjects.setId(1L);

        int databaseSizeBeforeCreate = assessmentProjectsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssessmentProjectsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssessmentProjects in the database
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = assessmentProjectsRepository.findAll().size();
        // set the field null
        assessmentProjects.setProjectName(null);

        // Create the AssessmentProjects, which fails.

        restAssessmentProjectsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isBadRequest());

        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = assessmentProjectsRepository.findAll().size();
        // set the field null
        assessmentProjects.setPriority(null);

        // Create the AssessmentProjects, which fails.

        restAssessmentProjectsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isBadRequest());

        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProjectManagerIsRequired() throws Exception {
        int databaseSizeBeforeTest = assessmentProjectsRepository.findAll().size();
        // set the field null
        assessmentProjects.setProjectManager(null);

        // Create the AssessmentProjects, which fails.

        restAssessmentProjectsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isBadRequest());

        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = assessmentProjectsRepository.findAll().size();
        // set the field null
        assessmentProjects.setDescription(null);

        // Create the AssessmentProjects, which fails.

        restAssessmentProjectsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isBadRequest());

        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssessmentProjects() throws Exception {
        // Initialize the database
        assessmentProjectsRepository.saveAndFlush(assessmentProjects);

        // Get all the assessmentProjectsList
        restAssessmentProjectsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assessmentProjects.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].projectManager").value(hasItem(DEFAULT_PROJECT_MANAGER)))
            .andExpect(jsonPath("$.[*].estStartDate").value(hasItem(DEFAULT_EST_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].estEndDate").value(hasItem(DEFAULT_EST_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualStartDate").value(hasItem(DEFAULT_ACTUAL_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualEndDate").value(hasItem(DEFAULT_ACTUAL_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getAssessmentProjects() throws Exception {
        // Initialize the database
        assessmentProjectsRepository.saveAndFlush(assessmentProjects);

        // Get the assessmentProjects
        restAssessmentProjectsMockMvc
            .perform(get(ENTITY_API_URL_ID, assessmentProjects.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assessmentProjects.getId().intValue()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.projectManager").value(DEFAULT_PROJECT_MANAGER))
            .andExpect(jsonPath("$.estStartDate").value(DEFAULT_EST_START_DATE.toString()))
            .andExpect(jsonPath("$.estEndDate").value(DEFAULT_EST_END_DATE.toString()))
            .andExpect(jsonPath("$.actualStartDate").value(DEFAULT_ACTUAL_START_DATE.toString()))
            .andExpect(jsonPath("$.actualEndDate").value(DEFAULT_ACTUAL_END_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAssessmentProjects() throws Exception {
        // Get the assessmentProjects
        restAssessmentProjectsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssessmentProjects() throws Exception {
        // Initialize the database
        assessmentProjectsRepository.saveAndFlush(assessmentProjects);

        int databaseSizeBeforeUpdate = assessmentProjectsRepository.findAll().size();

        // Update the assessmentProjects
        AssessmentProjects updatedAssessmentProjects = assessmentProjectsRepository.findById(assessmentProjects.getId()).get();
        // Disconnect from session so that the updates on updatedAssessmentProjects are not directly saved in db
        em.detach(updatedAssessmentProjects);
        updatedAssessmentProjects
            .projectName(UPDATED_PROJECT_NAME)
            .priority(UPDATED_PRIORITY)
            .projectManager(UPDATED_PROJECT_MANAGER)
            .estStartDate(UPDATED_EST_START_DATE)
            .estEndDate(UPDATED_EST_END_DATE)
            .actualStartDate(UPDATED_ACTUAL_START_DATE)
            .actualEndDate(UPDATED_ACTUAL_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restAssessmentProjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAssessmentProjects.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAssessmentProjects))
            )
            .andExpect(status().isOk());

        // Validate the AssessmentProjects in the database
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeUpdate);
        AssessmentProjects testAssessmentProjects = assessmentProjectsList.get(assessmentProjectsList.size() - 1);
        assertThat(testAssessmentProjects.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testAssessmentProjects.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testAssessmentProjects.getProjectManager()).isEqualTo(UPDATED_PROJECT_MANAGER);
        assertThat(testAssessmentProjects.getEstStartDate()).isEqualTo(UPDATED_EST_START_DATE);
        assertThat(testAssessmentProjects.getEstEndDate()).isEqualTo(UPDATED_EST_END_DATE);
        assertThat(testAssessmentProjects.getActualStartDate()).isEqualTo(UPDATED_ACTUAL_START_DATE);
        assertThat(testAssessmentProjects.getActualEndDate()).isEqualTo(UPDATED_ACTUAL_END_DATE);
        assertThat(testAssessmentProjects.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssessmentProjects.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testAssessmentProjects.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testAssessmentProjects.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingAssessmentProjects() throws Exception {
        int databaseSizeBeforeUpdate = assessmentProjectsRepository.findAll().size();
        assessmentProjects.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssessmentProjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assessmentProjects.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssessmentProjects in the database
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssessmentProjects() throws Exception {
        int databaseSizeBeforeUpdate = assessmentProjectsRepository.findAll().size();
        assessmentProjects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentProjectsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssessmentProjects in the database
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssessmentProjects() throws Exception {
        int databaseSizeBeforeUpdate = assessmentProjectsRepository.findAll().size();
        assessmentProjects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentProjectsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssessmentProjects in the database
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssessmentProjectsWithPatch() throws Exception {
        // Initialize the database
        assessmentProjectsRepository.saveAndFlush(assessmentProjects);

        int databaseSizeBeforeUpdate = assessmentProjectsRepository.findAll().size();

        // Update the assessmentProjects using partial update
        AssessmentProjects partialUpdatedAssessmentProjects = new AssessmentProjects();
        partialUpdatedAssessmentProjects.setId(assessmentProjects.getId());

        partialUpdatedAssessmentProjects
            .projectManager(UPDATED_PROJECT_MANAGER)
            .estStartDate(UPDATED_EST_START_DATE)
            .estEndDate(UPDATED_EST_END_DATE)
            .actualStartDate(UPDATED_ACTUAL_START_DATE)
            .actualEndDate(UPDATED_ACTUAL_END_DATE)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restAssessmentProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssessmentProjects.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssessmentProjects))
            )
            .andExpect(status().isOk());

        // Validate the AssessmentProjects in the database
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeUpdate);
        AssessmentProjects testAssessmentProjects = assessmentProjectsList.get(assessmentProjectsList.size() - 1);
        assertThat(testAssessmentProjects.getProjectName()).isEqualTo(DEFAULT_PROJECT_NAME);
        assertThat(testAssessmentProjects.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testAssessmentProjects.getProjectManager()).isEqualTo(UPDATED_PROJECT_MANAGER);
        assertThat(testAssessmentProjects.getEstStartDate()).isEqualTo(UPDATED_EST_START_DATE);
        assertThat(testAssessmentProjects.getEstEndDate()).isEqualTo(UPDATED_EST_END_DATE);
        assertThat(testAssessmentProjects.getActualStartDate()).isEqualTo(UPDATED_ACTUAL_START_DATE);
        assertThat(testAssessmentProjects.getActualEndDate()).isEqualTo(UPDATED_ACTUAL_END_DATE);
        assertThat(testAssessmentProjects.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssessmentProjects.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testAssessmentProjects.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testAssessmentProjects.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateAssessmentProjectsWithPatch() throws Exception {
        // Initialize the database
        assessmentProjectsRepository.saveAndFlush(assessmentProjects);

        int databaseSizeBeforeUpdate = assessmentProjectsRepository.findAll().size();

        // Update the assessmentProjects using partial update
        AssessmentProjects partialUpdatedAssessmentProjects = new AssessmentProjects();
        partialUpdatedAssessmentProjects.setId(assessmentProjects.getId());

        partialUpdatedAssessmentProjects
            .projectName(UPDATED_PROJECT_NAME)
            .priority(UPDATED_PRIORITY)
            .projectManager(UPDATED_PROJECT_MANAGER)
            .estStartDate(UPDATED_EST_START_DATE)
            .estEndDate(UPDATED_EST_END_DATE)
            .actualStartDate(UPDATED_ACTUAL_START_DATE)
            .actualEndDate(UPDATED_ACTUAL_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restAssessmentProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssessmentProjects.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssessmentProjects))
            )
            .andExpect(status().isOk());

        // Validate the AssessmentProjects in the database
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeUpdate);
        AssessmentProjects testAssessmentProjects = assessmentProjectsList.get(assessmentProjectsList.size() - 1);
        assertThat(testAssessmentProjects.getProjectName()).isEqualTo(UPDATED_PROJECT_NAME);
        assertThat(testAssessmentProjects.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testAssessmentProjects.getProjectManager()).isEqualTo(UPDATED_PROJECT_MANAGER);
        assertThat(testAssessmentProjects.getEstStartDate()).isEqualTo(UPDATED_EST_START_DATE);
        assertThat(testAssessmentProjects.getEstEndDate()).isEqualTo(UPDATED_EST_END_DATE);
        assertThat(testAssessmentProjects.getActualStartDate()).isEqualTo(UPDATED_ACTUAL_START_DATE);
        assertThat(testAssessmentProjects.getActualEndDate()).isEqualTo(UPDATED_ACTUAL_END_DATE);
        assertThat(testAssessmentProjects.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssessmentProjects.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testAssessmentProjects.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testAssessmentProjects.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingAssessmentProjects() throws Exception {
        int databaseSizeBeforeUpdate = assessmentProjectsRepository.findAll().size();
        assessmentProjects.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssessmentProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assessmentProjects.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssessmentProjects in the database
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssessmentProjects() throws Exception {
        int databaseSizeBeforeUpdate = assessmentProjectsRepository.findAll().size();
        assessmentProjects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssessmentProjects in the database
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssessmentProjects() throws Exception {
        int databaseSizeBeforeUpdate = assessmentProjectsRepository.findAll().size();
        assessmentProjects.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssessmentProjectsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assessmentProjects))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssessmentProjects in the database
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssessmentProjects() throws Exception {
        // Initialize the database
        assessmentProjectsRepository.saveAndFlush(assessmentProjects);

        int databaseSizeBeforeDelete = assessmentProjectsRepository.findAll().size();

        // Delete the assessmentProjects
        restAssessmentProjectsMockMvc
            .perform(delete(ENTITY_API_URL_ID, assessmentProjects.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssessmentProjects> assessmentProjectsList = assessmentProjectsRepository.findAll();
        assertThat(assessmentProjectsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
