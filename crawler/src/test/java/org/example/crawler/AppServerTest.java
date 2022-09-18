package org.example.crawler;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.testkit.TestRoute;
import org.example.crawler.model.dto.LinksDto;
import org.example.crawler.store.Store;
import org.example.crawler.util.JsonUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class AppServerTest extends JUnitRouteTest {

    private static String TEST_DIR = "testDir";

    private TestRoute appRoute;
    private ActorSystem system;

    @Override
    public FiniteDuration awaitDuration() {
        return FiniteDuration.create(15, TimeUnit.SECONDS);
    }

    @Before
    public void setup() {
        system = ActorSystem.create("system");
        final ActorRef storage =
                system.actorOf(Store.props(TEST_DIR), "storage");
        final ActorRef crawler =
                system.actorOf(Crawler.props(storage), "crawler");

        Route routes = new AppServer(crawler).routes();
        appRoute = testRoute(routes);
    }

    @Test
    public void testSimpleScenario() {
        appRoute.run(HttpRequest.POST("/links")
                .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, createLinksDto())))
                .assertStatusCode(201);
    }

    @After
    public void cleanUp() {
        system.terminate();
        deleteDirectory(new File(TEST_DIR));
    }

    private static final String createLinksDto() {
        LinksDto dto = new LinksDto(Arrays.asList(
                "https://www.bbc.com", "https://www.msn.com/"
        ));

        return JsonUtils.safeConvertObjectToJson(dto);
    }

    void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}
