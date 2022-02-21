package com.epam.esm.gcs.web.error;

import com.epam.esm.gcs.business.exception.EntitiesArchivedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.internal.Pair;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ErrorResponseEntitiesArchived extends ErrorResponse {

    private List<ArchivedToActual> archivedToActualList;
    private List<Long> unavailable;

    public ErrorResponseEntitiesArchived(String errorMessage, HttpStatus errorCode, EntitiesArchivedException ex) {
        super(errorMessage, errorCode, ex.getDtoClass());

        this.archivedToActualList = convertPairToClass(ex.getArchivedToActual());
        this.unavailable = ex.getUnavailable();
    }

    private List<ArchivedToActual> convertPairToClass(List<Pair<Long, Long>> archivedToActualList) {
        return archivedToActualList.stream()
                .map(pair -> new ArchivedToActual(pair.getLeft(), pair.getRight()))
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    public static class ArchivedToActual {

        private Long archivedId;
        private Long actualId;
    }
}
