package com.aubay.men.domain;

import static com.aubay.men.domain.CommunicationTestSamples.*;
import static com.aubay.men.domain.UniteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Communication.class);
        Communication communication1 = getCommunicationSample1();
        Communication communication2 = new Communication();
        assertThat(communication1).isNotEqualTo(communication2);

        communication2.setId(communication1.getId());
        assertThat(communication1).isEqualTo(communication2);

        communication2 = getCommunicationSample2();
        assertThat(communication1).isNotEqualTo(communication2);
    }

    @Test
    void uniteTest() throws Exception {
        Communication communication = getCommunicationRandomSampleGenerator();
        Unite uniteBack = getUniteRandomSampleGenerator();

        communication.setUnite(uniteBack);
        assertThat(communication.getUnite()).isEqualTo(uniteBack);

        communication.unite(null);
        assertThat(communication.getUnite()).isNull();
    }
}
