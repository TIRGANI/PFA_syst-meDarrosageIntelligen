package com.pfa.smartwatering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfa.smartwatering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FermTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ferm.class);
        Ferm ferm1 = new Ferm();
        ferm1.setId(1L);
        Ferm ferm2 = new Ferm();
        ferm2.setId(ferm1.getId());
        assertThat(ferm1).isEqualTo(ferm2);
        ferm2.setId(2L);
        assertThat(ferm1).isNotEqualTo(ferm2);
        ferm1.setId(null);
        assertThat(ferm1).isNotEqualTo(ferm2);
    }
}
