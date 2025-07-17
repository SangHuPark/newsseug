package com.a301.newsseug.external.caffeine.manager;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.repository.ArticleRepository;
import com.a301.newsseug.external.caffeine.type.CacheType;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class ArticleCacheManager extends BaseCacheManager<Article, Long> {

    private final ArticleRepository articleRepository;

    public ArticleCacheManager(CacheManager cacheManager, ArticleRepository articleRepository) {
        super(cacheManager);
        this.articleRepository = articleRepository;
    }

    @Override
    protected String getCacheName() {
        return CacheType.ARTICLE.getName();
    }

    @Override
    protected Article loadFromRepository(Long id) {
        return articleRepository.getOrThrow(id);
    }
}
