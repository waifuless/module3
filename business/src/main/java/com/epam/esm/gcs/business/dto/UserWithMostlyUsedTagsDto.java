package com.epam.esm.gcs.business.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class UserWithMostlyUsedTagsDto extends RepresentationModel<UserWithMostlyUsedTagsDto> {

    private AppUserDto appUserModel;
    private List<TagDto> mostUsedTags;
}
