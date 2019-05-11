package com.jonas.tensquare.article.service;

import com.jonas.tensquare.article.dao.ColumnDao;
import com.jonas.tensquare.article.pojo.Column;
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
 * 文章模块：专栏业务层
 */
@Service
@Transactional
public class ColumnService {

    @Autowired
    private ColumnDao columnDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    //添加专栏
    public void save(Column column) {
        column.setId(idWorker.nextId()+"");
        column.setCreatetime(new Date());
        columnDao.save(column);
    }

    //查询所有专栏
    public List<Column> findAll() {
        return columnDao.findAll();
    }

    //根据id查询专栏
    public Column findById(String columnId) {
        Column column = (Column) redisTemplate.opsForValue().get("column_" + columnId);
        if(column == null){
            column = columnDao.findById(columnId).get();
            redisTemplate.opsForValue().set("column_" + columnId,column);
        }
        return column;
    }

    //根据id修改专栏
    public void update(String columnId, Column column) {
        column.setId(columnId);
        columnDao.save(column);
        redisTemplate.delete("column_" + columnId);
    }

    //根据id删除专栏
    public void deleteById(String columnId) {
        columnDao.deleteById(columnId);
        redisTemplate.delete("column_" + columnId);
    }

    //条件查询
    public List<Column> search(Column column) {
        return columnDao.findAll(new Specification<Column>() {
            @Override
            public Predicate toPredicate(Root<Column> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(column.getName() != null && !"".equals(column.getName())){
                    Predicate predicate = criteriaBuilder.like(root.get("name").
                            as(String.class), "%" + column.getName() + "%");
                    list.add(predicate);
                }
                if(column.getState() != null && !"".equals(column.getState())){
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), column.getState());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        });
    }

    //条件分页查询
    public Page<Column> query(Column column,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return columnDao.findAll(new Specification<Column>() {
            @Override
            public Predicate toPredicate(Root<Column> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(column.getName() != null && !"".equals(column.getName())){
                    Predicate predicate = criteriaBuilder.like(root.get("name").
                            as(String.class), "%" + column.getName() + "%");
                    list.add(predicate);
                }
                if(column.getState() != null && !"".equals(column.getState())){
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), column.getState());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        },pageable);
    }
}
