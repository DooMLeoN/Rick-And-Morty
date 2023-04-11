package com.toons.RickAndMorty.dto.maper;

import com.toons.RickAndMorty.dto.ApiCharacterDto;
import com.toons.RickAndMorty.model.Gender;
import com.toons.RickAndMorty.model.MovieCharacter;
import com.toons.RickAndMorty.model.Status;
import org.springframework.stereotype.Component;

@Component
public class MovieCharacterMapper {
    public MovieCharacter parsMovieCharacterMapper(ApiCharacterDto apiCharacterDto) {
        MovieCharacter movieCharacter = new MovieCharacter();
        movieCharacter.setExternalId(apiCharacterDto.getId());
        movieCharacter.setName(apiCharacterDto.getName());
        movieCharacter.setGender(Gender.valueOf(apiCharacterDto.getGender()));
        movieCharacter.setStatus(Status.valueOf(apiCharacterDto.getStatus()));
        return movieCharacter;
    }
}
