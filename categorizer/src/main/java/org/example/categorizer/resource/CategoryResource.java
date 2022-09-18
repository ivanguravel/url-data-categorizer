package org.example.categorizer.resource;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import org.example.categorizer.model.dto.CategoryDto;
import org.example.categorizer.service.CategoryService;

import java.util.List;

/**
 * Enables user to communicate with the system via REST interface and work with categorization API
 *
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Searches categories in particular file which was crawled before by related app.
     *
     * @param fileId id of related file for further categorization
     *
     * @return response with categories or empty list
     */
    @GET
    @Path("/{fileId}")
    @Timed
    public Response searchCategories(@PathParam("fileId") String fileId) {
        List<String> categories = categoryService.searchCategoriesByFileId(fileId);
        return Response.ok(categories).build();
    }

    /**
     * Creates the category and stores it in app storage.
     *
     * @param dto describes particular category
     *
     * @return 201 status after post query execution
     */
    @POST
    @Timed
    public Response createCategory(@NotNull @Valid final CategoryDto dto) {
        categoryService.createCategory(dto);
        return Response.status(Response.Status.CREATED).build();
    }
}