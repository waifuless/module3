package com.epam.esm.gcs.web.assembler.impl;

import com.epam.esm.gcs.business.dto.PageDto;
import com.epam.esm.gcs.business.dto.PageParamsDto;
import com.epam.esm.gcs.web.assembler.PagedRepresentationAssembler;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Component
public class PagedRepresentationAssemblerImpl<T extends RepresentationModel<?>>
        implements PagedRepresentationAssembler<T> {

    private static final String PAGE_NUMBER_PARAM = "page";

    @Override
    public PagedModel<T> toModel(PageDto<T> page, RepresentationModelAssembler<T, T> assembler) {
        List<T> content = page.getContent();
        content.forEach(assembler::toModel);
        PageMetadata pageMetadata = constructMetaData(page);
        List<Link> links = constructLinks(pageMetadata);
        return PagedModel.of(page.getContent(), pageMetadata, links);
    }

    private PageMetadata constructMetaData(PageDto<T> page) {
        PageParamsDto pageParams = page.getPageParams();
        long size = pageParams.getSize();
        long number = pageParams.getPage();
        long totalElements = page.getTotalCount();
        long totalPages = totalElements / size + ((totalElements % size != 0) ? 1 : 0);
        return new PageMetadata(size, number, totalElements, totalPages);
    }

    private List<Link> constructLinks(PageMetadata pageMetadata) {
        List<Link> links = new ArrayList<>();
        ServletUriComponentsBuilder baseUriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();

        Link selfLink = Link.of(baseUriComponentsBuilder.toUriString(), IanaLinkRelations.SELF);
        links.add(selfLink);

        if (pageMetadata.getTotalPages() > 0) {
            Link firstLink = constructLink(baseUriComponentsBuilder, 1, IanaLinkRelations.FIRST);
            links.add(firstLink);

            Link lastLink = constructLink(baseUriComponentsBuilder, pageMetadata.getTotalPages(),
                    IanaLinkRelations.LAST);
            links.add(lastLink);

            long currentPage = pageMetadata.getNumber();
            if (currentPage <= pageMetadata.getTotalPages()) {
                if (currentPage > 1) {
                    Link previousLink =
                            constructLink(baseUriComponentsBuilder, currentPage - 1, IanaLinkRelations.PREVIOUS);
                    links.add(previousLink);
                }

                if (currentPage < pageMetadata.getTotalPages()) {
                    Link nextLink =
                            constructLink(baseUriComponentsBuilder, currentPage + 1, IanaLinkRelations.NEXT);
                    links.add(nextLink);
                }
            }
        }
        return links;
    }

    private Link constructLink(ServletUriComponentsBuilder baseUriComponentsBuilder, long page,
                               LinkRelation linkRelation) {
        String previousUri = baseUriComponentsBuilder
                .replaceQueryParam(PAGE_NUMBER_PARAM, page)
                .toUriString();
        return Link.of(previousUri, linkRelation);
    }
}
