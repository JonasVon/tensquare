package com.jonas.tensquare.service;

import com.jonas.tensquare.dao.LabelDao;
import com.jonas.tensquare.pojo.Label;
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
import java.util.concurrent.TimeUnit;

/**
 * 基础模块：标签服务层
 */
@Service
@Transactional
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    //查询所有标签
    public List<Label> findAll(){
        return labelDao.findAll();
    }

    //根据id查询标签
    public Label findById(String id){
        Label label = (Label) redisTemplate.opsForValue().get("label_" + id);
        if(label == null){
            label = labelDao.findById(id).get();
            redisTemplate.opsForValue().set("label_" + id,label,1, TimeUnit.DAYS);
        }
        return label;
    }

    //添加标签
    public void save(Label label){
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    //修改标签
    public void update(String labelId,Label label){
        label.setId(labelId);
        labelDao.save(label);
        redisTemplate.delete("label_" + labelId);
    }

    //根据id删除标签
    public void deleteById(String id){
        labelDao.deleteById(id);
        redisTemplate.delete("label_" + id);
    }

    //条件查询标签
    public List<Label> findSearch(Label label) {
        return labelDao.findAll(new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(label.getLabelname() != null && !"".equals(label.getLabelname())){
                    Predicate predicate = criteriaBuilder.like(root.get("labelname").
                            as(String.class), "%" + label.getLabelname() + "%");
                    list.add(predicate);
                }
                if(label.getState() != null && !"".equals(label.getState())){
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), label.getState());
                    list.add(predicate);
                }
                if(label.getRecommend() != null && !"".equals(label.getRecommend())){
                    Predicate predicate = criteriaBuilder.equal(root.get("recommend")
                            .as(String.class), label.getRecommend());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        });
    }

    //条件分页查询标签
    public Page<Label> pageQuery(Label label, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return labelDao.findAll(new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(label.getLabelname() != null && !"".equals(label.getLabelname())){
                    Predicate predicate = criteriaBuilder.like(root.get("labelname").
                            as(String.class), "%" + label.getLabelname() + "%");
                    list.add(predicate);
                }
                if(label.getState() != null && !"".equals(label.getState())){
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), label.getState());
                    list.add(predicate);
                }
                if(label.getRecommend() != null && !"".equals(label.getRecommend())){
                    Predicate predicate = criteriaBuilder.equal(root.get("recommend")
                            .as(String.class), label.getRecommend());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        },pageable);
    }
}
