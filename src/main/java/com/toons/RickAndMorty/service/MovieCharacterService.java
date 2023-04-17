package com.toons.RickAndMorty.service;

import com.toons.RickAndMorty.model.MovieCharacter;

import java.util.List;

public interface MovieCharacterService {
    void syncExternalCharacter();

    MovieCharacter getRandomCharacter();

    List<MovieCharacter> findAllByNameContaining(String namePart);

}
