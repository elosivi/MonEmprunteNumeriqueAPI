package com.aubay.men.service.mapper;

import static com.aubay.men.domain.FonctionAsserts.*;
import static com.aubay.men.domain.FonctionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FonctionMapperTest {

    private FonctionMapper fonctionMapper;

    @BeforeEach
    void setUp() {
        fonctionMapper = new FonctionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFonctionSample1();
        var actual = fonctionMapper.toEntity(fonctionMapper.toDto(expected));
        assertFonctionAllPropertiesEquals(expected, actual);
    }
}
