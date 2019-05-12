package com.jonas.tensquare.friend.controller;

import com.jonas.tensquare.friend.service.FriendService;
import entity.ConstantVariable;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 交友模块：交友控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;

    //取消关注(删除好友,不等于拉黑！！)
    @DeleteMapping("/{friendId}")
    public Result cancel(@PathVariable("friendId") String friendId){
        //权限校验
        Claims chaims_user = (Claims) request.getAttribute("chaims_user");
        if(chaims_user == null){
            return new Result(false, StatusCode.ERROR, ConstantVariable.AUTH_FAIL);
        }
        String userid = chaims_user.getId();
        try{
            friendService.cancel(userid,friendId);
        }catch (RuntimeException e){
            return new Result(false,StatusCode.ERROR,ConstantVariable.OPERATE_FAIL,e.getMessage());
        }
        return new Result(true,StatusCode.OK,ConstantVariable.OPERATE_SUCCESS);
    }

    //添加好友或者拉黑
    @PutMapping("/like/{friendid}/{type}")//type=1表示添加好友，2表示拉黑
    public Result save(@PathVariable String friendid,@PathVariable String type){
        //权限校验
        Claims chaims_user = (Claims) request.getAttribute("chaims_user");
        if(chaims_user == null){
            return new Result(false, StatusCode.ERROR, ConstantVariable.AUTH_FAIL);
        }
        String userid = chaims_user.getId();
        try {
            friendService.save(userid,friendid,type);
        }catch (RuntimeException e){
            return new Result(false,StatusCode.ERROR,ConstantVariable.SAVE_ERROR,e.getMessage());
        }
        return new Result(true,StatusCode.OK,ConstantVariable.OPERATE_SUCCESS);
    }
}
