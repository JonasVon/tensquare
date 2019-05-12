package com.jonas.tensquare.user.service;

import com.jonas.tensquare.user.dao.UserDao;
import com.jonas.tensquare.user.pojo.User;
import entity.ConstantVariable;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;
import util.JwtUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户模块：用户服务层
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //添加用户
    public void save(User user) {
        user.setId(idWorker.nextId()+"");
        userDao.save(user);
    }

    //查询所有用户
    public List<User> findAll() {
        return userDao.findAll();
    }

    //根据id查询用户
    public User findById(String userId) {
        User user = (User) redisTemplate.opsForValue().get("user_" + userId);
        if(user == null){
            user = userDao.findById(userId).get();
            redisTemplate.opsForValue().set("user_" + userId,user,1, TimeUnit.DAYS);
        }
        return user;
    }

    //修改用户
    public void update(String userId, User user) {
        user.setId(userId);
        userDao.save(user);
        redisTemplate.delete("user_" + userId);
    }

    //删除用户
    public void deleteById(String userId) {
        userDao.deleteById(userId);
        redisTemplate.delete("user_" + userId);
    }

    //发送短信验证码
    public void sendSMS(String mobile) {
        String checkCode = RandomStringUtils.randomNumeric(6);
        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("checkCode",checkCode);
        redisTemplate.opsForValue().set("checkCode_" + mobile,map,5,TimeUnit.MINUTES);
        rabbitTemplate.convertAndSend("sms",map);
        System.out.println("验证码为：" + checkCode);
    }

    //注册用户
    public void register(String code,User user) {
        Map<String,String> codeMap = (Map<String, String>) redisTemplate.opsForValue()
                .get("checkCode_" + user.getMobile());
        String checkCode = codeMap.get("checkCode");
        if(code.equals(checkCode)){
            //后端只验证用户输入的验证码与下发的验证码是否一致，其他非空验证交给前端实现
            user.setId(idWorker.nextId()+"");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRegdate(new Date());
            user.setLastdate(new Date());
            user.setUpdatedate(new Date());
            user.setFanscount(0);
            user.setFollowcount(0);
            userDao.save(user);
            System.out.println("验证通过");
        }else{
            //验证码错误
            throw new RuntimeException(ConstantVariable.CODE_ERROR);
        }
    }

    //用户登录
    public String login(User user) {
        User userLogin = userDao.findByMobile(user.getMobile());
        if(userLogin == null){
            //该用户未注册
            throw new RuntimeException(ConstantVariable.LOGIN_NAME_ERROR);
        }else{
            if(!bCryptPasswordEncoder.matches(user.getPassword(),userLogin.getPassword())){
                //密码错误
                throw new RuntimeException(ConstantVariable.PASSWORD_ERROR);
            }
        }
        //生成令牌
        return jwtUtil.createJWT(userLogin.getId(),userLogin.getMobile(),"user");
    }

    //更新粉丝量和关注数
    public void updateFanAndFollow(String userid,String friendid,int x){
        userDao.updateFansCount(friendid,x);
        userDao.updateFollowCount(userid,x);
    }
}
