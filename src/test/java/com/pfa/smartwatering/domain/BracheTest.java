package com.pfa.smartwatering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfa.smartwatering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BracheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Brache.class);
        Brache brache1 = new Brache();
        brache1.setId(1L);
        Brache brache2 = new Brache();
        brache2.setId(brache1.getId());
        assertThat(brache1).isEqualTo(brache2);
        brache2.setId(2L);
        assertThat(brache1).isNotEqualTo(brache2);
        brache1.setId(null);
        assertThat(brache1).isNotEqualTo(brache2);
    }
}
