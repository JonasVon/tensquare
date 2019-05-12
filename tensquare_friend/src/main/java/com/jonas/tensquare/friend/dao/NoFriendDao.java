package com.jonas.tensquare.friend.dao;

import com.jonas.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoFriendDao extends JpaRepository<NoFriend,String> {

    NoFriend findByUseridAndFriendid(String userid,String friendid);
}
