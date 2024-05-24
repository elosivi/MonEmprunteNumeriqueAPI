package com.aubay.men.domain;

import static com.aubay.men.domain.UniteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UniteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Unite.class);
        Unite unite1 = getUniteSample1();
        Unite unite2 = new Unite();
        assertThat(unite1).isNotEqualTo(unite2);

        unite2.setId(unite1.getId());
        assertThat(unite1).isEqualTo(unite2);

        unite2 = getUniteSample2();
        assertThat(unite1).isNotEqualTo(unite2);
    }
}
