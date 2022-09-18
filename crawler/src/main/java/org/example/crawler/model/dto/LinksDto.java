package org.example.crawler.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Contains information about links for further crawling.
 *
 */
public class LinksDto {
    private List<String> links;

    public LinksDto(@JsonProperty("links") List<String> links) {
        this.links = links;
    }

    public List<String> getLinks() {
        return links;
    }
}
