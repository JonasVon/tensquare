package com.jonas.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import entity.ConstantVariable;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        return "pre";//pre表示前置过滤器
    }

    @Override
    public int filterOrder() {
        return 0;//返回值表示优先级，数字越大，优先级越低
    }

    @Override
    public boolean shouldFilter() {
        return true;//是否执行该过滤器，true表示执行
    }

    //过滤器逻辑,返回任何Object类型的值都表示继续执行,如果返回setSendZuulResponse(false)则表示不再执行
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String authorization = request.getHeader("authorization");

        if(request.getMethod().equals("OPTIONS")){
            return null;
        }

        if(request.getRequestURL().toString().indexOf("login") > 0){
            return null;
        }

        if(authorization != null && !"".equals(authorization)){
            if(authorization.startsWith("Jonas")){
                String token = authorization.substring(6);
                try{
                    Claims claims = jwtUtil.parseJWT(token);
                    if(claims != null && "admin".equals(claims.get("roles"))){
                        requestContext.addZuulRequestHeader("authorization",authorization);
                        return null;
                    }
                }catch (Exception e){
                    requestContext.setSendZuulResponse(false);
                }
            }
        }
        requestContext.setResponseStatusCode(403);
        requestContext.setResponseBody(ConstantVariable.AUTH_FAIL);
        requestContext.getResponse().setContentType("text/html;charset=utf-8");
        requestContext.setSendZuulResponse(false);
        return null;
    }
}
