package com.pfa.smartwatering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfa.smartwatering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeSolTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeSol.class);
        TypeSol typeSol1 = new TypeSol();
        typeSol1.setId(1L);
        TypeSol typeSol2 = new TypeSol();
        typeSol2.setId(typeSol1.getId());
        assertThat(typeSol1).isEqualTo(typeSol2);
        typeSol2.setId(2L);
        assertThat(typeSol1).isNotEqualTo(typeSol2);
        typeSol1.setId(null);
        assertThat(typeSol1).isNotEqualTo(typeSol2);
    }
}
