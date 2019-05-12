package com.jonas.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("tensquare-user")
public interface UserClient {

    @PostMapping("/user/{userid}/{friendid}/{x}")
    void updateFanAndFollow(@PathVariable("userid") String userid,
                            @PathVariable("friendid") String friendid,
                            @PathVariable("x") int x);
}
