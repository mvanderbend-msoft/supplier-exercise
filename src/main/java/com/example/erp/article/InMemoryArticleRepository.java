package com.example.erp.article;

import java.util.*;

public class InMemoryArticleRepository implements ArticleRepository {
    private final Map<String, Article> byCode = new LinkedHashMap<>();
    @Override public void save(Article article) { byCode.put(article.getCode(), article); }
    @Override public Optional<Article> findByCode(String code) { return Optional.ofNullable(byCode.get(code)); }
    @Override public List<Article> findAll() { return new ArrayList<>(byCode.values()); }
}
