package com.jinhui.common.weixin.utils;

import com.jinhui.common.entity.domain.AccessToken;
import com.jinhui.common.weixin.menu.*;
import com.jinhui.common.weixin.pojo.PageAccessToken;
import com.jinhui.common.weixin.pojo.UserInfo;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

/**
 * 获取微信access_token
 *
 * @autor wsc
 * @create 2018-05-11 13:46
 **/
@Component
public class WeixinUtil {
    private static Logger logger = LoggerFactory.getLogger(WeixinUtil.class);
    private static String appID;
    private static String appsecret;
    private static String redirectUri;

    @Value("${wechat.mp.appId}")
    public  void setAppID(String appid) {
        appID = appid;
    }
    @Value("${wechat.mp.secret}")
    public  void setAppsecret(String appSecret) {
        appsecret = appSecret;
    }

    @Value("${wechat.mp.redirect-uri}")
    public void setRedirectUri(String redirecturi) {
        redirectUri = redirecturi;
    }

    // 凭证获取（GET）
    public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    // 菜单创建（POST）
    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    //通过code获取网页访问的access_token
    public static String page_auth_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";

    //通过网页access_token, openId 获取用户基本信息
    public static String userinfo_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";



    /**
     * 发送https请求
     *
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl,String requestMethod, String outputStr) {

        JSONObject jsonObject = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            logger.error("连接超时：{}", ce);
        } catch (Exception e) {
            logger.error("https请求异常：{}", e);
        }
        return jsonObject;
    }

    /**
     * 获取接口访问凭证
     *
     * @return
     */
    public static AccessToken getToken() {
        logger.info("appID:  " + appID + "  appsecret: "+ appsecret);
        String requestUrl = token_url.replace("APPID", appID).replace("APPSECRET", appsecret);
        logger.info("requestUrl:  " + requestUrl);
        AccessToken token = null;
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsRequest(requestUrl,"GET", null);

        if (null != jsonObject) {
            try {
                token = new AccessToken();
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getString("expires_in"));
            } catch (JSONException e) {
                token = null;
                // 获取token失败
                logger.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return token;
    }

    /**
     * 获取网页授权access_token
     * @param code
     * @return
     */
    public static PageAccessToken getPageAccessToken(String code){
        String requestUrl = page_auth_url.replace("APPID", appID).replace("APPSECRET", appsecret).replace("CODE",code);
    //    logger.info("requestUrl:  " + requestUrl);
        PageAccessToken pageAccessToken = null;
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsRequest(requestUrl,"GET", null);

        if (null != jsonObject) {
            try {
                pageAccessToken = new PageAccessToken();
                pageAccessToken.setAccessToken(jsonObject.getString("access_token"));
                pageAccessToken.setExpiresIn(jsonObject.getString("expires_in"));
                pageAccessToken.setRefreshToken(jsonObject.getString("refresh_token"));
                pageAccessToken.setOpenId(jsonObject.getString("openid"));
                pageAccessToken.setScope(jsonObject.getString("scope"));
            } catch (JSONException e) {
                pageAccessToken = null;
                // 获取token失败
                logger.error("获取网页授权access_token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return pageAccessToken;
    }

    //获取用户基本信息
    public static UserInfo getUserInfo(String pageAccessToken,String OPENID){
        String userInfoUrl = userinfo_url.replace("ACCESS_TOKEN", pageAccessToken).replace("OPENID", OPENID);
       // logger.info("requestUrl:  " + userInfoUrl);
        UserInfo userInfo = null;
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsRequest(userInfoUrl,"GET", null);
       // logger.info(jsonObject.toString());
        if (null != jsonObject) {
            try {
                userInfo = new UserInfo();
                userInfo.setOpenId(jsonObject.getString("openid"));
                userInfo.setNickName(jsonObject.getString("nickname"));
                userInfo.setSex(jsonObject.getString("sex"));
                userInfo.setProvince(jsonObject.getString("province"));
                userInfo.setCity(jsonObject.getString("city"));
                userInfo.setCountry(jsonObject.getString("country"));
                userInfo.setHeadImageUrl(jsonObject.getString("headimgurl"));
            } catch (JSONException e) {
                userInfo = null;
                // 获取token失败
                logger.error("获取用户基本信息失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return userInfo;
    }



    /**
     * 创建菜单
     *
     * @param menu 菜单实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(Menu menu, String accessToken) {
        int result = 0;
        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.fromObject(menu).toString();
        // 调用接口创建菜单
        JSONObject jsonObject = httpsRequest(url, "POST", jsonMenu);
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                logger.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }

        return result;
    }

    /**
     * 组装菜单数据
     *
     * @return
     */
    public static Menu getMenu() {
        logger.info("appID:  " + appID + "  appsecret: "+ appsecret +"  redirect_uri: " + redirectUri);
        ViewButton btn21 = new ViewButton();
        btn21.setName("私人馆");
        btn21.setType("view");
        btn21.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appID+"&redirect_uri="+redirectUri+"&response_type=code&scope=snsapi_userinfo&state=private#wechat_redirect");
        ViewButton btn22 = new ViewButton();
        btn22.setName("公开馆");
        btn22.setType("view");
        btn22.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appID+"&redirect_uri="+redirectUri+"&response_type=code&scope=snsapi_userinfo&state=public#wechat_redirect");
        logger.info(btn21.getUrl());

        ViewButton mainBtn1 = new ViewButton();
        mainBtn1.setName("快速建馆");
        mainBtn1.setType("view");
        mainBtn1.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appID+"&redirect_uri="+redirectUri+"&response_type=code&scope=snsapi_userinfo&state=create#wechat_redirect");

        ComplexButton mainBtn2 = new ComplexButton();
        mainBtn2.setName("在线寄语");
        mainBtn2.setSub_button(new ViewButton[] { btn21, btn22});


        ViewButton mainBtn3 = new ViewButton();
        mainBtn3.setName("我的");
        mainBtn3.setType("view");
        mainBtn3.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appID+"&redirect_uri="+redirectUri+"&response_type=code&scope=snsapi_userinfo&state=mine#wechat_redirect");

        /**
         * 封装整个菜单
         */
        Menu menu = new Menu();
        menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });

        return menu;
    }

}
