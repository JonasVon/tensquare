package com.jonas.tensquare.article.dao;

import com.jonas.tensquare.article.pojo.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 文章模块：频道DAO
 */
public interface ChannelDao extends JpaRepository<Channel,String>, JpaSpecificationExecutor<Channel> {

}
