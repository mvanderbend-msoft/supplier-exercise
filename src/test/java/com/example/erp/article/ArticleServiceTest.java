package com.example.erp.article;

import com.example.erp.common.RuleException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ArticleServiceTest {

    @Test void rejectsNonPositivePrice() {
        var repo = new InMemoryArticleRepository();
        var service = new ArticleService(repo);
        var a = new Article("A-1", "Stuff", BigDecimal.ZERO);
        assertThrows(RuleException.class, () -> service.save(a));
    }

    @Test void savesWhenValid() {
        var repo = new InMemoryArticleRepository();
        var service = new ArticleService(repo);
        service.save(new Article("A-1", "Stuff", new BigDecimal("1.50")));
        assertTrue(repo.findByCode("A-1").isPresent());
    }
}
