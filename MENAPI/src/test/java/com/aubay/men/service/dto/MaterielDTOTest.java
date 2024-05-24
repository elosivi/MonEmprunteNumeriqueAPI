package com.aubay.men.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterielDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterielDTO.class);
        MaterielDTO materielDTO1 = new MaterielDTO();
        materielDTO1.setId(1L);
        MaterielDTO materielDTO2 = new MaterielDTO();
        assertThat(materielDTO1).isNotEqualTo(materielDTO2);
        materielDTO2.setId(materielDTO1.getId());
        assertThat(materielDTO1).isEqualTo(materielDTO2);
        materielDTO2.setId(2L);
        assertThat(materielDTO1).isNotEqualTo(materielDTO2);
        materielDTO1.setId(null);
        assertThat(materielDTO1).isNotEqualTo(materielDTO2);
    }
}
