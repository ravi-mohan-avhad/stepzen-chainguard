package com.chainguard.sz.api.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chainguard.sz.api.IntegrationTest;
import com.chainguard.sz.api.domain.Tasks;
import com.chainguard.sz.api.domain.enumeration.TaskStatus;
import com.chainguard.sz.api.repository.TasksRepository;
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
 * Integration tests for the {@link TasksResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TasksResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final TaskStatus DEFAULT_STATUS = TaskStatus.COMPLETED;
    private static final TaskStatus UPDATED_STATUS = TaskStatus.INPROGRESS;

    private static final String DEFAULT_ASSIGNED_TO = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNED_TO = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTasksMockMvc;

    private Tasks tasks;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tasks createEntity(EntityManager em) {
        Tasks tasks = new Tasks()
            .name(DEFAULT_NAME)
            .date(DEFAULT_DATE)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .assignedTo(DEFAULT_ASSIGNED_TO)
            .sortOrder(DEFAULT_SORT_ORDER)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return tasks;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tasks createUpdatedEntity(EntityManager em) {
        Tasks tasks = new Tasks()
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .assignedTo(UPDATED_ASSIGNED_TO)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);
        return tasks;
    }

    @BeforeEach
    public void initTest() {
        tasks = createEntity(em);
    }

    @Test
    @Transactional
    void createTasks() throws Exception {
        int databaseSizeBeforeCreate = tasksRepository.findAll().size();
        // Create the Tasks
        restTasksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tasks)))
            .andExpect(status().isCreated());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeCreate + 1);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTasks.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTasks.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTasks.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTasks.getAssignedTo()).isEqualTo(DEFAULT_ASSIGNED_TO);
        assertThat(testTasks.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testTasks.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testTasks.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void createTasksWithExistingId() throws Exception {
        // Create the Tasks with an existing ID
        tasks.setId(1L);

        int databaseSizeBeforeCreate = tasksRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTasksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tasks)))
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tasksRepository.findAll().size();
        // set the field null
        tasks.setName(null);

        // Create the Tasks, which fails.

        restTasksMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tasks)))
            .andExpect(status().isBadRequest());

        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get all the tasksList
        restTasksMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tasks.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].assignedTo").value(hasItem(DEFAULT_ASSIGNED_TO)))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        // Get the tasks
        restTasksMockMvc
            .perform(get(ENTITY_API_URL_ID, tasks.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tasks.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.assignedTo").value(DEFAULT_ASSIGNED_TO))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTasks() throws Exception {
        // Get the tasks
        restTasksMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Update the tasks
        Tasks updatedTasks = tasksRepository.findById(tasks.getId()).get();
        // Disconnect from session so that the updates on updatedTasks are not directly saved in db
        em.detach(updatedTasks);
        updatedTasks
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .assignedTo(UPDATED_ASSIGNED_TO)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restTasksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTasks.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTasks))
            )
            .andExpect(status().isOk());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTasks.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTasks.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTasks.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTasks.getAssignedTo()).isEqualTo(UPDATED_ASSIGNED_TO);
        assertThat(testTasks.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testTasks.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testTasks.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tasks.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tasks))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tasks))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tasks)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTasksWithPatch() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Update the tasks using partial update
        Tasks partialUpdatedTasks = new Tasks();
        partialUpdatedTasks.setId(tasks.getId());

        partialUpdatedTasks
            .date(UPDATED_DATE)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTasks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTasks))
            )
            .andExpect(status().isOk());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTasks.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTasks.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTasks.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTasks.getAssignedTo()).isEqualTo(DEFAULT_ASSIGNED_TO);
        assertThat(testTasks.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testTasks.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testTasks.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateTasksWithPatch() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();

        // Update the tasks using partial update
        Tasks partialUpdatedTasks = new Tasks();
        partialUpdatedTasks.setId(tasks.getId());

        partialUpdatedTasks
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .assignedTo(UPDATED_ASSIGNED_TO)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTasks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTasks))
            )
            .andExpect(status().isOk());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
        Tasks testTasks = tasksList.get(tasksList.size() - 1);
        assertThat(testTasks.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTasks.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTasks.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTasks.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTasks.getAssignedTo()).isEqualTo(UPDATED_ASSIGNED_TO);
        assertThat(testTasks.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testTasks.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testTasks.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tasks.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tasks))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tasks))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTasks() throws Exception {
        int databaseSizeBeforeUpdate = tasksRepository.findAll().size();
        tasks.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTasksMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tasks)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tasks in the database
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTasks() throws Exception {
        // Initialize the database
        tasksRepository.saveAndFlush(tasks);

        int databaseSizeBeforeDelete = tasksRepository.findAll().size();

        // Delete the tasks
        restTasksMockMvc
            .perform(delete(ENTITY_API_URL_ID, tasks.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tasks> tasksList = tasksRepository.findAll();
        assertThat(tasksList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
