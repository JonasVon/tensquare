package com.jonas.tensquare.user.controller;

import com.jonas.tensquare.user.pojo.User;
import com.jonas.tensquare.user.service.UserService;
import entity.ConstantVariable;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户模块：用户控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //更新关注数和粉丝量
    @PostMapping("/{userid}/{friendid}/{x}")
    public void updateFanAndFollow(@PathVariable String userid,@PathVariable String friendid,@PathVariable int x){
        userService.updateFanAndFollow(userid, friendid, x);
    }

    //用户登录
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        String token;
        try{
            token = userService.login(user);
        }catch (RuntimeException e){
            return new Result(false,StatusCode.ERROR,ConstantVariable.LOGIN_FAIL,e.getMessage());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("name",user.getMobile());
        map.put("token",token);
        return new Result(true,StatusCode.OK,ConstantVariable.LOGIN_SUCCESS,map);
    }

    //注册用户
    @PostMapping("/register/{code}")
    public Result register(@PathVariable String code,@RequestBody User user){
        try{
            userService.register(code,user);
        }catch (RuntimeException e){
            return new Result(false,StatusCode.ERROR,ConstantVariable.CODE_ERROR,e.getMessage());
        }
        return new Result(true,StatusCode.OK,ConstantVariable.REGISTER_SUCCESS);
    }

    //发送短信验证码
    @PostMapping("/sendsms/{mobile}")
    public Result sendSMS(@PathVariable String mobile){
        userService.sendSMS(mobile);
        return new Result(true,StatusCode.OK,ConstantVariable.SEND_SUCCESS);
    }

    //删除用户
    @DeleteMapping("/{userId}")
    public Result deleteById(@PathVariable String userId){
        userService.deleteById(userId);
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }

    //修改用户
    @PutMapping("/{userId}")
    public Result update(@PathVariable String userId,@RequestBody User user){
        userService.update(userId,user);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //根据id查询用户
    @GetMapping("/{userId}")
    public Result findById(@PathVariable String userId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,userService.findById(userId));
    }

    //查询所有用户
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,userService.findAll());
    }

    //添加用户
    @PostMapping
    public Result save(@RequestBody User user){
        userService.save(user);
        return new Result(true, StatusCode.OK, ConstantVariable.SAVE_SUCCESS);
    }
}
