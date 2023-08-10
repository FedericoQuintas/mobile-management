package com.challenge.mobilemanagement.domain;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class VersionTest {

    @Test
    public void comparesBasedOnVersionNumber() {
        assertEquals(-1, Version.of(1).compareTo(Version.of(2)));
        assertEquals(0, Version.of(1).compareTo(Version.of(1)));
        assertEquals(1, Version.of(2).compareTo(Version.of(1)));
    }

}
