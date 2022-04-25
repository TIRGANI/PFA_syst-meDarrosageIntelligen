package com.pfa.smartwatering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfa.smartwatering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoitierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Boitier.class);
        Boitier boitier1 = new Boitier();
        boitier1.setId(1L);
        Boitier boitier2 = new Boitier();
        boitier2.setId(boitier1.getId());
        assertThat(boitier1).isEqualTo(boitier2);
        boitier2.setId(2L);
        assertThat(boitier1).isNotEqualTo(boitier2);
        boitier1.setId(null);
        assertThat(boitier1).isNotEqualTo(boitier2);
    }
}
