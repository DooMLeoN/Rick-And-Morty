package com.toons.RickAndMorty.dto.external;

import com.toons.RickAndMorty.model.Gender;
import com.toons.RickAndMorty.model.Status;
import lombok.Data;

@Data
public class CharacterResponseDto {
    private Long id;
    private Long externalId;
    private String name;
    private Status status;
    private Gender gender;
}
