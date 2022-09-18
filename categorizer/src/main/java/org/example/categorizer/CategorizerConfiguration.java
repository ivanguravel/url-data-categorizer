package org.example.categorizer;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Contains configuration for the categorizer app
 */
public class CategorizerConfiguration extends Configuration {

    @NotNull
    @NotEmpty
    private final String filesDirectory;

    public CategorizerConfiguration(@JsonProperty("filesDirectory") String filesDirectory) {
        this.filesDirectory = filesDirectory;
    }

    public String getFilesDirectory() {
        return filesDirectory;
    }
}