package com.jonas.tensquare.recruit.controller;

import com.jonas.tensquare.recruit.pojo.Recruit;
import com.jonas.tensquare.recruit.service.RecruitService;
import entity.ConstantVariable;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 招聘模块：招聘信息控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    //查询最新招聘信息(根据state不为0查询前12条并且创建时间按倒序排列)
    @GetMapping("/search/newlist")
    public Result newList(){
        List<Recruit> list = recruitService.findTop12ByStateNotOrderByCreatetimeDesc();
        return new Result(true,StatusCode.OK, ConstantVariable.QUERY_SUCCESS,list);
    }

    //查询推荐招聘信息(根据state为2并且按照创建时间倒序排列的前4条记录)
    @GetMapping("/search/recommend")
    public Result recommend(){
        List<Recruit> list = recruitService.findTop4ByStateOrderByCreatetimeDesc();
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,list);
    }

    //条件分页查询
    @PostMapping("/search/{page}/{size}")
    public Result query(@RequestBody Recruit recruit,@PathVariable("page") int page,@PathVariable("size") int size){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,recruitService.query(recruit,page,size));
    }

    //条件查询
    @PostMapping("/search")
    public Result search(@RequestBody Recruit recruit){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,recruitService.search(recruit));
    }

    //删除招聘信息
    @DeleteMapping("/{recruitId}")
    public Result deleteById(@PathVariable("recruitId") String recruitId){
        recruitService.deleteById(recruitId);
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }

    //修改招聘信息
    @PutMapping("/{recruitId}")
    public Result update(@RequestBody Recruit recruit,@PathVariable("recruitId") String recruitId){
        recruit.setId(recruitId);
        recruitService.update(recruitId,recruit);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //根据id查询招聘信息
    @GetMapping("/{recruitId}")
    public Result findById(@PathVariable("recruitId") String recruitId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,recruitService.findById(recruitId));
    }

    //查询所有招聘信息
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,recruitService.findAll());
    }

    //添加招聘信息
    @PostMapping
    public Result save(@RequestBody Recruit recruit){
        recruitService.save(recruit);
        return new Result(true, StatusCode.OK,ConstantVariable.SAVE_SUCCESS);
    }
}
