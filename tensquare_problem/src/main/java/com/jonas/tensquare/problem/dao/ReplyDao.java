package com.jonas.tensquare.problem.dao;

import com.jonas.tensquare.problem.pojo.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 问答模块：回答DAO
 */
public interface ReplyDao extends JpaRepository<Reply,String>, JpaSpecificationExecutor<Reply> {
    //根据问题id查询回答列表
    @Query(value="select r from Reply r where r.problemid = ?1")
    Page<Reply> findReplyByProblemId(String problemId, Pageable pageable);
}
