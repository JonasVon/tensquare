package com.jonas.tensquare.article.controller;

import com.jonas.tensquare.article.pojo.Article;
import com.jonas.tensquare.article.service.ArticleService;
import entity.ConstantVariable;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


/**
 * 文章模块：文章控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    //根据专栏id获取文章
    @PostMapping("/column/{columnId}/{page}/{size}")
    public Result findByColumnid(@PathVariable("columnId") String columnId,
                                 @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Article> articleList = articleService.findByColumnid(columnId, page, size);
        PageResult<Article> pageResult = new PageResult<>(articleList.getTotalElements(), articleList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //根据频道id获取文章
    @PostMapping("/channel/{channelId}/{page}/{size}")
    public Result findByChannelid(@PathVariable("channelId") String channelId,
                                  @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Article> articleList = articleService.findByChannelid(channelId, page, size);
        PageResult<Article> pageResult = new PageResult<>(articleList.getTotalElements(), articleList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);

    }

    //点赞文章
    @PutMapping("/thumbup/{articleId}")
    public Result thumbup(@PathVariable("articleId") String articleId){
        articleService.thumbup(articleId);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //头条文章
    @GetMapping("/top")
    public Result topList(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,articleService.topList());
    }

    //审核文章
    @PutMapping("/examine/{articleId}")
    public Result examine(@PathVariable("articleId") String articleId){
        articleService.examine(articleId);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //分页条件查询文章
    @PostMapping("/search/{page}/{size}")
    public Result query(@RequestBody Article article,
                        @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Article> articleList = articleService.query(article, page, size);
        PageResult<Article> pageResult = new PageResult<>(articleList.getTotalElements(), articleList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //条件查询文章
    @PostMapping("/search")
    public Result search(@RequestBody Article article){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,articleService.search(article));
    }

    //根据id删除文章
    public Result deleteById(@PathVariable("articleId") String articleId){
        articleService.deleteById(articleId);
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }

    //根据id修改文章
    @PutMapping("/{articleId}")
    public Result update(@RequestBody Article article,@PathVariable("articleId") String articleId){
        articleService.update(articleId,article);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //根据id查询文章
    @GetMapping("/{articleId}")
    public Result findById(@PathVariable("articleId") String articleId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,articleService.findById(articleId));
    }

    //查询所有文章
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,articleService.findAll());
    }

    //增加文章
    @PostMapping
    public Result save(@RequestBody Article article){
        articleService.save(article);
        return new Result(true, StatusCode.OK, ConstantVariable.SAVE_SUCCESS);
    }
}
