package com.jonas.tensquare.search.controller;

import com.jonas.tensquare.search.pojo.SearchArticle;
import com.jonas.tensquare.search.service.SearchArticleService;
import entity.ConstantVariable;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/search_article")
public class SearchArticleController {

    @Autowired
    private SearchArticleService searchArticleService;

    //根据文章标题或者描述或者一小段查询
    @GetMapping("/{keyword}/{page}/{size}")
    public Result findByTitleOrContentLike(@PathVariable String keyword,@PathVariable int page,@PathVariable int size){
        Page<SearchArticle> searchArticlePage =
                searchArticleService.findByTitleOrContentLike(keyword, page, size);
        PageResult<SearchArticle> pageResult =
                new PageResult<>(searchArticlePage.getTotalElements(), searchArticlePage.getContent());
        return new Result(true, StatusCode.OK, ConstantVariable.QUERY_SUCCESS,pageResult);
    }
}
