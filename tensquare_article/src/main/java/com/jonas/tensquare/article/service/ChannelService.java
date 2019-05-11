package com.jonas.tensquare.article.service;

import com.jonas.tensquare.article.dao.ChannelDao;
import com.jonas.tensquare.article.pojo.Channel;
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
 * 文章模块：频道业务层
 */
@Service
@Transactional
public class ChannelService {

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    //添加频道
    public void save(Channel channel) {
        channel.setId(idWorker.nextId()+"");
        channelDao.save(channel);
    }

    //查询所有频道
    public List<Channel> findAll() {
        return channelDao.findAll();
    }

    //根据id查询频道
    public Channel findById(String channelId) {
        Channel channel = (Channel) redisTemplate.opsForValue().get("channel_" + channelId);
        if(channel == null){
            channel = channelDao.findById(channelId).get();
            redisTemplate.opsForValue().set("channel_" + channelId,channel,1, TimeUnit.DAYS);
        }
        return channel;
    }

    //根据id修改频道
    public void update(String channelId, Channel channel) {
        channel.setId(channelId);
        channelDao.save(channel);
        redisTemplate.delete("channel_" + channelId);
    }

    //根据id删除频道
    public void deleteById(String channelId) {
        channelDao.deleteById(channelId);
        redisTemplate.delete("channel_" + channelId);
    }

    //条件查询
    public List<Channel> search(Channel channel) {
        return channelDao.findAll(new Specification<Channel>() {
            @Override
            public Predicate toPredicate(Root<Channel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(channel.getName() != null && !"".equals(channel.getName())){
                    Predicate predicate = criteriaBuilder.like(root.get("name").
                            as(String.class), "%" + channel.getName() + "%");
                    list.add(predicate);
                }
                if(channel.getState() != null && !"".equals(channel.getState())){
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), channel.getState());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        });
    }

    //条件分页查询
    public Page<Channel> query(Channel channel, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return channelDao.findAll(new Specification<Channel>() {
            @Override
            public Predicate toPredicate(Root<Channel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(channel.getName() != null && !"".equals(channel.getName())){
                    Predicate predicate = criteriaBuilder.like(root.get("name").
                            as(String.class), "%" + channel.getName() + "%");
                    list.add(predicate);
                }
                if(channel.getState() != null && !"".equals(channel.getState())){
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), channel.getState());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        },pageable);
    }
}
