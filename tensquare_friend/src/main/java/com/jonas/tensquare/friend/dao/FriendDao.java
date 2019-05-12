package com.jonas.tensquare.friend.dao;

import com.jonas.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend,String> {

    Friend findByUseridAndFriendid(String useid,String friendid);

    @Modifying
    @Query("update Friend set islike = ?3 where userid = ?1 and friendid = ?2")
    void updateIsLike(String userid,String friendid,String islike);

    void deleteByUseridAndFriendid(String userid,String friendid);
}
