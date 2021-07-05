package org.apiaddicts.apitools.apigen.archetypecore.core.responses.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.util.UriComponentsBuilder;

@Data
public class Paging {

    @JsonProperty("init")
    private Integer init;
    @JsonProperty("limit")
    private Integer limit;
    @JsonProperty("total")
    private Long total;

    @JsonProperty("total_pages")
    private Integer totalPages;
    @JsonProperty("num_page")
    private Integer numPage;

    @JsonProperty("links")
    private Links links;

    public static Paging from(Integer init, Integer limit, Long total) {

        Paging paging = new Paging();

        if (total != null && init != null && limit != null) {
            if (limit <= 0 || total <= 0) {
                paging.totalPages = 1;
            }
            else {
                paging.totalPages = (int) Math.ceil((double) total / limit);
            }

            paging.total = total;
            paging.numPage = init / limit + 1;
        }

        if (init != null && limit != null) {
            paging.init = init;
            paging.limit = limit;
        }

        return paging;
    }

    public void addLinks(UriComponentsBuilder baseLink) {
        if (init != null && limit != null) {
            this.links = Links.buildLinksMetadata(init, limit, total, baseLink);
        }
    }
}
