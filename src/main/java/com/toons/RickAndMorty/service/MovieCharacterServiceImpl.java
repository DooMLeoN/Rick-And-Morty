package com.toons.RickAndMorty.service;

import com.toons.RickAndMorty.dto.external.ApiCharacterDto;
import com.toons.RickAndMorty.dto.external.ApiResponseDto;
import com.toons.RickAndMorty.dto.maper.MovieCharacterMapper;
import com.toons.RickAndMorty.model.MovieCharacter;
import com.toons.RickAndMorty.repository.MovieCharacterRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


    @Scheduled(cron = "0 0 8 * * ?")
    @Override
    public void syncExternalCharacter() {
        log.info("syncExternalCharacter method was invoked " + LocalDateTime.now());
        ApiResponseDto apiResponseDto = httpClient.get("https://rickandmortyapi.com/api/character",
                ApiResponseDto.class);
        saveDtosToDB(apiResponseDto);
        while (apiResponseDto.getInfo().getNext() != null) {
            apiResponseDto = httpClient.get(apiResponseDto.getInfo().getNext(),
                    ApiResponseDto.class);
            saveDtosToDB(apiResponseDto);
        }
    }

    @Override
    public MovieCharacter getRandomCharacter() {
        long count = movieCharacterRepository.count();
        long randomId = (long) (Math.random() * count);
        return movieCharacterRepository.findById(randomId).orElseThrow(() ->
                new RuntimeException("Cant find char by id " + randomId));
    }

    @Override
    public List<MovieCharacter> findAllByNameContaining(String namePart) {
        return movieCharacterRepository.findAllByNameContaining(namePart);
    }

    private void saveDtosToDB(ApiResponseDto apiResponseDto) {
        Map<Long, ApiCharacterDto> externalDto = Arrays.stream(apiResponseDto.getResults())
                .collect(Collectors.toMap(ApiCharacterDto::getId, Function.identity()));
        Set<Long> externalIds = externalDto.keySet();
        List<MovieCharacter> existingCharacters = movieCharacterRepository.findAllByExternalIdIn(externalIds);
        Map<Long, MovieCharacter> existingCharactersWithIds = existingCharacters.stream()
                .collect(Collectors.toMap(MovieCharacter::getExternalId, Function.identity()));
        Set<Long> existingIds = existingCharactersWithIds.keySet();
        externalIds.removeAll(existingIds);
        List<MovieCharacter> charactersToSave = externalIds.stream()
                .map(i -> mapper.parsMovieCharacterMapper(externalDto.get(i))).toList();
        movieCharacterRepository.saveAll(charactersToSave);
    }
}
