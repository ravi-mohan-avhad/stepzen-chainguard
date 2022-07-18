package com.chainguard.sz.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chainguard.sz.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventCardsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventCards.class);
        EventCards eventCards1 = new EventCards();
        eventCards1.setId(1L);
        EventCards eventCards2 = new EventCards();
        eventCards2.setId(eventCards1.getId());
        assertThat(eventCards1).isEqualTo(eventCards2);
        eventCards2.setId(2L);
        assertThat(eventCards1).isNotEqualTo(eventCards2);
        eventCards1.setId(null);
        assertThat(eventCards1).isNotEqualTo(eventCards2);
    }
}
