package com.aubay.men.service.mapper;

import static com.aubay.men.domain.PrestationAsserts.*;
import static com.aubay.men.domain.PrestationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrestationMapperTest {

    private PrestationMapper prestationMapper;

    @BeforeEach
    void setUp() {
        prestationMapper = new PrestationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPrestationSample1();
        var actual = prestationMapper.toEntity(prestationMapper.toDto(expected));
        assertPrestationAllPropertiesEquals(expected, actual);
    }
}
