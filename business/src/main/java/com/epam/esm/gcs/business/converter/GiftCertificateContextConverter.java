package com.epam.esm.gcs.business.converter;

import com.epam.esm.gcs.business.dto.GiftCertificateDtoContext;
import com.epam.esm.gcs.persistence.model.GiftCertificateModelContext;
import com.epam.esm.gcs.persistence.tableproperty.GiftCertificateColumn;
import com.epam.esm.gcs.persistence.tableproperty.SortDirection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GiftCertificateContextConverter
        extends AbstractConverter<GiftCertificateDtoContext, GiftCertificateModelContext> {

    @Override
    protected GiftCertificateModelContext convert(GiftCertificateDtoContext source) {
        return GiftCertificateModelContext.builder()
                .tagName(source.getTagName())
                .searchValue(source.getSearchValue())
                .sortBy(parseSortListToMap(source.getSortBy()))
                .build();
    }

    private Map<GiftCertificateColumn, SortDirection> parseSortListToMap(List<String> sortBy) {
        if (sortBy == null) {
            return null;
        }
        Map<GiftCertificateColumn, SortDirection> sortByParsed = new LinkedHashMap<>();
        sortBy.forEach(sortByParam -> {
            int delimiterIndex = sortByParam.lastIndexOf('.');
            String columnAssociation = sortByParam.substring(0, delimiterIndex);
            String sortDirectionAssociation = sortByParam.substring(delimiterIndex + 1);
            ColumnAssociation.findColumnByAssociation(columnAssociation).ifPresent(column -> {
                SortDirection direction = SortDirection.valueOfIgnoreCase(sortDirectionAssociation)
                        .orElse(SortDirection.ASC);
                sortByParsed.put(column, direction);
            });
        });
        return sortByParsed;
    }

    @RequiredArgsConstructor
    @Getter
    private enum ColumnAssociation {

        NAME("name", GiftCertificateColumn.NAME),
        CREATE_DATE("create_date", GiftCertificateColumn.CREATE_DATE);

        private final String association;
        private final GiftCertificateColumn column;

        public static Optional<GiftCertificateColumn> findColumnByAssociation(String association) {
            return Arrays.stream(values())
                    .filter(columnAssociation -> columnAssociation.getAssociation().equalsIgnoreCase(association))
                    .map(ColumnAssociation::getColumn)
                    .findAny();
        }
    }
}
