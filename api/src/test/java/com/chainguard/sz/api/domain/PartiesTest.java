package com.chainguard.sz.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chainguard.sz.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartiesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parties.class);
        Parties parties1 = new Parties();
        parties1.setId(1L);
        Parties parties2 = new Parties();
        parties2.setId(parties1.getId());
        assertThat(parties1).isEqualTo(parties2);
        parties2.setId(2L);
        assertThat(parties1).isNotEqualTo(parties2);
        parties1.setId(null);
        assertThat(parties1).isNotEqualTo(parties2);
    }
}
