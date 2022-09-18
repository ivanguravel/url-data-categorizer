package org.example.crawler;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.example.crawler.store.Store;

import java.io.InputStream;
import java.util.Properties;

/**
 * Main entrypoint to the crawler app
 */
public class AppStarter {

    private static final String BROADCAST_LOCAL_IP = "0.0.0.0";

    public static void main(String[] args) throws Exception {
        Properties properties = loadProperties();

        final ActorSystem system = ActorSystem.create("system");
        final ActorRef storage =
                system.actorOf(Store.props(properties.getProperty("filesDirectory")), "storage");
        final ActorRef crawler =
                system.actorOf(Crawler.props(storage), "crawler");

        final AppServer server = new AppServer(crawler);
        server.startServer(BROADCAST_LOCAL_IP, 9091, system);
    }

    /**
     * Allows to load properties from HDD
     *
     * @return java.lang.Properties with values
     * @throws UnsupportedOperationException when some error happened during the properties load
     */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classloader.getResourceAsStream("app.properties")) {
            properties.load(inputStream);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
        return properties;
    }
}
