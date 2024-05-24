package com.aubay.men.service.mapper;

import static com.aubay.men.domain.DeplacementProfilAsserts.*;
import static com.aubay.men.domain.DeplacementProfilTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeplacementProfilMapperTest {

    private DeplacementProfilMapper deplacementProfilMapper;

    @BeforeEach
    void setUp() {
        deplacementProfilMapper = new DeplacementProfilMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDeplacementProfilSample1();
        var actual = deplacementProfilMapper.toEntity(deplacementProfilMapper.toDto(expected));
        assertDeplacementProfilAllPropertiesEquals(expected, actual);
    }
}
