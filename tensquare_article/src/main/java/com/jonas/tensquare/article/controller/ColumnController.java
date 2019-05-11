package com.jonas.tensquare.article.controller;

import com.jonas.tensquare.article.pojo.Column;
import com.jonas.tensquare.article.service.ColumnService;
import entity.ConstantVariable;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 文章模块：专栏控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/column")
public class ColumnController {

    @Autowired
    private ColumnService columnService;

    //条件分页查询
    @GetMapping("/search/{page}/{size}")
    public Result query(@RequestBody Column column,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Column> columnList = columnService.query(column, page, size);
        PageResult<Column> pageResult = new PageResult<>(columnList.getTotalElements(), columnList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //条件查询
    @GetMapping("/search")
    public Result search(@RequestBody Column column){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,columnService.search(column));
    }

    //根据id删除专栏
    @DeleteMapping("/{columnId}")
    public Result deleteById(@PathVariable("columnId") String columnId){
        columnService.deleteById(columnId);
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }

    //根据id修改专栏
    @PutMapping("/{columnId}")
    public Result update(@PathVariable("columnId") String columnId,@RequestBody Column column){
        columnService.update(columnId,column);
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS);
    }

    //根据id查询专栏
    @GetMapping("/{columnId}")
    public Result findById(@PathVariable("columnId") String columnId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,columnService.findById(columnId));
    }

    //查询所有专栏
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,columnService.findAll());
    }

    //添加专栏
    @PostMapping
    public Result save(@RequestBody Column column){
        columnService.save(column);
        return new Result(true, StatusCode.OK, ConstantVariable.SAVE_SUCCESS);
    }
}
