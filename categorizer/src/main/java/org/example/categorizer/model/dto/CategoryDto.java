package org.example.categorizer.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Contains information about category: it's name and words which are used for further category recognition
 *
 */
public class CategoryDto {
    @NotEmpty
    private final String name;
    @NotEmpty
    private final List<String> parts;


    public CategoryDto(@JsonProperty("name") String name, @JsonProperty("parts") List<String> parts) {
        this.name = name;
        this.parts = parts;
    }

    public String getName() {
        return name;
    }

    public List<String> getParts() {
        return parts;
    }
}
