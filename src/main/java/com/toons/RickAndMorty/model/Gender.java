package com.toons.RickAndMorty.model;

enum Gender {
        FEMALE("Female"),
        MALE("Mail"),
        GENDERLESS("Genderless"),
        UNKNOWN("unknown");
        private String value;

        Gender(String value) {
            this.value = value;
        }
    }