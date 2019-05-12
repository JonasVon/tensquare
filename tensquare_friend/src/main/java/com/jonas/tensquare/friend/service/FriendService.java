package com.jonas.tensquare.friend.service;

import com.jonas.tensquare.friend.client.UserClient;
import com.jonas.tensquare.friend.dao.FriendDao;
import com.jonas.tensquare.friend.dao.NoFriendDao;
import com.jonas.tensquare.friend.pojo.Friend;
import com.jonas.tensquare.friend.pojo.NoFriend;
import entity.ConstantVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    @Autowired
    private UserClient userClient;

    //添加或拉黑好友
    public void save(String userid,String friendid, String type) {
        if(type.equals("1")){//添加好友
            //判断好友是否已存在
            Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
            if(friend != null){
                //好友已存在
                throw new RuntimeException(ConstantVariable.FRIEND_IN);
            }
            friend = new Friend();
            friend.setUserid(userid);
            friend.setFriendid(friendid);
            //查询关系
            Friend _friend = friendDao.findByUseridAndFriendid(friendid, userid);
            if(_friend == null){
                //单向喜欢
                friend.setIslike("0");
            }else{
                //互粉关系
                friend.setIslike("1");
                if("0".equals(_friend.getIslike())){
                    //朋友原来就已经关注了你，所以要将朋友原来的islike改为1表示互粉
                    friendDao.updateIsLike(friendid,userid,"1");
                }
            }
            userClient.updateFanAndFollow(userid,friendid,1);
            friendDao.save(friend);
        }else if(type.equals("2")){
            //拉黑好友
            NoFriend noFriend = noFriendDao.findByUseridAndFriendid(userid, friendid);
            if(noFriend != null){
                //已经在黑名单中
                throw new RuntimeException(ConstantVariable.IN_BLACK_LIST);
            }
            //判断是否是粉转黑的情况，若是，则需要修改关注关系
            Friend friend = friendDao.findByUseridAndFriendid(userid,friendid);
            if(friend != null){
                //粉转黑,单向取消两者关系
                if("1".equals(friend.getIslike())){
                    //取消互相关注
                    friendDao.updateIsLike(friendid,userid,"0");
                }
                friendDao.deleteByUseridAndFriendid(userid,friendid);
            }
            noFriend = new NoFriend();
            noFriend.setUserid(userid);
            noFriend.setFriendid(friendid);
            userClient.updateFanAndFollow(userid,friendid,-1);
            noFriendDao.save(noFriend);
        }else{
            throw new RuntimeException(ConstantVariable.PARAM_ERROR);
        }
    }

    //取消关注(不等于拉黑！！)
    public void cancel(String userid, String friendId) {
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendId);
        if(friend == null){
            throw new RuntimeException(ConstantVariable.FRIEND_NOT_IN);
        }
        friendDao.deleteByUseridAndFriendid(userid,friendId);
        userClient.updateFanAndFollow(userid,friendId,-1);
    }
}
