package org.example.categorizer.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.categorizer.model.data.LinkInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Contains operations for working with files data in json format
 *
 */
public class JsonFileDao {

    private static final String PATH_PATTERN = "%s/%s";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String filesDirectory;

    public JsonFileDao(String filesDirectory) {
        this.filesDirectory = filesDirectory;
    }

    /**
     * Reads data from HDD and convert them in related files data model
     *
     * @param fileId id of the file for related search
     *
     * @return information about link and it's crawled text
     *
     * @see LinkInfo
     *
     */
    public LinkInfo read(String fileId) {
        File file = Paths.get(String.format(PATH_PATTERN, filesDirectory, fileId)).toFile();
        try {
            return MAPPER.readValue(file, LinkInfo.class);
        } catch (IOException e) {
            throw new UnsupportedOperationException(e);
        }
    }
}
