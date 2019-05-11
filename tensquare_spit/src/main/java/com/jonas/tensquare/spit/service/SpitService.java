package com.jonas.tensquare.spit.service;

import com.jonas.tensquare.spit.dao.SpitDao;
import com.jonas.tensquare.spit.pojo.Spit;
import entity.ConstantVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * 吐槽模块：吐槽业务层
 */
@Service
@Transactional
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IdWorker idWorker;

    //点赞
    public String thumbup(String id){
        //模拟用户登录id
        String userId = "1111";
        if(redisTemplate.opsForValue().get("thumbup_" + userId) != null){
            return ConstantVariable.THUMBUP_ERROR;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");
        redisTemplate.opsForValue().set("thumbup_" + userId,1);
        return ConstantVariable.THUMBUP_SUCCESS;
    }

    //查询父节点的吐槽
    public Page<Spit> findByParentid(String parentid,int page,int size){
        Pageable pageable = PageRequest.of(page-1,size);
        return spitDao.findByParentid(parentid,pageable);
    }

    //添加吐槽
    public void save(Spit spit) {
        spit.set_id(idWorker.nextId()+"");
        spit.setPulishtime(new Date());//发布日期
        spit.setVisits(0);//浏览数
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        if(spit.getParentid() != null && !"".equals(spit.getParentid())){
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment",1);
            mongoTemplate.updateFirst(query,update,"spit");
        }
        spitDao.save(spit);
    }

    //查询所有吐槽
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    //根据id查询吐槽
    public Spit findById(String spitId) {
        Spit spit = (Spit) redisTemplate.opsForValue().get("spit_" + spitId);
        if(spit == null){
            spit = spitDao.findById(spitId).get();
            redisTemplate.opsForValue().set("spit_" + spitId,spit);
        }
        return spit;
    }

    //修改吐槽
    public void update(String spitId, Spit spit) {
        spit.set_id(spitId);
        spitDao.save(spit);
        redisTemplate.delete("spit_" + spitId);
    }

    //删除吐槽
    public void deleteById(String spitId) {
        spitDao.deleteById(spitId);
        redisTemplate.delete("spit_" + spitId);
    }
}
