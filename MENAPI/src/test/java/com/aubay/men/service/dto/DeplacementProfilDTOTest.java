package com.aubay.men.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeplacementProfilDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeplacementProfilDTO.class);
        DeplacementProfilDTO deplacementProfilDTO1 = new DeplacementProfilDTO();
        deplacementProfilDTO1.setId(1L);
        DeplacementProfilDTO deplacementProfilDTO2 = new DeplacementProfilDTO();
        assertThat(deplacementProfilDTO1).isNotEqualTo(deplacementProfilDTO2);
        deplacementProfilDTO2.setId(deplacementProfilDTO1.getId());
        assertThat(deplacementProfilDTO1).isEqualTo(deplacementProfilDTO2);
        deplacementProfilDTO2.setId(2L);
        assertThat(deplacementProfilDTO1).isNotEqualTo(deplacementProfilDTO2);
        deplacementProfilDTO1.setId(null);
        assertThat(deplacementProfilDTO1).isNotEqualTo(deplacementProfilDTO2);
    }
}
