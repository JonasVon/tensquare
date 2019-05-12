package com.jonas.tensquare.friend.interceptor;

import entity.ConstantVariable;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("authorization");
        if(authorization != null){
            if(authorization.startsWith("Jonas ")){
                String token = authorization.substring(6);
                try{
                    Claims claims = jwtUtil.parseJWT(token);
                    String role = (String) claims.get("roles");
                    if("admin".equals(role)){
                        request.setAttribute("chaims_admin",token);
                    }
                    if("user".equals(role)){
                        request.setAttribute("chaims_user",token);
                    }
                }catch (RuntimeException e){
                    throw new RuntimeException(ConstantVariable.TOKEN_ERROR);
                }
            }
        }
        return true;
    }
}
