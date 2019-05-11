package com.jonas.tensquare.problem.dao;

import com.jonas.tensquare.problem.pojo.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProblemDao extends JpaRepository<Problem,String>, JpaSpecificationExecutor<Problem> {

    @Query(value="select p from Problem p join Pl pl on p.id = pl.problemid where pl.labelid = ?1 order by p.replytime desc")
    Page<Problem> findByLabelIdOrderByReplytime(String labelId, Pageable pageable);

    @Query(value="select p from Problem p join Pl pl on p.id = pl.problemid where pl.labelid = ?1 order by p.reply desc")
    Page<Problem> findProblemOrderByReply(String labelId,Pageable pageable);

    @Query(value="select p from Problem p join Pl pl on p.id = pl.problemid where pl.labelid = ?1 and p.reply = 0 order by p.createtime desc")
    Page<Problem> findProblemByReplyEqualsOrderByCreatetime(String labelId,Pageable pageable);

    @Query(value="select p from Problem p join Pl pl on p.id = pl.problemid where pl.labelid = ?1 order by p.createtime desc ")
    Page<Problem> findProblemBylabelId(String labelId,Pageable pageable);
}
