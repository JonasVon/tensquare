package com.jonas.tensquare.search.dao;

import com.jonas.tensquare.search.pojo.SearchArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 搜索模块：搜索文章DAO
 */

public interface SearchArticleDao extends ElasticsearchRepository<SearchArticle,String> {

    //搜索文章(根据标题或者文章描述或者文章内容的一段)
    Page<SearchArticle> findByTitleOrContentLike(String title,String content,Pageable pageable);
}
