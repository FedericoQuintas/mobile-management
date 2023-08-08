package com.challenge.mobilemanagement.domain;

public record Version(Integer version) implements Comparable<Version> {
    public static Version of(int version) {
        return new Version(version);
    }

    @Override
    public int compareTo(Version v2) {
        return Integer.compare(version, v2.version);
    }
}
