package com.aubay.men.service.mapper;

import static com.aubay.men.domain.MaterielProfilAsserts.*;
import static com.aubay.men.domain.MaterielProfilTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaterielProfilMapperTest {

    private MaterielProfilMapper materielProfilMapper;

    @BeforeEach
    void setUp() {
        materielProfilMapper = new MaterielProfilMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaterielProfilSample1();
        var actual = materielProfilMapper.toEntity(materielProfilMapper.toDto(expected));
        assertMaterielProfilAllPropertiesEquals(expected, actual);
    }
}
