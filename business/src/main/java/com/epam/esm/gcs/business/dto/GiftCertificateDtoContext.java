package com.epam.esm.gcs.business.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class GiftCertificateDtoContext {

    private Set<
            @Length(max = 100, message = "{parameter.length.max}")
                    String> tagName;

    @Length(max = 200, message = "{parameter.length.max}")
    private String searchValue;

    private List<
            @Pattern(regexp = "^\\w{1,100}\\.((asc)|(desc))$", message = "{sortBy.pattern.should.match}")
                    String> sortBy;

    private StateForSearchDto state;

    public enum StateForSearchDto {
        ACTUAL, ARCHIVED, ALL;
    }
}
