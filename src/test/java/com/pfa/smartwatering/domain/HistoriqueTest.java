package com.pfa.smartwatering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfa.smartwatering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoriqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Historique.class);
        Historique historique1 = new Historique();
        historique1.setId(1L);
        Historique historique2 = new Historique();
        historique2.setId(historique1.getId());
        assertThat(historique1).isEqualTo(historique2);
        historique2.setId(2L);
        assertThat(historique1).isNotEqualTo(historique2);
        historique1.setId(null);
        assertThat(historique1).isNotEqualTo(historique2);
    }
}
