package com.pfa.smartwatering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfa.smartwatering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GrandeurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Grandeur.class);
        Grandeur grandeur1 = new Grandeur();
        grandeur1.setId(1L);
        Grandeur grandeur2 = new Grandeur();
        grandeur2.setId(grandeur1.getId());
        assertThat(grandeur1).isEqualTo(grandeur2);
        grandeur2.setId(2L);
        assertThat(grandeur1).isNotEqualTo(grandeur2);
        grandeur1.setId(null);
        assertThat(grandeur1).isNotEqualTo(grandeur2);
    }
}
