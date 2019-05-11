package com.jonas.tensquare.article.service;

import com.jonas.tensquare.article.dao.ArticleDao;
import com.jonas.tensquare.article.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文章模块：文章业务层
 */
@Service
@Transactional
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    //增加文章
    public void save(Article article) {
        article.setId(idWorker.nextId()+"");
        article.setCreatetime(new Date());
        articleDao.save(article);
    }

    //查询所有文章
    public List<Article> findAll() {
        return articleDao.findAll();
    }

    //根据id查询文章
    public Article findById(String articleId) {
        Article article = (Article) redisTemplate.opsForValue().get("article_" + articleId);
        if(article == null){
            article = articleDao.findById(articleId).get();
            redisTemplate.opsForValue().set("article_" + articleId,article,1, TimeUnit.DAYS);
        }
        return article;
    }

    //根据id修改文章
    public void update(String articleId, Article article) {
        article.setId(articleId);
        article.setUpdatetime(new Date());
        articleDao.save(article);
        redisTemplate.delete("article_" + articleId);
    }

    //根据id删除文章
    public void deleteById(String articleId) {
        articleDao.deleteById(articleId);
        redisTemplate.delete("article_" + articleId);
    }

    //条件查询文章
    public List<Article> search(Article article) {
        return articleDao.findAll(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(article.getTitle() != null && !"".equals(article.getTitle())){
                    Predicate predicate = criteriaBuilder.like(root.get("title").as(String.class), "%" + article.getTitle() + "%");
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        });
    }

    //分页条件查询
    public Page<Article> query(Article article, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return articleDao.findAll(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(article.getTitle() != null && !"".equals(article.getTitle())){
                    Predicate predicate = criteriaBuilder.like(root.get("title").as(String.class), "%" + article.getTitle() + "%");
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        },pageable);
    }

    //审核文章
    public void examine(String articleId){
        articleDao.examine(articleId);
    }

    //查询头条文章
    public List<Article> topList() {
        return articleDao.topList();
    }

    //点赞文章
    public void thumbup(String articleId){
        articleDao.thumbup(articleId);
    }

    //根据频道id获取文章
    public Page<Article> findByChannelid(String channeliId,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return articleDao.findByChannelid(channeliId,pageable);
    }

    //根据专栏id获取文章
    public Page<Article> findByColumnid(String columnId,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return articleDao.findByColumnid(columnId,pageable);
    }
}
