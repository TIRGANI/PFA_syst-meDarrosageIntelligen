package com.pfa.smartwatering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfa.smartwatering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParcelleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parcelle.class);
        Parcelle parcelle1 = new Parcelle();
        parcelle1.setId(1L);
        Parcelle parcelle2 = new Parcelle();
        parcelle2.setId(parcelle1.getId());
        assertThat(parcelle1).isEqualTo(parcelle2);
        parcelle2.setId(2L);
        assertThat(parcelle1).isNotEqualTo(parcelle2);
        parcelle1.setId(null);
        assertThat(parcelle1).isNotEqualTo(parcelle2);
    }
}
