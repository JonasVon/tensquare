package com.jonas.tensquare.article.dao;

import com.jonas.tensquare.article.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 文章模块：文章DAO
 */
public interface ArticleDao extends JpaRepository<Article,String>, JpaSpecificationExecutor<Article> {

    //审核文章
    @Modifying
    @Query("update Article ar set ar.state = '1' where ar.id = ?1 ")
    void examine(String id);

    //头条文章
    @Query("select ar from Article ar where ar.istop = '1' order by ar.createtime desc")
    List<Article> topList();

    //点赞文章
    @Modifying
    @Query("update Article ar set ar.thumbup = ar.thumbup + 1 where ar.id = ?1")
    void thumbup(String articleId);

    //根据频道id获取文章
    @Query("select ar from Article ar where ar.channelid = ?1 order by ar.createtime desc")
    Page<Article> findByChannelid(String channeliId,Pageable pageable);

    //根据专栏id获取文章
    @Query("select ar from Article ar where ar.columnid = ?1 order by ar.createtime desc")
    Page<Article> findByColumnid(String columnId, Pageable pageable);
}
