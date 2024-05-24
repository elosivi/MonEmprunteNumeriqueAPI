package com.aubay.men.service.mapper;

import static com.aubay.men.domain.TransportProfilAsserts.*;
import static com.aubay.men.domain.TransportProfilTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransportProfilMapperTest {

    private TransportProfilMapper transportProfilMapper;

    @BeforeEach
    void setUp() {
        transportProfilMapper = new TransportProfilMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransportProfilSample1();
        var actual = transportProfilMapper.toEntity(transportProfilMapper.toDto(expected));
        assertTransportProfilAllPropertiesEquals(expected, actual);
    }
}
