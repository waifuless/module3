package com.epam.esm.gcs.business.converter;

import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;
import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext.StateForSearchDto;
import com.epam.esm.gcs.persistence.model.GiftCertificateModel;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext.StateForSearchModel;
import com.epam.esm.gcs.persistence.tableproperty.SortDirection;
import lombok.Getter;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class GiftCertificateContextDtoConverter
        extends AbstractConverter<GiftCertificateDtoContext, GiftCertificateModelContext> {

    @Override
    protected GiftCertificateModelContext convert(GiftCertificateDtoContext source) {
        return GiftCertificateModelContext.builder()
                .tagNames(source.getTagName())
                .searchValue(source.getSearchValue())
                .sortDirectionByFieldNameMap(parseSortListToMap(source.getSortBy()))
                .state(parseState(source.getState()))
                .build();
    }

    private Map<String, SortDirection> parseSortListToMap(List<String> sortBy) {
        if (sortBy == null) {
            return null;
        }

        Map<String, SortDirection> sortByParsed = new LinkedHashMap<>();
        sortBy.forEach(sortByParam -> {
            int delimiterIndex = sortByParam.lastIndexOf('.');
            String fieldAssociation = sortByParam.substring(0, delimiterIndex);
            String sortDirectionAssociation = sortByParam.substring(delimiterIndex + 1);
            FieldNameAssociation.findFieldNameByAssociation(fieldAssociation).ifPresent(field -> {
                SortDirection direction = SortDirection.valueOfIgnoreCase(sortDirectionAssociation)
                        .orElse(SortDirection.ASC);
                sortByParsed.put(field, direction);
            });
        });
        return sortByParsed;
    }

    private StateForSearchModel parseState(StateForSearchDto state) {
        if (state == null) {
            return StateForSearchModel.ACTUAL;
        }
        return StateForSearchModel.valueOf(state.name());
    }

    @Getter
    public enum FieldNameAssociation {

        NAME("name", "name"),
        CREATE_DATE("create_date", "createDate");

        private final String association;
        private final String fieldName;

        FieldNameAssociation(String association, String fieldName) {
            this.association = association;
            try {
                validateFieldExistence(fieldName);
                this.fieldName = fieldName;
            } catch (NoSuchFieldException e) {
                throw new Error(e);
            }
        }

        public static Optional<String> findFieldNameByAssociation(String association) {
            return Arrays.stream(values())
                    .filter(fieldNameAssociation ->
                            fieldNameAssociation.getAssociation().equalsIgnoreCase(association))
                    .map(FieldNameAssociation::getFieldName)
                    .findAny();
        }

        private void validateFieldExistence(String fieldName) throws NoSuchFieldException {
            GiftCertificateModel.class.getDeclaredField(fieldName);
        }
    }
}
