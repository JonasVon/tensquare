package com.jonas.tensquare.gathering.service;

import com.jonas.tensquare.gathering.dao.GatheringDao;
import com.jonas.tensquare.gathering.pojo.Gathering;
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
 * 活动模块：活动业务层
 */
@Service
@Transactional
public class GatheringService {

    @Autowired
    private GatheringDao gatheringDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    //添加活动
    public void save(Gathering gathering) {
        gathering.setId(idWorker.nextId()+"");
        gatheringDao.save(gathering);
    }

    //删除活动
    public void deleteById(String gatheringId) {
        gatheringDao.deleteById(gatheringId);
        redisTemplate.delete("gathering_" + gatheringId);
    }

    //修改活动
    public void update(String gatheringId, Gathering gathering) {
        gathering.setId(gatheringId);
        gatheringDao.save(gathering);
        redisTemplate.delete("gathering_" + gatheringId);
    }

    //根据id查询活动
    public Gathering findById(String gatheringId) {
        Gathering gathering = (Gathering) redisTemplate.opsForValue().get("gathering_" + gatheringId);
        if(gathering == null){
            gathering = gatheringDao.findById(gatheringId).get();
            redisTemplate.opsForValue().set("gathering_" + gatheringId,gathering);
        }
        return gathering;
    }

    //查询所有活动
    public List<Gathering> findAll() {
        return gatheringDao.findAll();
    }

    //条件查询
    public List<Gathering> search(Gathering gathering) {
        return gatheringDao.findAll(new Specification<Gathering>() {
            @Override
            public Predicate toPredicate(Root<Gathering> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(gathering.getName() != null && !"".equals(gathering.getName())){
                    Predicate predicate = criteriaBuilder.like(root.get("name").
                            as(String.class), "%" + gathering.getName() + "%");
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        });
    }

    //条件分页查询
    public Page<Gathering> query(Gathering gathering, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return gatheringDao.findAll(new Specification<Gathering>() {
            @Override
            public Predicate toPredicate(Root<Gathering> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(gathering.getName() != null && !"".equals(gathering.getName())){
                    Predicate predicate = criteriaBuilder.like(root.get("name").
                            as(String.class), "%" + gathering.getName() + "%");
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        },pageable);
    }

    //根据城市查询活动
    public Page<Gathering> findByCity(String city, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return gatheringDao.findByCity(city,pageable);
    }
}
