package com.example.erp.article;

import com.example.erp.common.RuleException;

import java.math.BigDecimal;
import java.util.List;

public class ArticleService {

    private final ArticleRepository articles;

    public ArticleService(ArticleRepository articles) {
        this.articles = articles;
    }

    public void save(Article article) {
        beforeSave(article);
        articles.save(article);
    }

    public List<Article> findAll() { return articles.findAll(); }

    protected void beforeSave(Article a) {
        if (a.getDescription() == null || a.getDescription().isBlank()) {
            throw new RuleException("Article description is required.");
        }
        if (a.getUnitPrice() == null || a.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuleException("Unit price must be greater than zero.");
        }
    }
}
