package com.jinhui.common.controller;

import com.google.gson.Gson;
import com.jinhui.common.entity.domain.User;
import com.jinhui.common.service.UserService;
import com.jinhui.common.utils.RedisUtils;
import com.jinhui.common.utils.ResultResp;
import com.jinhui.common.weixin.pojo.PageAccessToken;
import com.jinhui.common.weixin.pojo.UserInfo;
import com.jinhui.common.weixin.utils.WeixinUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 馆控制层
 *
 * @autor wsc
 * @create 2018-05-11 14:52
 **/
@Controller
@RequestMapping("/weixin")
public class WeixinController {
    private static Logger logger = LoggerFactory.getLogger(WeixinController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value="微信网页授权")
    @GetMapping(value="/auth")
    @ResponseBody
    public ResultResp auth(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       logger.info("-------微信网页授权验证---------code: " + code + "  state: "+ state);
        logger.info("sessionId: " + request.getSession().getId().toString());
       Gson gson = new Gson();
       //获取access_token openId
        PageAccessToken pageAccessToken = null;
        if(RedisUtils.get(code) == null){
            pageAccessToken = WeixinUtil.getPageAccessToken(code);
            RedisUtils.set(code, gson.toJson(pageAccessToken),300);
        }else{
            pageAccessToken = gson.fromJson(RedisUtils.get(code),PageAccessToken.class);
        }

       logger.info("-------网页授权access_token: " + pageAccessToken.toString());

        UserInfo userInfo = null;
        if(request.getSession().getAttribute("currUser") == null){
            userInfo = WeixinUtil.getUserInfo(pageAccessToken.getAccessToken(),pageAccessToken.getOpenId());

            //保存用户信息到数据库
            User user = new User();
            BeanUtils.copyProperties(userInfo,user);
            userService.addUser(user);

            //保存用户信息到session
            request.getSession().setAttribute("currUser",userInfo);
        }else{
            userInfo = (UserInfo) request.getSession().getAttribute("currUser");
        }
       //获取用户基本信息
       logger.info("-------用户基本信息: "+userInfo.toString());
       if("create".equals(state)){
           response.sendRedirect("/reminiscence/html/create.html");   //快速建馆
       }else if("private".equals(state)){
           response.sendRedirect("/reminiscence/html/index.html");    //私人馆
       }else if("public".equals(state)){
           response.sendRedirect("/reminiscence/html/list.html");     //公开馆
       }else{
           response.sendRedirect("/reminiscence/html/test.html");    //我的
       }


        return ResultResp.successData(userInfo,"");
    }

}
