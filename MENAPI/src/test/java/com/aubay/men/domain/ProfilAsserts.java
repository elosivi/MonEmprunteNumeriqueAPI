package com.aubay.men.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfilAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfilAllPropertiesEquals(Profil expected, Profil actual) {
        assertProfilAutoGeneratedPropertiesEquals(expected, actual);
        assertProfilAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfilAllUpdatablePropertiesEquals(Profil expected, Profil actual) {
        assertProfilUpdatableFieldsEquals(expected, actual);
        assertProfilUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfilAutoGeneratedPropertiesEquals(Profil expected, Profil actual) {
        assertThat(expected)
            .as("Verify Profil auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfilUpdatableFieldsEquals(Profil expected, Profil actual) {
        assertThat(expected)
            .as("Verify Profil relevant properties")
            .satisfies(e -> assertThat(e.getNom()).as("check nom").isEqualTo(actual.getNom()))
            .satisfies(e -> assertThat(e.getPrenom()).as("check prenom").isEqualTo(actual.getPrenom()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfilUpdatableRelationshipsEquals(Profil expected, Profil actual) {
        assertThat(expected)
            .as("Verify Profil relationships")
            .satisfies(e -> assertThat(e.getFonction()).as("check fonction").isEqualTo(actual.getFonction()))
            .satisfies(e -> assertThat(e.getTransportProfils()).as("check transportProfils").isEqualTo(actual.getTransportProfils()))
            .satisfies(e -> assertThat(e.getDeplacementProfils()).as("check deplacementProfils").isEqualTo(actual.getDeplacementProfils()))
            .satisfies(e -> assertThat(e.getMaterielProfils()).as("check materielProfils").isEqualTo(actual.getMaterielProfils()));
    }
}
