package com.jonas.tensquare.recruit.service;

import com.jonas.tensquare.recruit.dao.EnterpriseDao;
import com.jonas.tensquare.recruit.pojo.Enterprise;
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
import java.util.List;


/**
 * 招聘模块：企业信息业务层
 */
@Service
@Transactional
public class EnterpriseService {

    @Autowired
    private EnterpriseDao enterpriseDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    //添加企业
    public void save(Enterprise enterprise){
        enterprise.setId(idWorker.nextId()+"");
        enterpriseDao.save(enterprise);
    }

    //修改企业
    public void update(String enterpriseId,Enterprise enterprise) {
        enterprise.setId(enterpriseId);
        enterpriseDao.save(enterprise);
        redisTemplate.delete("enterprise_" + enterpriseId);
    }

    //根据id查询企业
    public Enterprise findById(String enterpriseId) {
        Enterprise enterprise = (Enterprise) redisTemplate.opsForValue().get("enterprise_" + enterpriseId);
        if(enterprise == null){
            enterprise = enterpriseDao.findById(enterpriseId).get();
            redisTemplate.opsForValue().set("enterprise_" + enterpriseId,enterprise);
        }
        return enterprise;
    }

    //查询所有企业
    public List<Enterprise> findAll(){
        return enterpriseDao.findAll();
    }

    //根据id删除企业
    public void deleteById(String enterpriseId) {
        enterpriseDao.deleteById(enterpriseId);
        redisTemplate.delete("enterprise_" + enterpriseId);
    }

    //条件查询企业
    public List<Enterprise> findSearch(Enterprise enterprise) {
        return enterpriseDao.findAll(new Specification<Enterprise>() {
            @Override
            public Predicate toPredicate(Root<Enterprise> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(enterprise.getName() != null && !"".equals(enterprise.getName())){
                    Predicate predicate = criteriaBuilder.like(root.get("name").
                            as(String.class), "%" + enterprise.getName() + "%");
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        });
    }

    //查询热门企业
    public List<Enterprise> findHotList() {
        return enterpriseDao.findByIshot("1");
    }

    //条件分页查询企业
    public Page<Enterprise> findQuery(Enterprise enterprise, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return enterpriseDao.findAll(new Specification<Enterprise>() {
            @Override
            public Predicate toPredicate(Root<Enterprise> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(enterprise.getName() != null && !"".equals(enterprise.getName())){
                    Predicate predicate = criteriaBuilder.like(root.get("name").
                            as(String.class), "%" + enterprise.getName() + "%");
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        },pageable);
    }
}
