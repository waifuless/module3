package com.epam.esm.gcs.business.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
public class GiftCertificateDtoContext {

    @Length(max = 100, message = "{entity.field.length.max}")
    private String tagName;

    @Length(max = 200, message = "{entity.field.length.max}")
    private String searchValue;

    private List<
            @Pattern(regexp = "^\\w{1,100}\\.((asc)|(desc))$", message = "{sortBy.pattern.should.match}")
                    String> sortBy;
}
