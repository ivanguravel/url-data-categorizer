package org.example.crawler;

import akka.actor.ActorRef;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.Route;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import org.example.crawler.model.dto.LinkId;
import org.example.crawler.model.dto.LinksDto;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * Defines server for further usage with the related routes
 */
public class AppServer extends HttpApp {

    private final ActorRef crawler;

    public AppServer(ActorRef crawler) {
        this.crawler = crawler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Route routes() {
        return path("links", this::handleLinks);
    }

    /**
     * Creates main routes for further usage inside app
     *
     * @return list of configured http routes and related behaviour during the unexpected errors
     */
    @SuppressWarnings("unchecked")
    private Route handleLinks() {
        return route(post(() -> entity(Jackson.unmarshaller(LinksDto.class), linksDto -> {
            final CompletionStage<List<LinkId>> linkParsingPerformed = PatternsCS
                    .ask(crawler,
                            linksDto,
                            new Timeout(Duration.create(15, TimeUnit.SECONDS)))
                    .thenApply(object -> (List<LinkId>) object);

            return onSuccess(() -> linkParsingPerformed,
                    performed -> complete(StatusCodes.CREATED, performed, Jackson.marshaller()));
        })));
    }
}
