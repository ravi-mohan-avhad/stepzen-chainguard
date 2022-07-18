package com.chainguard.sz.api.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chainguard.sz.api.IntegrationTest;
import com.chainguard.sz.api.domain.EventCards;
import com.chainguard.sz.api.domain.enumeration.EventCardStatus;
import com.chainguard.sz.api.repository.EventCardsRepository;
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
 * Integration tests for the {@link EventCardsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventCardsResourceIT {

    private static final LocalDate DEFAULT_EVENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EVENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_USER_REPORTING_COUNT = 1;
    private static final Integer UPDATED_USER_REPORTING_COUNT = 2;

    private static final Integer DEFAULT_EVENT_CARDS_REPITION = 1;
    private static final Integer UPDATED_EVENT_CARDS_REPITION = 2;

    private static final EventCardStatus DEFAULT_STATUS = EventCardStatus.COMPLETED;
    private static final EventCardStatus UPDATED_STATUS = EventCardStatus.INPROGRESS;

    private static final Integer DEFAULT_SORT_ORDER = 1;
    private static final Integer UPDATED_SORT_ORDER = 2;

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFIED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFIED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/event-cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventCardsRepository eventCardsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventCardsMockMvc;

    private EventCards eventCards;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventCards createEntity(EntityManager em) {
        EventCards eventCards = new EventCards()
            .eventDate(DEFAULT_EVENT_DATE)
            .userReportingCount(DEFAULT_USER_REPORTING_COUNT)
            .eventCardsRepition(DEFAULT_EVENT_CARDS_REPITION)
            .status(DEFAULT_STATUS)
            .sortOrder(DEFAULT_SORT_ORDER)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .dateModified(DEFAULT_DATE_MODIFIED);
        return eventCards;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventCards createUpdatedEntity(EntityManager em) {
        EventCards eventCards = new EventCards()
            .eventDate(UPDATED_EVENT_DATE)
            .userReportingCount(UPDATED_USER_REPORTING_COUNT)
            .eventCardsRepition(UPDATED_EVENT_CARDS_REPITION)
            .status(UPDATED_STATUS)
            .sortOrder(UPDATED_SORT_ORDER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .dateModified(UPDATED_DATE_MODIFIED);
        return eventCards;
    }

    @BeforeEach
    public void initTest() {
        eventCards = createEntity(em);
    }

    @Test
    @Transactional
    void createEventCards() throws Exception {
        int databaseSizeBeforeCreate = eventCardsRepository.findAll().size();
        // Create the EventCards
        restEventCardsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventCards)))
            .andExpect(status().isCreated());

        // Validate the EventCards in the database
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeCreate + 1);
        EventCards testEventCards = eventCardsList.get(eventCardsList.size() - 1);
        assertThat(testEventCards.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(testEventCards.getUserReportingCount()).isEqualTo(DEFAULT_USER_REPORTING_COUNT);
        assertThat(testEventCards.getEventCardsRepition()).isEqualTo(DEFAULT_EVENT_CARDS_REPITION);
        assertThat(testEventCards.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEventCards.getSortOrder()).isEqualTo(DEFAULT_SORT_ORDER);
        assertThat(testEventCards.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testEventCards.getDateModified()).isEqualTo(DEFAULT_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void createEventCardsWithExistingId() throws Exception {
        // Create the EventCards with an existing ID
        eventCards.setId(1L);

        int databaseSizeBeforeCreate = eventCardsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventCardsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventCards)))
            .andExpect(status().isBadRequest());

        // Validate the EventCards in the database
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEventCards() throws Exception {
        // Initialize the database
        eventCardsRepository.saveAndFlush(eventCards);

        // Get all the eventCardsList
        restEventCardsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventCards.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].userReportingCount").value(hasItem(DEFAULT_USER_REPORTING_COUNT)))
            .andExpect(jsonPath("$.[*].eventCardsRepition").value(hasItem(DEFAULT_EVENT_CARDS_REPITION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sortOrder").value(hasItem(DEFAULT_SORT_ORDER)))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dateModified").value(hasItem(DEFAULT_DATE_MODIFIED.toString())));
    }

    @Test
    @Transactional
    void getEventCards() throws Exception {
        // Initialize the database
        eventCardsRepository.saveAndFlush(eventCards);

        // Get the eventCards
        restEventCardsMockMvc
            .perform(get(ENTITY_API_URL_ID, eventCards.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventCards.getId().intValue()))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()))
            .andExpect(jsonPath("$.userReportingCount").value(DEFAULT_USER_REPORTING_COUNT))
            .andExpect(jsonPath("$.eventCardsRepition").value(DEFAULT_EVENT_CARDS_REPITION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.sortOrder").value(DEFAULT_SORT_ORDER))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.dateModified").value(DEFAULT_DATE_MODIFIED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEventCards() throws Exception {
        // Get the eventCards
        restEventCardsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEventCards() throws Exception {
        // Initialize the database
        eventCardsRepository.saveAndFlush(eventCards);

        int databaseSizeBeforeUpdate = eventCardsRepository.findAll().size();

        // Update the eventCards
        EventCards updatedEventCards = eventCardsRepository.findById(eventCards.getId()).get();
        // Disconnect from session so that the updates on updatedEventCards are not directly saved in db
        em.detach(updatedEventCards);
        updatedEventCards
            .eventDate(UPDATED_EVENT_DATE)
            .userReportingCount(UPDATED_USER_REPORTING_COUNT)
            .eventCardsRepition(UPDATED_EVENT_CARDS_REPITION)
            .status(UPDATED_STATUS)
            .sortOrder(UPDATED_SORT_ORDER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .dateModified(UPDATED_DATE_MODIFIED);

        restEventCardsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEventCards.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEventCards))
            )
            .andExpect(status().isOk());

        // Validate the EventCards in the database
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeUpdate);
        EventCards testEventCards = eventCardsList.get(eventCardsList.size() - 1);
        assertThat(testEventCards.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEventCards.getUserReportingCount()).isEqualTo(UPDATED_USER_REPORTING_COUNT);
        assertThat(testEventCards.getEventCardsRepition()).isEqualTo(UPDATED_EVENT_CARDS_REPITION);
        assertThat(testEventCards.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEventCards.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testEventCards.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testEventCards.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingEventCards() throws Exception {
        int databaseSizeBeforeUpdate = eventCardsRepository.findAll().size();
        eventCards.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventCardsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventCards.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCards))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCards in the database
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventCards() throws Exception {
        int databaseSizeBeforeUpdate = eventCardsRepository.findAll().size();
        eventCards.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCardsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventCards))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCards in the database
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventCards() throws Exception {
        int databaseSizeBeforeUpdate = eventCardsRepository.findAll().size();
        eventCards.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCardsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventCards)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventCards in the database
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventCardsWithPatch() throws Exception {
        // Initialize the database
        eventCardsRepository.saveAndFlush(eventCards);

        int databaseSizeBeforeUpdate = eventCardsRepository.findAll().size();

        // Update the eventCards using partial update
        EventCards partialUpdatedEventCards = new EventCards();
        partialUpdatedEventCards.setId(eventCards.getId());

        partialUpdatedEventCards
            .eventDate(UPDATED_EVENT_DATE)
            .userReportingCount(UPDATED_USER_REPORTING_COUNT)
            .eventCardsRepition(UPDATED_EVENT_CARDS_REPITION)
            .sortOrder(UPDATED_SORT_ORDER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .dateModified(UPDATED_DATE_MODIFIED);

        restEventCardsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventCards.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventCards))
            )
            .andExpect(status().isOk());

        // Validate the EventCards in the database
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeUpdate);
        EventCards testEventCards = eventCardsList.get(eventCardsList.size() - 1);
        assertThat(testEventCards.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEventCards.getUserReportingCount()).isEqualTo(UPDATED_USER_REPORTING_COUNT);
        assertThat(testEventCards.getEventCardsRepition()).isEqualTo(UPDATED_EVENT_CARDS_REPITION);
        assertThat(testEventCards.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEventCards.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testEventCards.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testEventCards.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateEventCardsWithPatch() throws Exception {
        // Initialize the database
        eventCardsRepository.saveAndFlush(eventCards);

        int databaseSizeBeforeUpdate = eventCardsRepository.findAll().size();

        // Update the eventCards using partial update
        EventCards partialUpdatedEventCards = new EventCards();
        partialUpdatedEventCards.setId(eventCards.getId());

        partialUpdatedEventCards
            .eventDate(UPDATED_EVENT_DATE)
            .userReportingCount(UPDATED_USER_REPORTING_COUNT)
            .eventCardsRepition(UPDATED_EVENT_CARDS_REPITION)
            .status(UPDATED_STATUS)
            .sortOrder(UPDATED_SORT_ORDER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .dateModified(UPDATED_DATE_MODIFIED);

        restEventCardsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventCards.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventCards))
            )
            .andExpect(status().isOk());

        // Validate the EventCards in the database
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeUpdate);
        EventCards testEventCards = eventCardsList.get(eventCardsList.size() - 1);
        assertThat(testEventCards.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEventCards.getUserReportingCount()).isEqualTo(UPDATED_USER_REPORTING_COUNT);
        assertThat(testEventCards.getEventCardsRepition()).isEqualTo(UPDATED_EVENT_CARDS_REPITION);
        assertThat(testEventCards.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEventCards.getSortOrder()).isEqualTo(UPDATED_SORT_ORDER);
        assertThat(testEventCards.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testEventCards.getDateModified()).isEqualTo(UPDATED_DATE_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingEventCards() throws Exception {
        int databaseSizeBeforeUpdate = eventCardsRepository.findAll().size();
        eventCards.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventCardsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventCards.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventCards))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCards in the database
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventCards() throws Exception {
        int databaseSizeBeforeUpdate = eventCardsRepository.findAll().size();
        eventCards.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCardsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventCards))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventCards in the database
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventCards() throws Exception {
        int databaseSizeBeforeUpdate = eventCardsRepository.findAll().size();
        eventCards.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventCardsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventCards))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventCards in the database
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventCards() throws Exception {
        // Initialize the database
        eventCardsRepository.saveAndFlush(eventCards);

        int databaseSizeBeforeDelete = eventCardsRepository.findAll().size();

        // Delete the eventCards
        restEventCardsMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventCards.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventCards> eventCardsList = eventCardsRepository.findAll();
        assertThat(eventCardsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
