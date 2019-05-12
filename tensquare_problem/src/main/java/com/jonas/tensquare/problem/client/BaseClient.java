package com.jonas.tensquare.problem.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("tensquare-base")
public interface BaseClient {

    @GetMapping(value="/label/{labelId}")
    Result findById(@PathVariable("labelId") String labelId);
}
