package com.chainguard.sz.api.web.rest;

import static com.chainguard.sz.api.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chainguard.sz.api.IntegrationTest;
import com.chainguard.sz.api.domain.Parties;
import com.chainguard.sz.api.repository.PartiesRepository;
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
 * Integration tests for the {@link PartiesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartiesResourceIT {

    private static final String DEFAULT_PARTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARTY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_HASH_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_HASH_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_CONTACT_NUMBER = new BigDecimal(1);
    private static final BigDecimal UPDATED_CONTACT_NUMBER = new BigDecimal(2);

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE_NUMBER = 1;
    private static final Integer UPDATED_PHONE_NUMBER = 2;

    private static final LocalDate DEFAULT_DATE_ADDED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADDED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/parties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartiesRepository partiesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartiesMockMvc;

    private Parties parties;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parties createEntity(EntityManager em) {
        Parties parties = new Parties()
            .partyName(DEFAULT_PARTY_NAME)
            .mainContact(DEFAULT_MAIN_CONTACT)
            .hashAddress(DEFAULT_HASH_ADDRESS)
            .title(DEFAULT_TITLE)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .status(DEFAULT_STATUS)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .dateAdded(DEFAULT_DATE_ADDED)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return parties;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parties createUpdatedEntity(EntityManager em) {
        Parties parties = new Parties()
            .partyName(UPDATED_PARTY_NAME)
            .mainContact(UPDATED_MAIN_CONTACT)
            .hashAddress(UPDATED_HASH_ADDRESS)
            .title(UPDATED_TITLE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .status(UPDATED_STATUS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);
        return parties;
    }

    @BeforeEach
    public void initTest() {
        parties = createEntity(em);
    }

    @Test
    @Transactional
    void createParties() throws Exception {
        int databaseSizeBeforeCreate = partiesRepository.findAll().size();
        // Create the Parties
        restPartiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parties)))
            .andExpect(status().isCreated());

        // Validate the Parties in the database
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeCreate + 1);
        Parties testParties = partiesList.get(partiesList.size() - 1);
        assertThat(testParties.getPartyName()).isEqualTo(DEFAULT_PARTY_NAME);
        assertThat(testParties.getMainContact()).isEqualTo(DEFAULT_MAIN_CONTACT);
        assertThat(testParties.getHashAddress()).isEqualTo(DEFAULT_HASH_ADDRESS);
        assertThat(testParties.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testParties.getContactNumber()).isEqualByComparingTo(DEFAULT_CONTACT_NUMBER);
        assertThat(testParties.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testParties.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testParties.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testParties.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testParties.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testParties.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void createPartiesWithExistingId() throws Exception {
        // Create the Parties with an existing ID
        parties.setId(1L);

        int databaseSizeBeforeCreate = partiesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartiesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parties)))
            .andExpect(status().isBadRequest());

        // Validate the Parties in the database
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParties() throws Exception {
        // Initialize the database
        partiesRepository.saveAndFlush(parties);

        // Get all the partiesList
        restPartiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parties.getId().intValue())))
            .andExpect(jsonPath("$.[*].partyName").value(hasItem(DEFAULT_PARTY_NAME)))
            .andExpect(jsonPath("$.[*].mainContact").value(hasItem(DEFAULT_MAIN_CONTACT)))
            .andExpect(jsonPath("$.[*].hashAddress").value(hasItem(DEFAULT_HASH_ADDRESS)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(sameNumber(DEFAULT_CONTACT_NUMBER))))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getParties() throws Exception {
        // Initialize the database
        partiesRepository.saveAndFlush(parties);

        // Get the parties
        restPartiesMockMvc
            .perform(get(ENTITY_API_URL_ID, parties.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parties.getId().intValue()))
            .andExpect(jsonPath("$.partyName").value(DEFAULT_PARTY_NAME))
            .andExpect(jsonPath("$.mainContact").value(DEFAULT_MAIN_CONTACT))
            .andExpect(jsonPath("$.hashAddress").value(DEFAULT_HASH_ADDRESS))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.contactNumber").value(sameNumber(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingParties() throws Exception {
        // Get the parties
        restPartiesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParties() throws Exception {
        // Initialize the database
        partiesRepository.saveAndFlush(parties);

        int databaseSizeBeforeUpdate = partiesRepository.findAll().size();

        // Update the parties
        Parties updatedParties = partiesRepository.findById(parties.getId()).get();
        // Disconnect from session so that the updates on updatedParties are not directly saved in db
        em.detach(updatedParties);
        updatedParties
            .partyName(UPDATED_PARTY_NAME)
            .mainContact(UPDATED_MAIN_CONTACT)
            .hashAddress(UPDATED_HASH_ADDRESS)
            .title(UPDATED_TITLE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .status(UPDATED_STATUS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restPartiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParties.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParties))
            )
            .andExpect(status().isOk());

        // Validate the Parties in the database
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeUpdate);
        Parties testParties = partiesList.get(partiesList.size() - 1);
        assertThat(testParties.getPartyName()).isEqualTo(UPDATED_PARTY_NAME);
        assertThat(testParties.getMainContact()).isEqualTo(UPDATED_MAIN_CONTACT);
        assertThat(testParties.getHashAddress()).isEqualTo(UPDATED_HASH_ADDRESS);
        assertThat(testParties.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testParties.getContactNumber()).isEqualByComparingTo(UPDATED_CONTACT_NUMBER);
        assertThat(testParties.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParties.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testParties.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testParties.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testParties.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testParties.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingParties() throws Exception {
        int databaseSizeBeforeUpdate = partiesRepository.findAll().size();
        parties.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parties.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parties))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parties in the database
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParties() throws Exception {
        int databaseSizeBeforeUpdate = partiesRepository.findAll().size();
        parties.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parties))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parties in the database
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParties() throws Exception {
        int databaseSizeBeforeUpdate = partiesRepository.findAll().size();
        parties.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartiesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parties)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parties in the database
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartiesWithPatch() throws Exception {
        // Initialize the database
        partiesRepository.saveAndFlush(parties);

        int databaseSizeBeforeUpdate = partiesRepository.findAll().size();

        // Update the parties using partial update
        Parties partialUpdatedParties = new Parties();
        partialUpdatedParties.setId(parties.getId());

        partialUpdatedParties
            .mainContact(UPDATED_MAIN_CONTACT)
            .title(UPDATED_TITLE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .email(UPDATED_EMAIL)
            .status(UPDATED_STATUS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateAdded(UPDATED_DATE_ADDED);

        restPartiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParties.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParties))
            )
            .andExpect(status().isOk());

        // Validate the Parties in the database
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeUpdate);
        Parties testParties = partiesList.get(partiesList.size() - 1);
        assertThat(testParties.getPartyName()).isEqualTo(DEFAULT_PARTY_NAME);
        assertThat(testParties.getMainContact()).isEqualTo(UPDATED_MAIN_CONTACT);
        assertThat(testParties.getHashAddress()).isEqualTo(DEFAULT_HASH_ADDRESS);
        assertThat(testParties.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testParties.getContactNumber()).isEqualByComparingTo(UPDATED_CONTACT_NUMBER);
        assertThat(testParties.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParties.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testParties.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testParties.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testParties.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testParties.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdatePartiesWithPatch() throws Exception {
        // Initialize the database
        partiesRepository.saveAndFlush(parties);

        int databaseSizeBeforeUpdate = partiesRepository.findAll().size();

        // Update the parties using partial update
        Parties partialUpdatedParties = new Parties();
        partialUpdatedParties.setId(parties.getId());

        partialUpdatedParties
            .partyName(UPDATED_PARTY_NAME)
            .mainContact(UPDATED_MAIN_CONTACT)
            .hashAddress(UPDATED_HASH_ADDRESS)
            .title(UPDATED_TITLE)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .status(UPDATED_STATUS)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateAdded(UPDATED_DATE_ADDED)
            .dateModified(UPDATED_DATE_MODIFIED);

        restPartiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParties.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParties))
            )
            .andExpect(status().isOk());

        // Validate the Parties in the database
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeUpdate);
        Parties testParties = partiesList.get(partiesList.size() - 1);
        assertThat(testParties.getPartyName()).isEqualTo(UPDATED_PARTY_NAME);
        assertThat(testParties.getMainContact()).isEqualTo(UPDATED_MAIN_CONTACT);
        assertThat(testParties.getHashAddress()).isEqualTo(UPDATED_HASH_ADDRESS);
        assertThat(testParties.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testParties.getContactNumber()).isEqualByComparingTo(UPDATED_CONTACT_NUMBER);
        assertThat(testParties.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParties.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testParties.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testParties.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testParties.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testParties.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingParties() throws Exception {
        int databaseSizeBeforeUpdate = partiesRepository.findAll().size();
        parties.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parties.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parties))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parties in the database
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParties() throws Exception {
        int databaseSizeBeforeUpdate = partiesRepository.findAll().size();
        parties.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parties))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parties in the database
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParties() throws Exception {
        int databaseSizeBeforeUpdate = partiesRepository.findAll().size();
        parties.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartiesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parties)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parties in the database
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParties() throws Exception {
        // Initialize the database
        partiesRepository.saveAndFlush(parties);

        int databaseSizeBeforeDelete = partiesRepository.findAll().size();

        // Delete the parties
        restPartiesMockMvc
            .perform(delete(ENTITY_API_URL_ID, parties.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parties> partiesList = partiesRepository.findAll();
        assertThat(partiesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
