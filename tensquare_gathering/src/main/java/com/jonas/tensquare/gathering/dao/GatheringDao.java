package com.jonas.tensquare.gathering.dao;


import com.jonas.tensquare.gathering.pojo.Gathering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 活动模块：活动DAO
 */
public interface GatheringDao extends JpaRepository<Gathering,String>, JpaSpecificationExecutor<Gathering> {
    //根据城市查询活动
    @Query("select ga from Gathering ga where city = ?1")
    Page<Gathering> findByCity(String city, Pageable pageable);
}
