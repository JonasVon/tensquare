package com.jonas.tensquare.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

/**
 * 搜索模块：索引库文章实体类
 */
@Document(indexName = "tensquare_article",type = "article")
public class SearchArticle implements Serializable {

    @Id
    private String id;

    @Field(analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String title;//文章标题

    @Field(analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String content;//文章内容/描述

    private String state;//审核状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
