package com.jonas.tensquare.spit.dao;

import com.jonas.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 吐槽模块：吐槽DAO
 */
public interface SpitDao extends MongoRepository<Spit,String> {

    //查询父节点吐槽
    Page<Spit> findByParentid(String parentid, Pageable pageable);
}
