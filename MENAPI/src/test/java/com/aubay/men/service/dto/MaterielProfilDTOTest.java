package com.aubay.men.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterielProfilDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterielProfilDTO.class);
        MaterielProfilDTO materielProfilDTO1 = new MaterielProfilDTO();
        materielProfilDTO1.setId(1L);
        MaterielProfilDTO materielProfilDTO2 = new MaterielProfilDTO();
        assertThat(materielProfilDTO1).isNotEqualTo(materielProfilDTO2);
        materielProfilDTO2.setId(materielProfilDTO1.getId());
        assertThat(materielProfilDTO1).isEqualTo(materielProfilDTO2);
        materielProfilDTO2.setId(2L);
        assertThat(materielProfilDTO1).isNotEqualTo(materielProfilDTO2);
        materielProfilDTO1.setId(null);
        assertThat(materielProfilDTO1).isNotEqualTo(materielProfilDTO2);
    }
}
