package com.jonas.tensquare.user.dao;

import com.jonas.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户模块：用户DAO
 */
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

    User findByMobile(String mobile);

    @Modifying
    @Query("update User u set u.fanscount = u.fanscount + ?2 where u.id = ?1")
    void updateFansCount(String id,int x);

    @Modifying
    @Query("update User u set u.followcount = u.followcount + ?2 where u.id = ?1")
    void updateFollowCount(String id,int x);
}
