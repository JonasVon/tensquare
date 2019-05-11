package com.jonas.tensquare.problem.controller;

import com.jonas.tensquare.problem.pojo.Problem;
import com.jonas.tensquare.problem.service.ProblemService;
import entity.ConstantVariable;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 问答模块：问题控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    //根据标签id分页查询所有问题
    @GetMapping("/all/{labelId}/{page}/{size}")
    public Result findProblemBylabelId(@PathVariable("labelId") String lebelId,
                                       @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Problem> pageList = problemService.findProblemBylabelId(lebelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //根据标签id分页查询等待回答列表(等待回答问题：回复数量reply为0)
    @GetMapping("/waitlist/{labelId}/{page}/{size}")
    public Result findProblemByReplyEqualsOrderByCreatetime(@PathVariable("labelId") String labelId,
                                                            @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Problem> pageList = problemService.findProblemByReplyEqualsOrderByCreatetime(labelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //根据标签id分页查询热门问题列表(热门问题：回复数量reply倒序)
    @GetMapping("/hotlist/{labelId}/{page}/{size}")
    public Result findProblemOrderByReply(@PathVariable("labelId") String labelId,
                                          @PathVariable("page") int page,@PathVariable("size") int size){
        Page<Problem> pageList = problemService.findProblemOrderByReply(labelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //根据标签id分页查询最新问题列表(最新问题：回复时间replytime倒序)
    @GetMapping("/newlist/{labelId}/{page}/{size}")
    public Result findNewList(@PathVariable("labelId") String labelId,@PathVariable("page") int page,
                              @PathVariable("size") int size){
        Page<Problem> pageList = problemService.findByLabelIdOrderByReplytime(labelId, page, size);
        PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //条件分页查询问题
    @PostMapping("/search/{page}/{size}")
    public Result query(@RequestBody Problem problem,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Problem> pageList = problemService.query(problem, page, size);
        PageResult<Problem> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,pageResult);
    }

    //条件查询问题
    @PostMapping("/search")
    public Result search(@RequestBody Problem problem){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,problemService.search(problem));
    }

    //根据问题id删除问题
    @DeleteMapping("/{problemId}")
    public Result deleteById(@PathVariable("problemId") String problemId){
        problemService.deleteById(problemId);
        return new Result(true,StatusCode.OK,ConstantVariable.DELETE_SUCCESS);
    }

    //根据问题id修改问题
    @PutMapping("/{problemId}")
    public Result update(@PathVariable("problemId") String problemId,@RequestBody Problem problem){
        problemService.update(problemId,problem);
        return new Result(true,StatusCode.OK,ConstantVariable.UPDATE_SUCCESS);
    }

    //根据问题id查询问题
    @GetMapping("/{problemId}")
    public Result findById(@PathVariable("problemId") String problemId){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,problemService.findById(problemId));
    }

    //查询所有问题
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,ConstantVariable.QUERY_SUCCESS,problemService.findAll());
    }

    //添加问题
    @PostMapping
    public Result save(@RequestBody Problem problem){
        problemService.save(problem);
        return new Result(true, StatusCode.OK, ConstantVariable.SAVE_SUCCESS);
    }
}
