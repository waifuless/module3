package com.epam.esm.gcs.business.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class UserWithMostlyUsedTagsDto {

    private AppUserDto appUserModel;
    private List<TagDto> mostUsedTags;
}
