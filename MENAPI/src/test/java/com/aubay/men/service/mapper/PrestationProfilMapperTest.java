package com.aubay.men.service.mapper;

import static com.aubay.men.domain.PrestationProfilAsserts.*;
import static com.aubay.men.domain.PrestationProfilTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrestationProfilMapperTest {

    private PrestationProfilMapper prestationProfilMapper;

    @BeforeEach
    void setUp() {
        prestationProfilMapper = new PrestationProfilMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPrestationProfilSample1();
        var actual = prestationProfilMapper.toEntity(prestationProfilMapper.toDto(expected));
        assertPrestationProfilAllPropertiesEquals(expected, actual);
    }
}
