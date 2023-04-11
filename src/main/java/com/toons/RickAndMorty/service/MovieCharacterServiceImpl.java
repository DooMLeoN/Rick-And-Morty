package com.toons.RickAndMorty.service;

import com.toons.RickAndMorty.dto.ApiResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MovieCharacterServiceImpl implements MovieCharacterService {
    private final HttpClient httpClient;

    public MovieCharacterServiceImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void syncExternalCharacter() {
        ApiResponseDto apiResponseDto = httpClient.get("https://rickandmortyapi.com/api/character",
                ApiResponseDto.class);
        while (apiResponseDto.getInfo().getNext() != null) {
            apiResponseDto = httpClient.get(apiResponseDto.getInfo().getNext(),
                    ApiResponseDto.class);
        }
    }
}
