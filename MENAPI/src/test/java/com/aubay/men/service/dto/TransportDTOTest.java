package com.aubay.men.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransportDTO.class);
        TransportDTO transportDTO1 = new TransportDTO();
        transportDTO1.setId(1L);
        TransportDTO transportDTO2 = new TransportDTO();
        assertThat(transportDTO1).isNotEqualTo(transportDTO2);
        transportDTO2.setId(transportDTO1.getId());
        assertThat(transportDTO1).isEqualTo(transportDTO2);
        transportDTO2.setId(2L);
        assertThat(transportDTO1).isNotEqualTo(transportDTO2);
        transportDTO1.setId(null);
        assertThat(transportDTO1).isNotEqualTo(transportDTO2);
    }
}
