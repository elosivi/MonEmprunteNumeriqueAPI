package com.aubay.men.service.mapper;

import static com.aubay.men.domain.CommunicationAsserts.*;
import static com.aubay.men.domain.CommunicationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommunicationMapperTest {

    private CommunicationMapper communicationMapper;

    @BeforeEach
    void setUp() {
        communicationMapper = new CommunicationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCommunicationSample1();
        var actual = communicationMapper.toEntity(communicationMapper.toDto(expected));
        assertCommunicationAllPropertiesEquals(expected, actual);
    }
}
