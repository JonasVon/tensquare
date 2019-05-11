package com.jonas.tensquare.problem.service;

import com.jonas.tensquare.problem.dao.ReplyDao;
import com.jonas.tensquare.problem.pojo.Reply;
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

/**
 * 问答模块：回答业务层
 */
@Service
@Transactional
public class ReplyService {

    @Autowired
    private ReplyDao replyDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    //添加回答
    public void save(Reply reply) {
        reply.setId(idWorker.nextId()+"");
        System.out.println(reply.getId());
        reply.setCreatetime(new Date());
        replyDao.save(reply);
    }

    //查询所有回答
    public List<Reply> findAll() {
        return replyDao.findAll();
    }

    //根据id查询回答
    public Reply findById(String replyId) {
        Reply reply = (Reply) redisTemplate.opsForValue().get("reply_" + replyId);
        if(reply == null){
            reply = replyDao.findById(replyId).get();
            redisTemplate.opsForValue().set("reply_" + replyId,reply);
        }
        return reply;
    }

    //根据id修改回答
    public void update(String replyId, Reply reply) {
        reply.setId(replyId);
        reply.setUpdatetime(new Date());
        replyDao.save(reply);
    }

    //根据id删除回答
    public void delete(String replyId) {
        replyDao.deleteById(replyId);
    }

    //分页条件查询
    public Page<Reply> search(Reply reply, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return replyDao.findAll(new Specification<Reply>() {
            @Override
            public Predicate toPredicate(Root<Reply> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(reply.getNickname() != null && !"".equals(reply.getNickname())){
                    Predicate predicate = criteriaBuilder.like(root.get("nickname").
                            as(String.class), "%" + reply.getNickname() + "%");
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        },pageable);
    }

    //根据问题id查询回答列表
    public Page<Reply> findReplyByProblemId(String problemId,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return replyDao.findReplyByProblemId(problemId,pageable);
    }
}
