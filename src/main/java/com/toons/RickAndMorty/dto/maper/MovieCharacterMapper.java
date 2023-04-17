package com.toons.RickAndMorty.dto.maper;

import com.toons.RickAndMorty.dto.external.ApiCharacterDto;
import com.toons.RickAndMorty.dto.external.CharacterResponseDto;
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
        movieCharacter.setGender(Gender.valueOf(apiCharacterDto.getGender().toUpperCase()));
        movieCharacter.setStatus(Status.valueOf(apiCharacterDto.getStatus().toUpperCase()));
        return movieCharacter;
    }

    public CharacterResponseDto toResponseDto(MovieCharacter movieCharacter) {
        CharacterResponseDto characterResponseDto = new CharacterResponseDto();
        characterResponseDto.setId(movieCharacter.getId());
        characterResponseDto.setExternalId(movieCharacter.getExternalId());
        characterResponseDto.setStatus(movieCharacter.getStatus());
        characterResponseDto.setName(movieCharacter.getName());
        characterResponseDto.setGender(movieCharacter.getGender());
        return characterResponseDto;
    }
}
