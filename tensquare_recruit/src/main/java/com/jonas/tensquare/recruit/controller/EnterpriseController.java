package com.jonas.tensquare.recruit.controller;

import com.jonas.tensquare.recruit.pojo.Enterprise;
import com.jonas.tensquare.recruit.service.EnterpriseService;
import entity.ConstantVariable;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 招聘模块：企业信息控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    private EnterpriseService enterpriseService;

    //条件分页查询企业
    @PostMapping("/search/{page}/{size}")
    public Result findQuery(@RequestBody Enterprise enterprise,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Enterprise> pageData = enterpriseService.findQuery(enterprise, page, size);
        PageResult<Enterprise> pageResult = new PageResult<>(pageData.getTotalElements(), pageData.getContent());
        return new Result(true,StatusCode.OK, ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //查询热门企业(字段ishot为1)
    @GetMapping("/search/hotlist")
    public Result findHotList(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,enterpriseService.findHotList());
    }

    //条件查询企业
    @PostMapping("/search")
    public Result findSearch(@RequestBody Enterprise enterprise){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,enterpriseService.findSearch(enterprise));
    }

    //查询所有企业
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,enterpriseService.findAll());
    }

    //根据id查询企业
    @GetMapping("/{enterpriseId}")
    public Result findById(@PathVariable("enterpriseId") String enterpriseId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,enterpriseService.findById(enterpriseId));
    }

    //添加企业
    @PostMapping
    public Result save(@RequestBody Enterprise enterprise){
        enterpriseService.save(enterprise);
        return new Result(true, StatusCode.OK,ConstantVariable.SAVE_SUCCESS);
    }

    //修改企业
    @PutMapping("/{enterpriseId}")
    public Result update(@PathVariable("enterpriseId") String enterpriseId,@RequestBody Enterprise enterprise){
        enterpriseService.update(enterpriseId,enterprise);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //删除企业
    @DeleteMapping("/{enterpriseId}")
    public Result deleteById(@PathVariable("enterpriseId") String enterpriseId){
        enterpriseService.deleteById(enterpriseId);
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }
}
