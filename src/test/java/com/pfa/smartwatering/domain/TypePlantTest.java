package com.pfa.smartwatering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfa.smartwatering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypePlantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePlant.class);
        TypePlant typePlant1 = new TypePlant();
        typePlant1.setId(1L);
        TypePlant typePlant2 = new TypePlant();
        typePlant2.setId(typePlant1.getId());
        assertThat(typePlant1).isEqualTo(typePlant2);
        typePlant2.setId(2L);
        assertThat(typePlant1).isNotEqualTo(typePlant2);
        typePlant1.setId(null);
        assertThat(typePlant1).isNotEqualTo(typePlant2);
    }
}
