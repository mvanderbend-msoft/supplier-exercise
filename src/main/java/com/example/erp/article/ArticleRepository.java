package com.example.erp.article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    void save(Article article);
    Optional<Article> findByCode(String code);
    List<Article> findAll();
}
