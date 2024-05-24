package com.aubay.men.service.mapper;

import static com.aubay.men.domain.UniteAsserts.*;
import static com.aubay.men.domain.UniteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UniteMapperTest {

    private UniteMapper uniteMapper;

    @BeforeEach
    void setUp() {
        uniteMapper = new UniteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUniteSample1();
        var actual = uniteMapper.toEntity(uniteMapper.toDto(expected));
        assertUniteAllPropertiesEquals(expected, actual);
    }
}
