package com.jonas.tensquare.recruit.service;

import com.jonas.tensquare.recruit.dao.RecruitDao;
import com.jonas.tensquare.recruit.pojo.Recruit;
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
 * 招聘模块：招聘信息业务层
 */
@Service
@Transactional
public class RecruitService {

    @Autowired
    private RecruitDao recruitDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    //添加招聘信息
    public void save(Recruit recruit) {
        recruit.setId(idWorker.nextId()+"");
        recruit.setCreatetime(new Date());
        recruitDao.save(recruit);
    }

    //查询所有招聘信息
    public List<Recruit> findAll() {
        return recruitDao.findAll();
    }

    //根据id查询招聘信息
    public Recruit findById(String recruitId) {
        Recruit recruit = (Recruit) redisTemplate.opsForValue().get("recruit_" + recruitId);
        if(recruit == null){
            recruit = recruitDao.findById(recruitId).get();
            redisTemplate.opsForValue().set("recruit_" + recruitId,recruit);
        }
        return recruit;
    }

    //修改招聘信息
    public void update(String recruitId,Recruit recruit) {
        recruitDao.save(recruit);
        redisTemplate.delete("recruit_" + recruitId);
    }

    //根据id删除招聘信息
    public void deleteById(String recruitId) {
        recruitDao.deleteById(recruitId);
        redisTemplate.delete("recruit_" + recruitId);
    }

    //条件查询招聘信息
    public List<Recruit> search(Recruit recruit) {
        return recruitDao.findAll(new Specification<Recruit>() {
            @Override
            public Predicate toPredicate(Root<Recruit> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(recruit.getJobname() != null && !"".equals(recruit.getJobname())){
                    Predicate predicate = criteriaBuilder.like(root.get("jobname").
                            as(String.class), "%" + recruit.getJobname() + "%");
                    list.add(predicate);
                }
                if(recruit.getAddress() != null && !"".equals(recruit.getAddress())){
                    Predicate predicate = criteriaBuilder.like(root.get("address").
                            as(String.class), "%" + recruit.getAddress() + "%");
                    list.add(predicate);
                }
                if(recruit.getState() != null && !"".equals(recruit.getState())){
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), recruit.getState());
                    list.add(predicate);
                }
                if(recruit.getType() != null && !"".equals(recruit.getType())){
                    Predicate predicate = criteriaBuilder.equal(root.get("type").as(String.class), recruit.getType());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        });
    }

    //条件分页查询招聘信息
    public Page<Recruit> query(Recruit recruit, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return recruitDao.findAll(new Specification<Recruit>() {
            @Override
            public Predicate toPredicate(Root<Recruit> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(recruit.getJobname() != null && !"".equals(recruit.getJobname())){
                    Predicate predicate = criteriaBuilder.like(root.get("jobname").
                            as(String.class), "%" + recruit.getJobname() + "%");
                    list.add(predicate);
                }
                if(recruit.getAddress() != null && !"".equals(recruit.getAddress())){
                    Predicate predicate = criteriaBuilder.like(root.get("address").
                            as(String.class), "%" + recruit.getAddress() + "%");
                    list.add(predicate);
                }
                if(recruit.getState() != null && !"".equals(recruit.getState())){
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), recruit.getState());
                    list.add(predicate);
                }
                if(recruit.getType() != null && !"".equals(recruit.getType())){
                    Predicate predicate = criteriaBuilder.equal(root.get("type").as(String.class), recruit.getType());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        },pageable);
    }

    //查询热门招聘：state为2,只返回前4条记录并且按照创建时间倒序排列
    public List<Recruit> findTop4ByStateOrderByCreatetimeDesc(){
        return recruitDao.findTop4ByStateOrderByCreatetimeDesc("2");
    }

    //查询最新招聘信息：state不为0,只返回前12条记录并且按照创建时间倒序排列
    public List<Recruit> findTop12ByStateNotOrderByCreatetimeDesc(){
        return recruitDao.findTop12ByStateNotOrderByCreatetimeDesc("0");
    }

}
