package com.aubay.men.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrestationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrestationDTO.class);
        PrestationDTO prestationDTO1 = new PrestationDTO();
        prestationDTO1.setId(1L);
        PrestationDTO prestationDTO2 = new PrestationDTO();
        assertThat(prestationDTO1).isNotEqualTo(prestationDTO2);
        prestationDTO2.setId(prestationDTO1.getId());
        assertThat(prestationDTO1).isEqualTo(prestationDTO2);
        prestationDTO2.setId(2L);
        assertThat(prestationDTO1).isNotEqualTo(prestationDTO2);
        prestationDTO1.setId(null);
        assertThat(prestationDTO1).isNotEqualTo(prestationDTO2);
    }
}
