package com.jonas.tensquare.user.service;

import com.jonas.tensquare.user.dao.AdminDao;
import com.jonas.tensquare.user.dao.UserDao;
import com.jonas.tensquare.user.pojo.Admin;
import entity.ConstantVariable;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;
import util.JwtUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户模块：管理员服务层
 */
@Service
@Transactional
public class AdminService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private JwtUtil jwtUtil;

    //添加管理员
    public void save(Admin admin) {
        admin.setId(idWorker.nextId()+"");
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        adminDao.save(admin);
    }

    //查询所有管理员
    public List<Admin> findAll() {
        return adminDao.findAll();
    }

    //根据id查询管理员
    public Admin findById(String adminId) {
        Admin admin = (Admin) redisTemplate.opsForValue().get("admin_" + adminId);
        if(admin == null){
            admin = adminDao.findById(adminId).get();
            redisTemplate.opsForValue().set("admin_" + adminId,admin,1, TimeUnit.DAYS);
        }
        return admin;
    }

    //根据id修改管理员
    public void update(String adminId, Admin admin) {
        admin.setId(adminId);
        adminDao.save(admin);
        redisTemplate.delete("admin_" + adminId);
    }

    //根据id删除用户
    public void deleteById(String userId,String authorization){
        String admin_token = (String) request.getAttribute("chaims_admin");
        if(admin_token == null || "".equals(admin_token)){
            throw new RuntimeException(ConstantVariable.AUTH_FAIL);
        }
        userDao.deleteById(userId);
        redisTemplate.delete("user_" + userId);
    }

    //条件分页查询管理员
    public Page<Admin> query(Admin admin, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return adminDao.findAll(new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(admin.getLoginname() != null && !"".equals(admin.getLoginname())){
                    Predicate predicate = criteriaBuilder.like(root.get("loginname")
                            .as(String.class), "%" + admin.getLoginname() + "%");
                    list.add(predicate);
                }
                if(admin.getState() != null && !"".equals(admin.getState())){
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), admin.getState());
                    list.add(predicate);
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return criteriaBuilder.and(predicates);
            }
        },pageable);
    }

    //管理员登录
    public String login(Admin admin) {
        Admin adminLogin = adminDao.findByLoginname(admin.getLoginname());
        if(adminLogin == null){
            //用户未注册
            throw new RuntimeException(ConstantVariable.LOGIN_NAME_ERROR);
        }else{
            //密码错误
            if(!bCryptPasswordEncoder.matches(admin.getPassword(),adminLogin.getPassword())){
                throw new RuntimeException(ConstantVariable.PASSWORD_ERROR);
            }
            //生成令牌
            return jwtUtil.createJWT(adminLogin.getId(),adminLogin.getLoginname(),"admin");
        }
    }
}
