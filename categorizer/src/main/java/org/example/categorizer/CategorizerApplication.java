package org.example.categorizer;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.example.categorizer.dao.CategoryDao;
import org.example.categorizer.dao.JsonFileDao;
import org.example.categorizer.resource.CategoryResource;
import org.example.categorizer.service.CategoryService;

/**
 * Main entrypoint to the categorizer app
 */
public class CategorizerApplication extends Application<CategorizerConfiguration> {

    public static void main(String[] args) throws Exception {
        new CategorizerApplication().run(args);
    }

    @Override
    public void run(CategorizerConfiguration configuration, Environment environment) {
        CategoryDao categoryDao = new CategoryDao();
        JsonFileDao fileDao = new JsonFileDao(configuration.getFilesDirectory());

        CategoryService service = new CategoryService(categoryDao, fileDao);

        environment.jersey().register(new CategoryResource(service));
      }
}