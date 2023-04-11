package com.toons.RickAndMorty.service;

import com.toons.RickAndMorty.dto.ApiCharacterDto;
import com.toons.RickAndMorty.dto.ApiResponseDto;
import com.toons.RickAndMorty.dto.maper.MovieCharacterMapper;
import com.toons.RickAndMorty.model.MovieCharacter;
import com.toons.RickAndMorty.repository.MovieCharacterRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@Service
public class MovieCharacterServiceImpl implements MovieCharacterService {
    private final HttpClient httpClient;
    private final MovieCharacterRepository movieCharacterRepository;
    private final MovieCharacterMapper mapper;

    public MovieCharacterServiceImpl(HttpClient httpClient,
                                     MovieCharacterRepository movieCharacterRepository, MovieCharacterMapper mapper) {
        this.httpClient = httpClient;
        this.movieCharacterRepository = movieCharacterRepository;
        this.mapper = mapper;
    }

    @Override
    public void syncExternalCharacter() {
        ApiResponseDto apiResponseDto = httpClient.get("https://rickandmortyapi.com/api/character",
                ApiResponseDto.class);
        saveDtoToDB(apiResponseDto);
        while (apiResponseDto.getInfo().getNext() != null) {
            apiResponseDto = httpClient.get(apiResponseDto.getInfo().getNext(),
                    ApiResponseDto.class);
            saveDtoToDB(apiResponseDto);
        }
    }

    private void saveDtoToDB(ApiResponseDto apiResponseDto) {
        Map<Long, ApiCharacterDto> externalDto = Arrays.stream(apiResponseDto.getResult())
                .collect(Collectors.toMap(ApiCharacterDto::getId, Function.identity()));
        Set<Long> externalIds = externalDto.keySet();
        List<MovieCharacter> existingCharacters = movieCharacterRepository.findAllByExternalIdIn(externalIds);
        Map<Long, MovieCharacter> existingCharactersWithIds = existingCharacters.stream()
                .collect(Collectors.toMap(MovieCharacter::getExternalId, Function.identity()));
        Set<Long> existingIds = existingCharactersWithIds.keySet();
        externalIds.removeAll(existingIds);
        List<MovieCharacter> charactersToSave = existingIds.stream()
                .map(i -> mapper.parsMovieCharacterMapper(externalDto.get(i))).toList();
        movieCharacterRepository.saveAll(charactersToSave);
    }
}
