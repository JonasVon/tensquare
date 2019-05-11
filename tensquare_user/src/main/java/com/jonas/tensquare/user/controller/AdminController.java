package com.jonas.tensquare.user.controller;

import com.jonas.tensquare.user.pojo.Admin;
import com.jonas.tensquare.user.service.AdminService;
import entity.ConstantVariable;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户模块：管理员控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private HttpServletRequest request;

    //管理员登录
    @PostMapping("/login")
    public Result login(@RequestBody Admin admin){
        String token;
        try {
            token = adminService.login(admin);
        }catch (RuntimeException e){
            return new Result(false,StatusCode.ERROR,ConstantVariable.LOGIN_FAIL,e.getMessage());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("name",admin.getLoginname());
        map.put("token",token);
        return new Result(true,StatusCode.OK,ConstantVariable.LOGIN_SUCCESS,map);
    }

    //条件分页查询管理员
    @GetMapping("/search/{page}/{size}")
    public Result query(@RequestBody Admin admin,@PathVariable int page,@PathVariable int size){
        Page<Admin> adminList = adminService.query(admin, page, size);
        PageResult<Admin> pageResult = new PageResult<>(adminList.getTotalElements(), adminList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //根据id删除用户
    @DeleteMapping("/{userId}")
    public Result deleteById(@PathVariable String userId){
        String authorization = request.getHeader("Authorization");
        try{
            adminService.deleteById(userId,authorization);
        }catch (RuntimeException e){
            return new Result(false,StatusCode.ACCESS_ERROR,ConstantVariable.AUTH_FAIL,e.getMessage());
        }
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }

    //根据id修改管理员
    @PutMapping("/{adminId}")
    public Result update(@PathVariable String adminId,@RequestBody Admin admin){
        adminService.update(adminId,admin);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //根据id查询管理员
    @GetMapping("/{adminId}")
    public Result findById(@PathVariable String adminId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,adminService.findById(adminId));
    }

    //查询所有管理员
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,adminService.findAll());
    }

    //添加管理员
    @PostMapping
    public Result save(@RequestBody Admin admin){
        adminService.save(admin);
        return new Result(true, StatusCode.OK, ConstantVariable.SAVE_SUCCESS);
    }
}
