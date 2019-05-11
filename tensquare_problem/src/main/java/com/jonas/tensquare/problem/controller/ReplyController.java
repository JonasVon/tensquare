package com.jonas.tensquare.problem.controller;

import com.jonas.tensquare.problem.pojo.Reply;
import com.jonas.tensquare.problem.service.ReplyService;
import entity.ConstantVariable;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 问答模块：回答控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    //根据问题id查询回答列表(分页)
    @GetMapping("/problem/{problemId}/{page}/{size}")
    public Result findReplyByProblemId(@PathVariable("problemId") String problemId,
                                       @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Reply> replyList = replyService.findReplyByProblemId(problemId, page, size);
        PageResult<Reply> pageResult = new PageResult<>(replyList.getTotalElements(), replyList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //条件分页查询回答
    @PostMapping("/search/{page}/{size}")
    public Result search(@RequestBody Reply reply,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Reply> replyList = replyService.search(reply, page, size);
        PageResult<Reply> pageResult = new PageResult<>(replyList.getTotalElements(), replyList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //根据id删除回答
    @DeleteMapping("/{replyId}")
    public Result deleteById(@PathVariable("replyId") String replyId){
        replyService.delete(replyId);
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }

    //根据id修改回答
    @PutMapping("/{replyId}")
    public Result update(@RequestBody Reply reply,@PathVariable("replyId") String replyId){
        replyService.update(replyId,reply);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //根据id查询回答
    @GetMapping("/{replyId}")
    public Result findById(@PathVariable("replyId") String replyId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,replyService.findById(replyId));
    }

    //查询所有回答
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,replyService.findAll());
    }

    //添加回答
    @PostMapping
    public Result save(@RequestBody Reply reply){
        replyService.save(reply);
        return new Result(true, StatusCode.OK, ConstantVariable.SAVE_SUCCESS);
    }
}
