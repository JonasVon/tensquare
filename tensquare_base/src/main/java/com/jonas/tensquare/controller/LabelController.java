package com.jonas.tensquare.controller;

import com.jonas.tensquare.pojo.Label;
import com.jonas.tensquare.service.LabelService;
import entity.ConstantVariable;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 基础模块：标签控制器
 */
@RestController
@CrossOrigin
@RefreshScope
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    //条件分页查询
    @PostMapping("/search/{page}/{size}")
    public Result pageQuery(@RequestBody Label label,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Label> pageData = labelService.pageQuery(label,page,size);
        PageResult<Label> pageResult = new PageResult<>(pageData.getTotalElements(), pageData.getContent());
        return new Result(true,StatusCode.OK, ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //根据条件查询标签
    @PostMapping("/search")
    public Result findSearch(@RequestBody Label label){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,labelService.findSearch(label));
    }

    //查询所有标签
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,labelService.findAll());
    }

    //根据id查询标签
    @GetMapping(value="/{labelId}")
    public Result findById(@PathVariable("labelId") String labelId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,labelService.findById(labelId));
    }

    //添加标签
    @PostMapping
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true,StatusCode.OK,ConstantVariable.SAVE_SUCCESS);
    }

    //修改标签
    @PutMapping(value="/{labelId}")
    public Result update(@PathVariable("labelId") String labelId,@RequestBody Label label){
        labelService.update(labelId,label);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //删除标签
    @DeleteMapping(value="/{labelId}")
    public Result deleteById(@PathVariable("labelId") String labelId){
        labelService.deleteById(labelId);
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }

}
