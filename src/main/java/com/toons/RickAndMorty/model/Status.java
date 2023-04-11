package com.toons.RickAndMorty.model;

public enum Status {
        DEAD("Dead"),
        ALIVE("Alive"),
        UNKNOWN("unknown");

        private String value;

        Status(String value) {
            this.value = value;
        }
    }