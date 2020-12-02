package net.cloudappi.apigen.archetypecore.core.responses.metadata;

import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Data
public class Links {
    private Link self;
    private Link previous;
    private Link next;
    private Link first;
    private Link last;

    @Data
    public static class Link {
        private String href;

        public Link(String link) {
            href = link;
        }
    }

    public static Links buildLinksMetadata(Integer init, Integer limit, Long total, UriComponentsBuilder baseLink) {
        UriComponents uriComponents = baseLink.build();
        String query = getUrl(uriComponents);
        Links links = new Links();

        long nextInit = init + limit;
        uriComponents = updateQueryParams(uriComponents, baseLink, String.valueOf(nextInit));
        links.next = new Link(getUrl(uriComponents));

        if (total != null) {
            if (total <= nextInit) {
                links.next = null;
            }

            long lastInit = total / limit * limit;
            if (lastInit >= total) lastInit -= limit;
            uriComponents = updateQueryParams(uriComponents, baseLink, String.valueOf(lastInit));
            links.last = new Link(getUrl(uriComponents));
        }

        links.self = new Link(query);

        if (init > 0) {
            long previousInit = Math.max(init - limit, 0);
            uriComponents = updateQueryParams(uriComponents, baseLink, String.valueOf(previousInit));
            links.previous = new Link(getUrl(uriComponents));
        }

        uriComponents = updateQueryParams(uriComponents, baseLink, String.valueOf(0));
        Link first = new Link(getUrl(uriComponents));
        links.first = first;

        return links;
    }

    private static String getUrl(UriComponents uriComponents) {
        if(uriComponents.getQuery() == null || uriComponents.getQuery().isEmpty()){
            return uriComponents.getPath();
        }else{
            return uriComponents.getPath() + "?" + uriComponents.getQuery();
        }
    }

    private static UriComponents updateQueryParams(UriComponents uriComponents, UriComponentsBuilder baseLink, String value) {
        MultiValueMap<String, String> args = new LinkedMultiValueMap<>(uriComponents.getQueryParams());
        args.remove("$init");
        args.add("$init", value);
        return baseLink.replaceQueryParams(args).build();
    }


}
