package com.aubay.men.service.mapper;

import static com.aubay.men.domain.DonneesReferencesAsserts.*;
import static com.aubay.men.domain.DonneesReferencesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DonneesReferencesMapperTest {

    private DonneesReferencesMapper donneesReferencesMapper;

    @BeforeEach
    void setUp() {
        donneesReferencesMapper = new DonneesReferencesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDonneesReferencesSample1();
        var actual = donneesReferencesMapper.toEntity(donneesReferencesMapper.toDto(expected));
        assertDonneesReferencesAllPropertiesEquals(expected, actual);
    }
}
