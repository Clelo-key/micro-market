package com.micromarket.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.micromarket.entity.CheckResult;
import com.micromarket.util.JwtUtils;
import com.micromarket.util.StringUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 拦截器
 * */

public class SysInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        System.out.println(path);
        if (handler instanceof HandlerMethod){
            String token = request.getHeader("token");
            if (StringUtil.isEmpty(token)){
                System.out.println("token为空");
                throw  new RuntimeException("签名验证不存在！");
            }else {
                int errCode = JwtUtils.validateJWT(token).getErrCode();
                Claims claims = JwtUtils.validateJWT(token).getClaims();
                // 系统管理员鉴权
                if (path.startsWith("/admin")){
                    if (claims==null || !claims.getSubject().equals("admin") || !claims.getId().equals("-1")){
                        throw  new RuntimeException("管理员，鉴权失败");
                    }else {
                        System.out.println("建权成功");
                        return true;
                    }
                }
                if (errCode==4001){
                    throw new RuntimeException("token过期");
                }
                if (claims==null){
//                    System.out.println(checkResult.getErrCode());
                    System.out.println("token不存在，获取失败");
                    throw  new RuntimeException("建权失败！");
                }else {
                    System.out.println("鉴权成功");
                    return true;
                }
            }
        }
        else {
            return true;
        }
//        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
