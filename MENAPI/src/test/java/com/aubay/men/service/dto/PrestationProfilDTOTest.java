package com.aubay.men.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrestationProfilDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrestationProfilDTO.class);
        PrestationProfilDTO prestationProfilDTO1 = new PrestationProfilDTO();
        prestationProfilDTO1.setId(1L);
        PrestationProfilDTO prestationProfilDTO2 = new PrestationProfilDTO();
        assertThat(prestationProfilDTO1).isNotEqualTo(prestationProfilDTO2);
        prestationProfilDTO2.setId(prestationProfilDTO1.getId());
        assertThat(prestationProfilDTO1).isEqualTo(prestationProfilDTO2);
        prestationProfilDTO2.setId(2L);
        assertThat(prestationProfilDTO1).isNotEqualTo(prestationProfilDTO2);
        prestationProfilDTO1.setId(null);
        assertThat(prestationProfilDTO1).isNotEqualTo(prestationProfilDTO2);
    }
}
