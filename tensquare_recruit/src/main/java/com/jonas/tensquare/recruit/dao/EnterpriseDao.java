package com.jonas.tensquare.recruit.dao;

import com.jonas.tensquare.recruit.pojo.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 招聘模块：企业信息DAO层
 */
public interface EnterpriseDao extends JpaRepository<Enterprise,String>, JpaSpecificationExecutor<Enterprise> {
    List<Enterprise> findByIshot(String ishot);
}
