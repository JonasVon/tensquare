package com.jonas.tensquare.search.service;

import com.jonas.tensquare.search.dao.SearchArticleDao;
import com.jonas.tensquare.search.pojo.SearchArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.IdWorker;

/**
 * 搜索模块：搜索文章业务层
 */
@Service
public class SearchArticleService {

    @Autowired
    private SearchArticleDao searchArticleDao;

    @Autowired
    private IdWorker idWorker;

    public Page<SearchArticle> findByTitleOrContentLike(String keyword,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return searchArticleDao.findByTitleOrContentLike(keyword,keyword,pageable);
    }
}
