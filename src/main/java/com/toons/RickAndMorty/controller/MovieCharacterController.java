package com.toons.RickAndMorty.controller;

import com.toons.RickAndMorty.dto.external.CharacterResponseDto;
import com.toons.RickAndMorty.dto.maper.MovieCharacterMapper;
import com.toons.RickAndMorty.model.MovieCharacter;
import com.toons.RickAndMorty.service.MovieCharacterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movie-characters")
public class MovieCharacterController {
    private final MovieCharacterService movieCharacterService;
    private final MovieCharacterMapper movieCharacterMapper;

    public MovieCharacterController(MovieCharacterService movieCharacterService,
                                    MovieCharacterMapper movieCharacterMapper) {
        this.movieCharacterService = movieCharacterService;
        this.movieCharacterMapper = movieCharacterMapper;
    }

    @GetMapping("/random")
    public CharacterResponseDto getRandom() {
        MovieCharacter randomCharacter = movieCharacterService.getRandomCharacter();
        return movieCharacterMapper.toResponseDto(randomCharacter);
    }

    @GetMapping("/by-name")
    public List<CharacterResponseDto> findByName(@RequestParam("name") String namePart) {
        List<MovieCharacter> characters = movieCharacterService.findAllByNameContaining(namePart);
        return characters.stream().map(movieCharacterMapper::toResponseDto).collect(Collectors.toList());
    }
}
