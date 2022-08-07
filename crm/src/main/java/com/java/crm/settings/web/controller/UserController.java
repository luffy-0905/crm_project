package com.java.crm.settings.web.controller;

import com.java.crm.commens.contants.Contants;
import com.java.crm.commens.domain.ReturnObject;
import com.java.crm.commens.utils.DateUtils;
import com.java.crm.settings.domain.User;
import com.java.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Map<String,Object> map=new HashMap<>();
        map.put("loginAct" ,loginAct);
        map.put("loginPwd" ,loginPwd);
        //调用service方法
        User user = userService.queryUserByActAndPwd(map);
        //根据查询结果，生成响应信息
        ReturnObject returnObject=new ReturnObject();
        if(user==null){
            //用户名和密码不能为空
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名和密码错误");
        }else{
            //进一步判断用户是否合法
            String nowStr = DateUtils.formatDateTime(new Date());
            if(nowStr.compareTo(user.getExpireTime())>0){
                //登录失败,账号时间过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号时间过期");
            }else if ("0".equals(user.getLockState())){
                //登录失败,状态被锁
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁");
            }else if (!user.getAllowIps().contains(request.getRemoteAddr())){
                //登录失败，IP受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("IP受限");
            }else {
                //登录成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                //登录成功后,将user的值保存在session作用域
                session.setAttribute(Contants.SESSION_USER,user);

                //如果需要记住密码,就往外写cookie
                if("true".equals(isRemPwd)){
                    Cookie c1 = new Cookie("loginAct", user.getLoginAct());
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", user.getLoginPwd());
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c2);
                }else {
                    Cookie c1 = new Cookie("loginAct", "1");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2 = new Cookie("loginPwd", "1");
                    c1.setMaxAge(0);
                    response.addCookie(c2);
                }
            }
        }
        return returnObject;
    }
    //安全退出
    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //销毁cookie
        Cookie c1 = new Cookie("loginAct", "1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd", "1");
        c1.setMaxAge(0);
        response.addCookie(c2);
        //销毁session
        session.invalidate();
        return "redirect:/";
    }
}
