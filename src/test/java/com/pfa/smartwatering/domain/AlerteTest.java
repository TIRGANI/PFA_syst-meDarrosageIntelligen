package com.pfa.smartwatering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfa.smartwatering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlerteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alerte.class);
        Alerte alerte1 = new Alerte();
        alerte1.setId(1L);
        Alerte alerte2 = new Alerte();
        alerte2.setId(alerte1.getId());
        assertThat(alerte1).isEqualTo(alerte2);
        alerte2.setId(2L);
        assertThat(alerte1).isNotEqualTo(alerte2);
        alerte1.setId(null);
        assertThat(alerte1).isNotEqualTo(alerte2);
    }
}
