package com.pfa.smartwatering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfa.smartwatering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlantageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plantage.class);
        Plantage plantage1 = new Plantage();
        plantage1.setId(1L);
        Plantage plantage2 = new Plantage();
        plantage2.setId(plantage1.getId());
        assertThat(plantage1).isEqualTo(plantage2);
        plantage2.setId(2L);
        assertThat(plantage1).isNotEqualTo(plantage2);
        plantage1.setId(null);
        assertThat(plantage1).isNotEqualTo(plantage2);
    }
}
