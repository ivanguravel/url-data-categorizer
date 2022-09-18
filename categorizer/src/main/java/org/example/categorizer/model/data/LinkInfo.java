package org.example.categorizer.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains information about link, it's unique id inside system and parsed text without html tags
 *
 */
public class LinkInfo {
    private final String text;
    private final String link;
    private final String id;


    public LinkInfo(@JsonProperty("link") String link,
                    @JsonProperty("id") String id,
                    @JsonProperty("text") String text) {
        this.link = link;
        this.id = id;
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
