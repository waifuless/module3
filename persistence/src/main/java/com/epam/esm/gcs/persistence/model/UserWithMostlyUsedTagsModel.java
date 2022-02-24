package com.epam.esm.gcs.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithMostlyUsedTagsModel {

    private AppUserModel appUserModel;
    private List<TagModel> mostUsedTags;
}
