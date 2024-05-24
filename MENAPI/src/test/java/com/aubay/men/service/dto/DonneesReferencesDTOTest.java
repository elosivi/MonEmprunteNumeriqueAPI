package com.aubay.men.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DonneesReferencesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DonneesReferencesDTO.class);
        DonneesReferencesDTO donneesReferencesDTO1 = new DonneesReferencesDTO();
        donneesReferencesDTO1.setId(1L);
        DonneesReferencesDTO donneesReferencesDTO2 = new DonneesReferencesDTO();
        assertThat(donneesReferencesDTO1).isNotEqualTo(donneesReferencesDTO2);
        donneesReferencesDTO2.setId(donneesReferencesDTO1.getId());
        assertThat(donneesReferencesDTO1).isEqualTo(donneesReferencesDTO2);
        donneesReferencesDTO2.setId(2L);
        assertThat(donneesReferencesDTO1).isNotEqualTo(donneesReferencesDTO2);
        donneesReferencesDTO1.setId(null);
        assertThat(donneesReferencesDTO1).isNotEqualTo(donneesReferencesDTO2);
    }
}
