package com.jinhui.common.controller;

import com.jinhui.common.controller.vo.VenueUserVo;
import com.jinhui.common.entity.domain.AccessToken;
import com.jinhui.common.entity.domain.Venue;
import com.jinhui.common.entity.domain.VenueUser;
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
        venueUser.setHeadImage(domainUrl+venueUserVo.getHeadImage());
        logger.info("sessionId: "+request.getSession().getId());

       // UserInfo userInfo = (UserInfo) request.getSession().getAttribute("currUser");

        venueService.createVenue(venueUser,"oKtQM09cN15sk0REkHPKrZyugOKA");


        return ResultResp.successData(venueUser,"");
    }

    @ApiOperation(value="私人馆列表")
    @GetMapping(value="/privateVenueList")
    @ResponseBody
    public ResultResp privateVenueList(HttpServletRequest request) {
        logger.info("sessionId: "+request.getSession().getId());
        //UserInfo userInfo = (UserInfo) request.getSession().getAttribute("currUser");

        List<VenueUser> list = venueService.queryPrivateVenue("oKtQM09cN15sk0REkHPKrZyugOKA");
        return ResultResp.successData(list,"");
    }

    @ApiOperation(value="公共馆列表")
    @GetMapping(value="/publicVenueList")
    @ResponseBody
    public ResultResp publicVenueList() {

        List<VenueUser> list = venueService.queryPublicVenue();
        return ResultResp.successData(list,"");
    }


}
