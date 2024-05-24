package com.aubay.men.service.mapper;

import static com.aubay.men.domain.TransportAsserts.*;
import static com.aubay.men.domain.TransportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransportMapperTest {

    private TransportMapper transportMapper;

    @BeforeEach
    void setUp() {
        transportMapper = new TransportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTransportSample1();
        var actual = transportMapper.toEntity(transportMapper.toDto(expected));
        assertTransportAllPropertiesEquals(expected, actual);
    }
}
