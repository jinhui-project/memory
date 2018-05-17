package com.jinhui.common.controller;

import com.jinhui.common.weixin.utils.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 来接收微信服务器传来信息
 *
 * @autor wsc
 * @create 2018-05-10 16:41
 **/
@WebServlet(name="handlerServlet",urlPatterns={"/handlerServlet"})
public class HandlerServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(HandlerServlet.class);
    private static final long serialVersionUID = 4323197796926899691L;

    /**
     * 确认请求来自微信服务器
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("--------  接收微信服务器传来信息   ----------------");
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();

        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
        }

        out.close();
        out = null;
    }

    /**
     * 处理微信服务器发来的消息
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO 消息的接收、处理、响应
        logger.info("--------  处理微信服务器传来信息   ----------------");

    }
}
