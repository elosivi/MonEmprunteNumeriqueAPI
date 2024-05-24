package com.aubay.men.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.aubay.men.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransportProfilDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransportProfilDTO.class);
        TransportProfilDTO transportProfilDTO1 = new TransportProfilDTO();
        transportProfilDTO1.setId(1L);
        TransportProfilDTO transportProfilDTO2 = new TransportProfilDTO();
        assertThat(transportProfilDTO1).isNotEqualTo(transportProfilDTO2);
        transportProfilDTO2.setId(transportProfilDTO1.getId());
        assertThat(transportProfilDTO1).isEqualTo(transportProfilDTO2);
        transportProfilDTO2.setId(2L);
        assertThat(transportProfilDTO1).isNotEqualTo(transportProfilDTO2);
        transportProfilDTO1.setId(null);
        assertThat(transportProfilDTO1).isNotEqualTo(transportProfilDTO2);
    }
}
