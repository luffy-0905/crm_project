package com.java.crm.settings.web.interceptor;

import com.alibaba.druid.sql.visitor.functions.Concat;
import com.java.crm.commens.contants.Contants;
import com.java.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //如果用户没有登录成功,就返回登录页面
        HttpSession session = httpServletRequest.getSession();
        User user=(User) session.getAttribute(Contants.SESSION_USER);
        if(user==null){
            //自己重定向时，url的路径必须加项目的名称
            //在springMVC自己的重定向时不需要，因为SpringMVC已经帮我们做了
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
