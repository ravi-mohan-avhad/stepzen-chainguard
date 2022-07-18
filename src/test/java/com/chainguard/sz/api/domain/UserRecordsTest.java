package com.chainguard.sz.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.chainguard.sz.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserRecordsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRecords.class);
        UserRecords userRecords1 = new UserRecords();
        userRecords1.setId(1L);
        UserRecords userRecords2 = new UserRecords();
        userRecords2.setId(userRecords1.getId());
        assertThat(userRecords1).isEqualTo(userRecords2);
        userRecords2.setId(2L);
        assertThat(userRecords1).isNotEqualTo(userRecords2);
        userRecords1.setId(null);
        assertThat(userRecords1).isNotEqualTo(userRecords2);
    }
}
