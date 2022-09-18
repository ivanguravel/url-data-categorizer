package org.example.crawler.model.dto;

/**
 * Contains information about link and it's unique id inside system.
 *
 */
public class LinkId {
    private final String link;
    private final String id;

    public LinkId(String link, String id) {
        this.link = link;
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public String getId() {
        return id;
    }
}
