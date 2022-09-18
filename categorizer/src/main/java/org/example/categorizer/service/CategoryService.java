package org.example.categorizer.service;

import org.ahocorasick.trie.PayloadTrie;
import org.example.categorizer.dao.CategoryDao;
import org.example.categorizer.dao.JsonFileDao;
import org.example.categorizer.model.data.LinkInfo;
import org.example.categorizer.model.dto.CategoryDto;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Wraps main operations under categories
 *
 */
public class CategoryService {

    private final CategoryDao categoryDao;
    private final JsonFileDao fileDao;

    public CategoryService(CategoryDao categoryDao, JsonFileDao fileDao) {
        this.categoryDao = categoryDao;
        this.fileDao = fileDao;
    }

    /**
     * Searches categories by file id in the file which is created using cralwer app
     *
     * @param fileId id for the file with crawled text from html link
     *
     * @return list of found categories or empty list when there are no category found
     *
     */
    public List<String> searchCategoriesByFileId(String fileId) {
        LinkInfo info = Objects.requireNonNull(fileDao.read(fileId));
        String text = info.getText();
        List<String> result = new LinkedList<>();
        Set<String> categories = categoryDao.readAll();
        for (String category : categories) {
            PayloadTrie<String> trie = categoryDao.read(category);
            if (!trie.parseText(text).isEmpty()) {
                result.add(category);
            }
        }
        return result;
    }

    /**
     * Creates category using Trie datastructure and Aho-Corasick algorithm under the hood
     *
     * @param dto contains information about particular category
     * @see CategoryDto
     *
     */
    public void createCategory(CategoryDto dto) {
        String categoryName = dto.getName();
        PayloadTrie<String> trie = buildTrieFromWords(dto.getParts());
        categoryDao.write(categoryName, trie);
    }

    private PayloadTrie<String> buildTrieFromWords(List<String> words) {
        PayloadTrie.PayloadTrieBuilder<String> builder =
                PayloadTrie.<String>builder()
                        .onlyWholeWords()
                        .ignoreCase();

        for (String word : words) {
            builder.addKeyword(word);
        }

        return builder.build();
    }
}
