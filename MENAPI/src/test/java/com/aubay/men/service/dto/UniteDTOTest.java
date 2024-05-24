package com.aubay.men.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UniteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniteDTO.class);
        UniteDTO uniteDTO1 = new UniteDTO();
        uniteDTO1.setId(1L);
        UniteDTO uniteDTO2 = new UniteDTO();
        assertThat(uniteDTO1).isNotEqualTo(uniteDTO2);
        uniteDTO2.setId(uniteDTO1.getId());
        assertThat(uniteDTO1).isEqualTo(uniteDTO2);
        uniteDTO2.setId(2L);
        assertThat(uniteDTO1).isNotEqualTo(uniteDTO2);
        uniteDTO1.setId(null);
        assertThat(uniteDTO1).isNotEqualTo(uniteDTO2);
    }
}
