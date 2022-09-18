package org.example.crawler;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.FI;
import org.example.crawler.model.LinkInfo;
import org.example.crawler.model.dto.LinkId;
import org.example.crawler.model.dto.LinksDto;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;


/**
 * Crawler for http pages which is created based on Actor model
 */
public class Crawler extends AbstractActor {

    private final ActorRef storage;

    static public Props props(ActorRef storage) {
        return Props.create(Crawler.class, () -> new Crawler(storage));
    }

    public Crawler(ActorRef storage) {
        this.storage = storage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(LinksDto.class, processLinks())
                .build();
    }

    /**
     * Processes list of links for crawling and returns the result to the actor which is sent the request
     */
    private FI.UnitApply<LinksDto> processLinks() {
        return linksDto -> {
            requireNonNull(linksDto);
            final List<String> links = linksDto.getLinks();
            requireNonNull(links);

            List<LinkId> processedLinks = processLinks(links);
            sender().tell(processedLinks, getSelf());
        };
    }

    /**
     * Runs the process of crawling for list of links
     *
     * @param links for further processing
     */
    private List<LinkId> processLinks(List<String> links) {
        List<LinkId> result = new ArrayList<>(links.size());
        String text;
        LinkId linkId;
        // could be replaced with parallel stream in case of many links are going from the request at the same time
        for (String link : links) {
            text = safeGetTextFromLink(link);
            linkId = new LinkId(link, generateUniqOperationId());
            storage.tell(new LinkInfo(link, linkId.getId(), text), getSelf());
            result.add(linkId);
        }
        return result;
    }

    /**
     * Gets the text from http link
     *
     * @param link for further getting text from it
     *
     * @return text from http link
     */
    private String safeGetTextFromLink(String link) {
        try {
            return Jsoup.connect(link).get().body().text();
        } catch (IOException e) {
            throw new UnsupportedOperationException(String.format("can't parse link %s", link), e);
        }
    }

    /**
     * Generates unique value which will be used as an id for operations with links and their text.
     *
     * @return unique id
     */
    private static String generateUniqOperationId() {
        return UUID.randomUUID().toString().substring(0, 10);
    }
}
