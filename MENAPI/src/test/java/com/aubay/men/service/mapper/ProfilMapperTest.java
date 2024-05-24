package com.aubay.men.service.mapper;

import static com.aubay.men.domain.ProfilAsserts.*;
import static com.aubay.men.domain.ProfilTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfilMapperTest {

    private ProfilMapper profilMapper;

    @BeforeEach
    void setUp() {
        profilMapper = new ProfilMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfilSample1();
        var actual = profilMapper.toEntity(profilMapper.toDto(expected));
        assertProfilAllPropertiesEquals(expected, actual);
    }
}
