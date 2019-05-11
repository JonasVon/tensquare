package com.jonas.tensquare.spit.controller;

import com.jonas.tensquare.spit.pojo.Spit;
import com.jonas.tensquare.spit.service.SpitService;
import entity.ConstantVariable;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 吐槽模块：吐槽控制器
 */
@RestController
@CrossOrigin
@RequestMapping("spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    //点赞吐槽
    @PutMapping("/thumbup/{spitId}")
    public Result thumbup(@PathVariable("spitId") String spitId){
        String message = spitService.thumbup(spitId);
        return new Result(true,StatusCode.OK,ConstantVariable.THUMBUP_SUCCESS,message);
    }

    //分页查询父节点吐槽
    @GetMapping("/comment/{parentid}/{page}/{size}")
    public Result findByParentid(@PathVariable("parentid") String parentid,
                                 @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Spit> spitList = spitService.findByParentid(parentid, page, size);
        PageResult<Spit> pageResult = new PageResult<>(spitList.getTotalElements(), spitList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //根据id删除吐槽
    @DeleteMapping("/{spitId}")
    public Result deleteById(@PathVariable("spitId") String spitId){
        spitService.deleteById(spitId);
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }

    //根据id修改吐槽
    @PutMapping("/{spitId}")
    public Result update(@PathVariable("spitId") String spitId,@RequestBody Spit spit){
        spitService.update(spitId,spit);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //根据id查询吐槽
    @GetMapping("/{spitId}")
    public Result findById(@PathVariable("spitId") String spitId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,spitService.findById(spitId));
    }

    //查询所有吐槽
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,spitService.findAll());
    }

    //添加吐槽
    @PostMapping
    public Result save(@RequestBody Spit spit){
        spitService.save(spit);
        return new Result(true, StatusCode.OK, ConstantVariable.SAVE_SUCCESS);
    }
}
