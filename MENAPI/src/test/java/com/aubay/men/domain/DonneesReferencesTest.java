package com.aubay.men.domain;

import static com.aubay.men.domain.DonneesReferencesTestSamples.*;
import static com.aubay.men.domain.UniteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DonneesReferencesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DonneesReferences.class);
        DonneesReferences donneesReferences1 = getDonneesReferencesSample1();
        DonneesReferences donneesReferences2 = new DonneesReferences();
        assertThat(donneesReferences1).isNotEqualTo(donneesReferences2);

        donneesReferences2.setId(donneesReferences1.getId());
        assertThat(donneesReferences1).isEqualTo(donneesReferences2);

        donneesReferences2 = getDonneesReferencesSample2();
        assertThat(donneesReferences1).isNotEqualTo(donneesReferences2);
    }

    @Test
    void uniteTest() throws Exception {
        DonneesReferences donneesReferences = getDonneesReferencesRandomSampleGenerator();
        Unite uniteBack = getUniteRandomSampleGenerator();

        donneesReferences.setUnite(uniteBack);
        assertThat(donneesReferences.getUnite()).isEqualTo(uniteBack);

        donneesReferences.unite(null);
        assertThat(donneesReferences.getUnite()).isNull();
    }

    @Test
    void temporaliteTest() throws Exception {
        DonneesReferences donneesReferences = getDonneesReferencesRandomSampleGenerator();
        Unite uniteBack = getUniteRandomSampleGenerator();

        donneesReferences.setTemporalite(uniteBack);
        assertThat(donneesReferences.getTemporalite()).isEqualTo(uniteBack);

        donneesReferences.temporalite(null);
        assertThat(donneesReferences.getTemporalite()).isNull();
    }
}
