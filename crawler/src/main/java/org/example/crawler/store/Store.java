package org.example.crawler.store;

import akka.actor.AbstractActor;
import akka.actor.Props;
import org.example.crawler.model.LinkInfo;
import org.example.crawler.util.JsonUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Uses for storing information about link and it's text to the file on the local fs
 *
 */
import static java.util.Objects.requireNonNull;

public class Store extends AbstractActor {

    private final String filesDirectory;

    static public Props props(String filesDirectory) {
        return Props.create(Store.class, () -> new Store(filesDirectory));
    }

    public Store(String filesDirectory) {
        this.filesDirectory = filesDirectory;
        safeCreateDirectoryIfNotExists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(LinkInfo.class, this::safeStoreTextToFile)
                .build();
    }

    /**
     * Converts information about link and text from it to json and stores information on local fs
     *
     * @param info link with parsed text model
     * @see LinkInfo
     */
    private void safeStoreTextToFile(LinkInfo info) {
        requireNonNull(info);
        String json = JsonUtils.safeConvertObjectToJson(info);
        safeWriteDataToFile(info.getId(), json);
    }

    /**
     * Helps to create parent directory for files
     */
    private void safeCreateDirectoryIfNotExists() {
        try {
            Path path = Paths.get(filesDirectory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new UnsupportedOperationException("can't create main directory", e);
        }
    }

    /**
     * Writes information about link to the local fs in streaming mode
     *
     * @param fileId id which will be used for related file
     * @param data json with link and parsed text
     */
    private void safeWriteDataToFile(String fileId, String data) {
        try (OutputStream output = new FileOutputStream(String.format("%s/%s", filesDirectory, fileId))) {
            output.write(data.getBytes());
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
