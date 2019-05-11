package com.jonas.tensquare.user.dao;

import com.jonas.tensquare.user.pojo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用户模块：管理员DAO
 */
public interface AdminDao extends JpaRepository<Admin,String>, JpaSpecificationExecutor<Admin> {

    Admin findByLoginname(String loginname);
}
