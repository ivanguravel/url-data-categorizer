package org.example.categorizer.dao;

import org.ahocorasick.trie.PayloadTrie;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains operations for working with Categories data
 *
 */
public class CategoryDao {

    private final Map<String, PayloadTrie> categories = new ConcurrentHashMap<>();

    /**
     * Helps to store category data
     *
     * @param category which will be used for searching
     * @param keywords contains data about category keywords
     *
     * @return set of categories
     *
     */
    public void write(String category, PayloadTrie keywords) {
        categories.putIfAbsent(category, keywords);
    }

    /**
     * Receives category as a param and searches for related keys in storage.
     *
     * @param category
     *
     * @return trie datastructure which is contains category keywords data
     *
     */
    public PayloadTrie read(String category) {
        return Optional.ofNullable(categories.get(category))
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("can't find the value for %s category", category)));
    }

    /**
     * Helps to get all stored categories
     *
     * @return set of categories
     *
     */
    public Set<String> readAll() {
        return categories.keySet();
    }
}
