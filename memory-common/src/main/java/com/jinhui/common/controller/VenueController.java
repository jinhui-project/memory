package com.jinhui.common.controller;

import com.jinhui.common.controller.vo.MessageVo;
import com.jinhui.common.controller.vo.VenueUserVo;
import com.jinhui.common.entity.domain.AccessToken;
import com.jinhui.common.entity.domain.Message;
import com.jinhui.common.entity.domain.Venue;
import com.jinhui.common.entity.domain.VenueUser;
import com.jinhui.common.resolver.JsonParam;
import com.jinhui.common.service.AccessTokenService;
import com.jinhui.common.service.VenueService;
import com.jinhui.common.utils.DateUtils;
import com.jinhui.common.utils.ResultResp;
import com.jinhui.common.weixin.pojo.UserInfo;
import com.jinhui.common.weixin.utils.WeixinUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 馆控制层
 *
 * @autor wsc
 * @create 2018-05-11 14:52
 **/
@Controller
@RequestMapping("/venue")
public class VenueController {
    private static Logger logger = LoggerFactory.getLogger(VenueController.class);

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private VenueService venueService;

    private String uploadFolder;
    private String domainUrl;
    private String showFolder;

    @Value("${UPLOADED_FOLDER}")
    public void setUploadFolder(String uploadFolder) {
        this.uploadFolder = uploadFolder;
    }
    @Value("${SHOW_FOLDER}")
    public void setShowFolder(String showFolder) {
        this.showFolder = showFolder;
    }

    @Value("${DOMAIN_URL}")
    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    @ApiOperation(value="创建菜单")
    @PostMapping(value="/createMenu")
    @ResponseBody
    public ResultResp createMenu() {

        AccessToken accessToken = accessTokenService.queryLatestToken();
        if(accessToken != null){
            WeixinUtil.createMenu(WeixinUtil.getMenu(),accessToken.getAccessToken());
        }
        return ResultResp.successData(accessToken,"");
    }

    @ApiOperation(value="获取用户基本信息")
    @PostMapping(value="/userInfo")
    @ResponseBody
    public ResultResp userInfo(HttpServletRequest request) {
        logger.info("sessionId: "+request.getSession().getId());
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("currUser");

        return ResultResp.successData(userInfo,"");
    }

    @ApiOperation(value="上传")
    @PostMapping(value="/upload")
    @ResponseBody
    public ResultResp upload(@RequestParam("file") MultipartFile file) {
        logger.info("上传地址: "+ uploadFolder + file.getOriginalFilename());
        if (file.isEmpty()) {
            return ResultResp.fail("请选择要上传图片!");
        }
        logger.info(" fileSize: " + file.getSize() + " " + file.getContentType());
        if(!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpeg")){
           return ResultResp.fail("只支持png,jpeg格式的图片");
        }else if(file.getSize() > 5*1024*1024){
           return ResultResp.fail("只支持小于5M的图片");
        }
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFolder + file.getOriginalFilename());
            Files.write(path, bytes);

        } catch (IOException e) {
            logger.info(e.getMessage());
        }

        return ResultResp.successData(domainUrl+showFolder + file.getOriginalFilename(),null);
    }

    @ApiOperation(value="创建馆")
    @PostMapping(value="/createVenue")
    @ResponseBody
    public ResultResp createVenue(@RequestBody VenueUserVo venueUserVo,HttpServletRequest request) throws InvocationTargetException, IllegalAccessException {
        logger.info("请求参数： " + venueUserVo.toString());
        VenueUser venueUser = new VenueUser();
        BeanUtils.copyProperties(venueUser,venueUserVo);
        venueUser.setVenueId("G" + DateUtils.getCurrentDatetime());
        venueUser.setHeadImage(venueUserVo.getHeadImage());
        logger.info("sessionId: "+request.getSession().getId());

        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("currUser");

        venueService.createVenue(venueUser,userInfo.getOpenId());


        return ResultResp.successData(venueUser,"");
    }

    @ApiOperation(value="私人馆列表")
    @GetMapping(value="/privateVenueList")
    @ResponseBody
    public ResultResp privateVenueList(HttpServletRequest request) {
        logger.info("sessionId: "+request.getSession().getId());
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("currUser");

        List<VenueUser> list = venueService.queryPrivateVenue(userInfo.getOpenId());
        return ResultResp.successData(list,"");
    }

    @ApiOperation(value="公共馆列表")
    @GetMapping(value="/publicVenueList")
    @ResponseBody
    public ResultResp publicVenueList() {

        List<VenueUser> list = venueService.queryPublicVenue();
        return ResultResp.successData(list,"");
    }

    @ApiOperation(value="馆详情")
    @GetMapping(value="/venueInfo")
    @ResponseBody
    public ResultResp venueInfo(@JsonParam(value = "venueId") String venueId) {
        logger.info("venueId:  " + venueId);
        VenueUser venue = venueService.queryVenue(venueId);
        return ResultResp.successData(venue,"");
    }


    @ApiOperation(value="留言")
    @PostMapping(value="/message")
    @ResponseBody
    public ResultResp message(@JsonParam(value="venueId") String venueId,@JsonParam(value="content") String content,HttpServletRequest request) {
        logger.info("venueId:  " + venueId + "  " + content);

        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("currUser");

        Message message = new Message();
        message.setVenueId(venueId);
        message.setMessage(content);
        message.setCreateId(userInfo.getOpenId());
        venueService.addMessage(message);

        return ResultResp.successData(null,"");
    }

    @ApiOperation(value="留言列表")
    @PostMapping(value="/messageList")
    @ResponseBody
    public ResultResp messageList(@JsonParam(value="venueId") String venueId) {
        logger.info("venueId:  " + venueId);
        List<Message> messageList = venueService.queryMessageListByVenueId(venueId);
        return ResultResp.successData(messageList,"");
    }


}
