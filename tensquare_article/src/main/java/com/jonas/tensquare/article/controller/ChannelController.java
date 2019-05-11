package com.jonas.tensquare.article.controller;

import com.jonas.tensquare.article.pojo.Channel;
import com.jonas.tensquare.article.service.ChannelService;
import entity.ConstantVariable;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章模块：频道控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    //条件分页查询
    @PostMapping("/search/{page}/{size}")
    public Result query(@RequestBody Channel channel,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Channel> channelList = channelService.query(channel, page, size);
        PageResult<Channel> pageResult = new PageResult<>(channelList.getTotalElements(), channelList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //条件查询
    @PostMapping("/search")
    public Result search(@RequestBody Channel channel){
        List<Channel> channelList = channelService.search(channel);
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,channelList);
    }

    //根据id删除频道
    @DeleteMapping("/{channelId}")
    public Result deleteById(@PathVariable("channelId") String channelId){
        channelService.deleteById(channelId);
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }

    //根据id修改频道
    @PutMapping("/{channelId}")
    public Result update(@RequestBody Channel channel,@PathVariable("channelId") String channelId){
        channelService.update(channelId,channel);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //根据id查询频道
    @GetMapping("/{channelId}")
    public Result findById(@PathVariable("channelId") String channelId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,channelService.findById(channelId));
    }

    //查询所有频道
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,channelService.findAll());
    }

    //添加频道
    @PostMapping
    public Result save(@RequestBody Channel channel){
        channelService.save(channel);
        return new Result(true, StatusCode.OK, ConstantVariable.SAVE_SUCCESS);
    }
}
