package com.jonas.tensquare.gathering.controller;

import com.jonas.tensquare.gathering.pojo.Gathering;
import com.jonas.tensquare.gathering.service.GatheringService;
import entity.ConstantVariable;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 活动模块：活动控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/gathering")
public class GatheringController {

    @Autowired
    private GatheringService gatheringService;

    //根据城市分页查询活动
    @GetMapping("/city/{city}/{page}/{size}")
    public Result findByCity(@PathVariable("city") String city,
                             @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Gathering> gatheringList = gatheringService.findByCity(city, page, size);
        PageResult<Gathering> pageResult = new PageResult<>(gatheringList.getTotalElements(), gatheringList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //条件分页查询
    @PostMapping("/search/{page}/{size}")
    public Result query(@RequestBody Gathering gathering,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Gathering> gatheringList = gatheringService.query(gathering, page, size);
        PageResult<Gathering> pageResult = new PageResult<>(gatheringList.getTotalElements(), gatheringList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //条件查询
    @PostMapping("/search")
    public Result search(@RequestBody Gathering gathering){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,gatheringService.search(gathering));
    }

    //根据id删除活动
    @DeleteMapping("/{gatheringId}")
    public Result deleteById(@PathVariable("gatheringId") String gatheringId){
        gatheringService.deleteById(gatheringId);
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }

    //根据id修改活动
    @PutMapping("/{gatheringId}")
    public Result update(@PathVariable("gatheringId") String gatheringId,@RequestBody Gathering gathering){
        gatheringService.update(gatheringId,gathering);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //根据id查询活动
    @GetMapping("/{gatheringId}")
    public Result findById(@PathVariable("gatheringId") String gatheringId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,gatheringService.findById(gatheringId));
    }

    //查询所有活动
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,gatheringService.findAll());
    }

    //添加活动
    @PostMapping
    public Result save(@RequestBody Gathering gathering){
        gatheringService.save(gathering);
        return new Result(true, StatusCode.OK, ConstantVariable.SAVE_SUCCESS);
    }
}
