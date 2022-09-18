package org.example.crawler.model;

import org.example.crawler.model.dto.LinkId;

/**
 * Contains information about link, it's unique id inside system and parsed text without html tags
 *
 */
public class LinkInfo extends LinkId {
    private final String text;


    public LinkInfo(String link, String id, String text) {
        super(link, id);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
