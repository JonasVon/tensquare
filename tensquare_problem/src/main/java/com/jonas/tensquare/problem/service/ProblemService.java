package com.jonas.tensquare.problem.service;

import com.jonas.tensquare.problem.dao.ProblemDao;
import com.jonas.tensquare.problem.pojo.Problem;
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

@Service
@Transactional
public class ProblemService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProblemDao problemDao;

    //添加问题
    public void save(Problem problem) {
        problem.setId(idWorker.nextId()+"");
        problem.setCreatetime(new Date());
        problemDao.save(problem);
    }

    //查询所有问题
    public List<Problem> findAll() {
        return problemDao.findAll();
    }

    //根据id查询问题
    public Problem findById(String problemId) {
        Problem problem = (Problem) redisTemplate.opsForValue().get("problem_" + problemId);
        if(problem == null){
            problem = problemDao.findById(problemId).get();
            redisTemplate.opsForValue().set("problem_" + problemId,problem);
        }
        return problem;
    }

    //根据id修改问题
    public void update(String problemId, Problem problem) {
        problem.setId(problemId);
        problem.setUpdatetime(new Date());
        problemDao.save(problem);
        redisTemplate.delete("problem_" + problemId);
    }

    //根据id删除问题
    public void deleteById(String problemId) {
        problemDao.deleteById(problemId);
        redisTemplate.delete("problem_" + problemId);
    }

    //条件查询问题
    public List<Problem> search(Problem problem) {
        return problemDao.findAll(new Specification<Problem>() {
            @Override
            public Predicate toPredicate(Root<Problem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(problem.getTitle() != null && !"".equals(problem.getTitle())){
                    Predicate predicate = criteriaBuilder.like(root.get("title").
                            as(String.class), "%" + problem.getTitle() + "%");
                    list.add(predicate);
                }
                if(problem.getNickname() != null && !"".equals(problem.getNickname())){
                    Predicate predicate = criteriaBuilder.like(root.get("nickname").
                            as(String.class), "%" + problem.getNickname() + "%");
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        });
    }

    //条件分页查询问题
    public Page<Problem> query(Problem problem, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.findAll(new Specification<Problem>() {
            @Override
            public Predicate toPredicate(Root<Problem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(problem.getTitle() != null && !"".equals(problem.getTitle())){
                    Predicate predicate = criteriaBuilder.like(root.get("title").
                            as(String.class), "%" + problem.getTitle() + "%");
                    list.add(predicate);
                }
                if(problem.getNickname() != null && !"".equals(problem.getNickname())){
                    Predicate predicate = criteriaBuilder.like(root.get("nickname").
                            as(String.class), "%" + problem.getNickname() + "%");
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        },pageable);
    }

    //根据标签id分页查询最新问题列表(最新问题：回复时间replytime倒序)
    public Page<Problem> findByLabelIdOrderByReplytime(String labelId,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.findByLabelIdOrderByReplytime(labelId,pageable);
    }

    //根据标签id分页查询热门问题列表(热门问题：回复数量reply倒序)
    public Page<Problem> findProblemOrderByReply(String labelId,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.findProblemOrderByReply(labelId,pageable);
    }

    //根据标签id分页查询等待回答列表(等待回答问题：回复数量reply为0)
    public Page<Problem> findProblemByReplyEqualsOrderByCreatetime(String labelId,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.findProblemByReplyEqualsOrderByCreatetime(labelId,pageable);
    }

    //根据标签id分页查询所有问题
    public Page<Problem> findProblemBylabelId(String labelId,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return problemDao.findProblemBylabelId(labelId,pageable);
    }
}
