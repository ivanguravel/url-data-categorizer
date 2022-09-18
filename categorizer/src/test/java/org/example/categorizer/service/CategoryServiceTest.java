package org.example.categorizer.service;

import org.ahocorasick.trie.PayloadTrie;
import org.example.categorizer.dao.CategoryDao;
import org.example.categorizer.dao.JsonFileDao;
import org.example.categorizer.model.data.LinkInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    private static final String STAR_WARS_CATEGORY = "Star Wars";

    private static final String STAR_WARS_TEXT = "Set about five years after the fall " +
            "of the Empire, before the rise of the First" +
            "Order, The Mandalorian is an exploration of a new era in Star Wars storytelling onscreen.";

    @Mock
    private CategoryDao categoryDao;
    @Mock
    private JsonFileDao fileDao;

    @InjectMocks
    private CategoryService service;

    @Test
    public void testSearchCategoriesByFileId() {
        when(fileDao.read("id")).thenReturn(new LinkInfo("http://starwars.com",
                UUID.randomUUID().toString(), STAR_WARS_TEXT));
        when(categoryDao.readAll()).thenReturn(new HashSet<>(Collections.singletonList(STAR_WARS_CATEGORY)));
        when(categoryDao.read(STAR_WARS_CATEGORY)).thenReturn(createTrie());

        assertEquals(Arrays.asList(STAR_WARS_CATEGORY), service.searchCategoriesByFileId("id"));
    }

    private static PayloadTrie<String> createTrie() {
        PayloadTrie.PayloadTrieBuilder<String> builder =
                PayloadTrie.<String>builder()
                        .onlyWholeWords()
                        .ignoreCase();

        builder.addKeyword("star war");
        builder.addKeyword("star wars");
        builder.addKeyword("starwar");
        builder.addKeyword("starwars");
        builder.addKeyword("r2d2");

        return builder.build();
    }
}
