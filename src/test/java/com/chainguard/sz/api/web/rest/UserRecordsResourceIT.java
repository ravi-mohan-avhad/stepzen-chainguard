package com.chainguard.sz.api.web.rest;

import static com.chainguard.sz.api.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chainguard.sz.api.IntegrationTest;
import com.chainguard.sz.api.domain.UserRecords;
import com.chainguard.sz.api.domain.enumeration.AccountStatus;
import com.chainguard.sz.api.domain.enumeration.UserCategory;
import com.chainguard.sz.api.repository.UserRecordsRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link UserRecordsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserRecordsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CONTACT_NUMBER = new BigDecimal(1);
    private static final BigDecimal UPDATED_CONTACT_NUMBER = new BigDecimal(2);

    private static final UserCategory DEFAULT_CATEGORY = UserCategory.MALICIOUS;
    private static final UserCategory UPDATED_CATEGORY = UserCategory.VERIFIED;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final AccountStatus DEFAULT_STATUS = AccountStatus.GREEN;
    private static final AccountStatus UPDATED_STATUS = AccountStatus.RED;

    private static final String ENTITY_API_URL = "/api/user-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserRecordsRepository userRecordsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserRecordsMockMvc;

    private UserRecords userRecords;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRecords createEntity(EntityManager em) {
        UserRecords userRecords = new UserRecords()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .title(DEFAULT_TITLE)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .category(DEFAULT_CATEGORY)
            .sortOrder(DEFAULT_SORT_ORDER)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED)
            .status(DEFAULT_STATUS);
        return userRecords;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRecords createUpdatedEntity(EntityManager em) {
        UserRecords userRecords = new UserRecords()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .title(UPDATED_TITLE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .category(UPDATED_CATEGORY)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS);
        return userRecords;
    }

    @BeforeEach
    public void initTest() {
        userRecords = createEntity(em);
    }

    @Test
    @Transactional
    void createUserRecords() throws Exception {
        int databaseSizeBeforeCreate = userRecordsRepository.findAll().size();
        // Create the UserRecords
        restUserRecordsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userRecords)))
            .andExpect(status().isCreated());

        // Validate the UserRecords in the database
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeCreate + 1);
        UserRecords testUserRecords = userRecordsList.get(userRecordsList.size() - 1);
        assertThat(testUserRecords.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserRecords.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserRecords.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testUserRecords.getContactNumber()).isEqualByComparingTo(DEFAULT_CONTACT_NUMBER);
        assertThat(testUserRecords.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testUserRecords.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testUserRecords.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testUserRecords.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
        assertThat(testUserRecords.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createUserRecordsWithExistingId() throws Exception {
        // Create the UserRecords with an existing ID
        userRecords.setId(1L);

        int databaseSizeBeforeCreate = userRecordsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRecordsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userRecords)))
            .andExpect(status().isBadRequest());

        // Validate the UserRecords in the database
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserRecords() throws Exception {
        // Initialize the database
        userRecordsRepository.saveAndFlush(userRecords);

        // Get all the userRecordsList
        restUserRecordsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRecords.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(sameNumber(DEFAULT_CONTACT_NUMBER))))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getUserRecords() throws Exception {
        // Initialize the database
        userRecordsRepository.saveAndFlush(userRecords);

        // Get the userRecords
        restUserRecordsMockMvc
            .perform(get(ENTITY_API_URL_ID, userRecords.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userRecords.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.contactNumber").value(sameNumber(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserRecords() throws Exception {
        // Get the userRecords
        restUserRecordsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserRecords() throws Exception {
        // Initialize the database
        userRecordsRepository.saveAndFlush(userRecords);

        int databaseSizeBeforeUpdate = userRecordsRepository.findAll().size();

        // Update the userRecords
        UserRecords updatedUserRecords = userRecordsRepository.findById(userRecords.getId()).get();
        // Disconnect from session so that the updates on updatedUserRecords are not directly saved in db
        em.detach(updatedUserRecords);
        updatedUserRecords
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .title(UPDATED_TITLE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .category(UPDATED_CATEGORY)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS);

        restUserRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserRecords.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserRecords))
            )
            .andExpect(status().isOk());

        // Validate the UserRecords in the database
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeUpdate);
        UserRecords testUserRecords = userRecordsList.get(userRecordsList.size() - 1);
        assertThat(testUserRecords.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserRecords.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserRecords.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserRecords.getContactNumber()).isEqualByComparingTo(UPDATED_CONTACT_NUMBER);
        assertThat(testUserRecords.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testUserRecords.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testUserRecords.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testUserRecords.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testUserRecords.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingUserRecords() throws Exception {
        int databaseSizeBeforeUpdate = userRecordsRepository.findAll().size();
        userRecords.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRecords.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRecords in the database
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserRecords() throws Exception {
        int databaseSizeBeforeUpdate = userRecordsRepository.findAll().size();
        userRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRecords in the database
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserRecords() throws Exception {
        int databaseSizeBeforeUpdate = userRecordsRepository.findAll().size();
        userRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRecordsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userRecords)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRecords in the database
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserRecordsWithPatch() throws Exception {
        // Initialize the database
        userRecordsRepository.saveAndFlush(userRecords);

        int databaseSizeBeforeUpdate = userRecordsRepository.findAll().size();

        // Update the userRecords using partial update
        UserRecords partialUpdatedUserRecords = new UserRecords();
        partialUpdatedUserRecords.setId(userRecords.getId());

        partialUpdatedUserRecords
            .email(UPDATED_EMAIL)
            .title(UPDATED_TITLE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .category(UPDATED_CATEGORY)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS);

        restUserRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserRecords))
            )
            .andExpect(status().isOk());

        // Validate the UserRecords in the database
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeUpdate);
        UserRecords testUserRecords = userRecordsList.get(userRecordsList.size() - 1);
        assertThat(testUserRecords.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUserRecords.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserRecords.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserRecords.getContactNumber()).isEqualByComparingTo(UPDATED_CONTACT_NUMBER);
        assertThat(testUserRecords.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testUserRecords.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testUserRecords.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testUserRecords.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testUserRecords.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateUserRecordsWithPatch() throws Exception {
        // Initialize the database
        userRecordsRepository.saveAndFlush(userRecords);

        int databaseSizeBeforeUpdate = userRecordsRepository.findAll().size();

        // Update the userRecords using partial update
        UserRecords partialUpdatedUserRecords = new UserRecords();
        partialUpdatedUserRecords.setId(userRecords.getId());

        partialUpdatedUserRecords
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .title(UPDATED_TITLE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .category(UPDATED_CATEGORY)
            .sortOrder(UPDATED_SORT_ORDER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED)
            .status(UPDATED_STATUS);

        restUserRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserRecords))
            )
            .andExpect(status().isOk());

        // Validate the UserRecords in the database
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeUpdate);
        UserRecords testUserRecords = userRecordsList.get(userRecordsList.size() - 1);
        assertThat(testUserRecords.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUserRecords.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserRecords.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserRecords.getContactNumber()).isEqualByComparingTo(UPDATED_CONTACT_NUMBER);
        assertThat(testUserRecords.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testUserRecords.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testUserRecords.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testUserRecords.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
        assertThat(testUserRecords.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingUserRecords() throws Exception {
        int databaseSizeBeforeUpdate = userRecordsRepository.findAll().size();
        userRecords.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRecords in the database
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserRecords() throws Exception {
        int databaseSizeBeforeUpdate = userRecordsRepository.findAll().size();
        userRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRecords in the database
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserRecords() throws Exception {
        int databaseSizeBeforeUpdate = userRecordsRepository.findAll().size();
        userRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userRecords))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRecords in the database
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserRecords() throws Exception {
        // Initialize the database
        userRecordsRepository.saveAndFlush(userRecords);

        int databaseSizeBeforeDelete = userRecordsRepository.findAll().size();

        // Delete the userRecords
        restUserRecordsMockMvc
            .perform(delete(ENTITY_API_URL_ID, userRecords.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserRecords> userRecordsList = userRecordsRepository.findAll();
        assertThat(userRecordsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
